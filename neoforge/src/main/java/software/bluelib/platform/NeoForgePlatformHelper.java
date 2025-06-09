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
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforgespi.language.IModInfo;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.mod.ModMeta;
import software.bluelib.api.registry.NeoRecipeGenerator;

import static software.bluelib.api.registry.AbstractRegistryBuilder.getModID;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String pModId) {
        return ModList.get().isLoaded(pModId);
    }

    @Override
    public Set<String> getLoadedMods() {
        return ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ModMeta> getLoadedModMetadata() {
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
    public BlueLibConstants.Environment getEnvironment() {
        return FMLEnvironment.dist.isClient() ? BlueLibConstants.Environment.CLIENT : BlueLibConstants.Environment.SERVER;
    }

    @Override
    public BlueLibConstants.ModAPI getAPI() {
        return BlueLibConstants.ModAPI.NEOFORGE;
    }

    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public JsonElement generateRecipeJson(String modId, String name, BiConsumer<RecipeOutput, Supplier<JsonElement>> recipeConsumer) {
        return NeoRecipeGenerator.generateRecipeJson(recipeConsumer);
    }

    @Override
    public Path getAssetsDir(boolean isCommon) {
        if(isCommon) {
            return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("common/src/main/resources/assets/"+ getModID());
        }
        return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("src/main/resources/assets/" + getModID());
    }

    @Override
    public Path getDataDir(boolean isCommon) {
        if(isCommon) {
            return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("common/src/main/resources/data/"+ getModID());
        }
        return FMLPaths.GAMEDIR.get().getParent().getParent().resolve("src/main/resources/data/" + getModID());
    }
}
