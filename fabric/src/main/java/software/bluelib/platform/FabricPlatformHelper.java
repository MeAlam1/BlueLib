/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.mod.ModMeta;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public String getPlatformName() {
		return "Fabric";
	}

	@Override
	public boolean isModLoaded(String pModId) {
		return FabricLoader.getInstance().isModLoaded(pModId);
	}

	@Override
	public Set<String> getLoadedMods() {
		return FabricLoader.getInstance().getAllMods().stream()
				.map(modContainer -> modContainer.getMetadata().getId())
				.collect(Collectors.toSet());
	}

	@Override
	public List<ModMeta> getLoadedModMetadata() {
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
	public Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public BlueLibConstants.Environment getEnvironment() {
		return switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> BlueLibConstants.Environment.CLIENT;
			case SERVER -> BlueLibConstants.Environment.SERVER;
		};
	}

	@Override
	public BlueLibConstants.ModAPI getAPI() {
		return BlueLibConstants.ModAPI.FABRIC;
	}

	@Override
	public MinecraftServer getServer() {
		return this.getEnvironment() == BlueLibConstants.Environment.CLIENT ? Minecraft.getInstance().getSingleplayerServer() : BlueLibConstants.server;
	}
}
