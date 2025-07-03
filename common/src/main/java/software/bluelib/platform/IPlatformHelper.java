/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.Environment;
import software.bluelib.api.ModAPI;
import software.bluelib.api.event.mod.ModMeta;

@SuppressWarnings("unused")
public interface IPlatformHelper {

	@NotNull
	String getPlatformName();

	boolean isModLoaded(@NotNull String pModId);

	@NotNull
	Set<String> getLoadedMods();

	@NotNull
	List<ModMeta> getLoadedModMetadata();

	boolean isDevelopmentEnvironment();

	@NotNull
	default String getEnvironmentName() {
		return isDevelopmentEnvironment() ? "development" : "production";
	}

	@NotNull
	Environment getEnvironment();

	@NotNull
	ModAPI getAPI();

	@Nullable
	MinecraftServer getServer();
}
