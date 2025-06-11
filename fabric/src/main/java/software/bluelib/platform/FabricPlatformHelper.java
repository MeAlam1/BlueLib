/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.platform;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.api.registry.FabricRecipeGenerator;

import static software.bluelib.api.registry.AbstractRegistryBuilder.getModID;

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

    @Override
    public JsonElement generateRecipeJson(String modId, String name, BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
        return FabricRecipeGenerator.generateRecipeJson(recipeConsumer);
    }

    @Override
    public Path getAssetsDir(boolean isCommon) {
        if(isCommon) {
            return FabricLoader.getInstance().getGameDir().getParent().getParent().resolve("common/src/main/resources/assets/"+ getModID());
        }
        return FabricLoader.getInstance().getGameDir().getParent().getParent().resolve("src/main/resources/assets/"+ getModID());
    }

    @Override
    public Path getDataDir(boolean isCommon) {
        if(isCommon) {
            return FabricLoader.getInstance().getGameDir().getParent().getParent().resolve("common/src/main/resources/data/"+ getModID());
        }
        return FabricLoader.getInstance().getGameDir().getParent().getParent().resolve("src/main/resources/data/"+ getModID());
    }
}
