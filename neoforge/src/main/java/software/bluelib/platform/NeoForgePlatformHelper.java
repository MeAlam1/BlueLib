/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import com.google.gson.JsonElement;
import java.nio.file.Path;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.Environment;
import software.bluelib.api.ModAPI;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.api.registry.NeoRecipeGenerator;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public @NotNull String getPlatformName() {
		return "NeoForge";
	}

	@Override
	public boolean isModLoaded(@NotNull String pModId) {
		return ModList.get().isLoaded(pModId);
	}

	@Override
	public @NotNull Set<String> getLoadedMods() {
		return ModList.get().getMods().stream()
				.map(IModInfo::getModId)
				.collect(Collectors.toSet());
	}

	@Override
	public @NotNull List<ModMeta> getLoadedModMetadata() {
		List<ModMeta> mods = new ArrayList<>();
		for (IModInfo modInfo : ModList.get().getMods()) {
			String modId = modInfo.getModId();
			String displayName = modInfo.getDisplayName();
			String version = modInfo.getVersion().toString();
			String description = modInfo.getDescription();
			Optional<String> logoFile = modInfo.getLogoFile();
			mods.add(new ModMeta(modId, displayName, version, description, logoFile));
		}
		return mods;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public @NotNull Environment getEnvironment() {
		return FMLEnvironment.dist.isClient() ? Environment.CLIENT : Environment.SERVER;
	}

	@Override
	public @NotNull ModAPI getAPI() {
		return ModAPI.NEOFORGE;
	}

	@Override
	public @Nullable MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}

	@Override
	public JsonElement generateRecipeJson(String modId, String name, BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
		return NeoRecipeGenerator.generateRecipeJson(recipeConsumer);
	}

	@Override
	public Path getAssetsDir(boolean isCommon) {
		if (isCommon) {
			return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("common/src/main/resources/assets/" + getModID());
		}
		return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("src/main/resources/assets/" + getModID());
	}

	@Override
	public Path getDataDir(boolean isCommon) {
		if (isCommon) {
			return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("common/src/main/resources/data/" + getModID());
		}
		return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("src/main/resources/data/" + getModID());
	}
}
