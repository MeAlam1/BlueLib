/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public class ModIntegration {

	@NotNull
	private static final List<ModMeta> loadedModMetas = new ArrayList<>();
	@NotNull
	private static ExcludedMods excludedModsProvider = new ExcludedMods();

	public static void setExcludedModsProvider(@NotNull ExcludedMods pProvider) {
		excludedModsProvider = pProvider;
	}

	public static void checkSupportMods() {
		Set<String> excludedMods = excludedModsProvider.getExcludedMods();

		Set<String> loadedMods = BlueLibConstants.PlatformHelper.PLATFORM.getLoadedMods().stream()
				.filter(mod -> !excludedMods.contains(mod))
				.collect(Collectors.toSet());

		if (!loadedMods.isEmpty()) {
			StringBuilder modsMessage = new StringBuilder();
			for (String mod : loadedMods) {
				ModMeta modMeta = getModMeta(mod);
				if (modMeta != null) {
					loadedModMetas.add(modMeta);
					BlueLibConstants.PlatformHelper.EVENT_PROXY.onModLoaded(modMeta);
				}
				modsMessage.append(mod).append("\n");
			}
			BlueLibConstants.PlatformHelper.EVENT_PROXY.onAllModsLoaded(loadedModMetas);
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.translate("mod.loaded", Component.literal(modsMessage.toString())));
		} else {
			BlueLibConstants.PlatformHelper.EVENT_PROXY.onAllModsLoaded(loadedModMetas);
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.translate("mod.loaded.empty"));
		}
	}

	@Nullable
	public static ModMeta getModMeta(@NotNull String pModId) {
		return BlueLibConstants.PlatformHelper.PLATFORM.getLoadedModMetadata().stream()
				.filter(modMeta -> modMeta.modId().equals(pModId))
				.findFirst()
				.orElse(null);
	}

	@NotNull
	public static List<ModMeta> getLoadedModMetas() {
		return loadedModMetas;
	}
}
