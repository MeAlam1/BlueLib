/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.Environment;
import software.bluelib.api.ModAPI;
import software.bluelib.api.event.mod.ModMeta;

public class FabricPlatformHelper implements IPlatformHelper {

	public FabricPlatformHelper() {}

	@Override
	public @NotNull String getPlatformName() {
		return "Fabric";
	}

	@Override
	public boolean isModLoaded(@NotNull String pModId) {
		return FabricLoader.getInstance().isModLoaded(pModId);
	}

	@Override
	public @NotNull Set<String> getLoadedMods() {
		return FabricLoader.getInstance().getAllMods().stream()
				.map(modContainer -> modContainer.getMetadata().getId())
				.collect(Collectors.toSet());
	}

	@Override
	public @NotNull List<ModMeta> getLoadedModMetadata() {
		List<ModMeta> mods = new ArrayList<>();
		for (ModContainer modInfo : FabricLoader.getInstance().getAllMods()) {
			String modId = modInfo.getMetadata().getId();
			String displayName = modInfo.getMetadata().getName();
			String version = modInfo.getMetadata().getVersion().toString();
			String description = modInfo.getMetadata().getDescription();
			Optional<String> logoFile = modInfo.getMetadata().getIconPath(128);
			mods.add(new ModMeta(modId, displayName, version, description, logoFile));
		}
		return mods;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public @NotNull Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public @NotNull Environment getEnvironment() {
		return switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> Environment.CLIENT;
			case SERVER -> Environment.SERVER;
		};
	}

	@Override
	public @NotNull ModAPI getAPI() {
		return ModAPI.FABRIC;
	}
}
