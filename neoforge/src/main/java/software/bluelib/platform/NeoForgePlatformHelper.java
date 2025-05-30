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
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforgespi.language.IModInfo;
import software.bluelib.BlueLib;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.mod.ModMeta;

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
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
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
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String pId, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return BlueLib.DATA_COMPONENTS_REGISTER.registerComponentType(pId, pBuilder);
    }
}
