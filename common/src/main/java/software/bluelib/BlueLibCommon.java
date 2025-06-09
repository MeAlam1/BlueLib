/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.BlueRegistryBuilder;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.registry.BlueNetworkRegistry;
import software.bluelib.registry.TestEntityReg;

public class BlueLibCommon {

    /**
     * Initializes the {@link AbstractRegistryBuilder} instance with the mod ID. Replace {@link BlueLibConstants#MOD_ID} with your mod's unique mod ID to register content under your mod's namespace.
     * <p>
     * This is essential for registering mod content such as items, blocks, and entities.
     * <p>
     * <strong>Do not remove</strong>, as it will break the mod's registration system.
     * <p>
     * Do not use this, you need to add this line into your own mod.
     */
    public static AbstractRegistryBuilder REGISTRIES = new BlueRegistryBuilder(BlueLibConstants.MOD_ID);

    private BlueLibCommon() {}

    @ApiStatus.Internal
    public static void init() {
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                ModIntegration.checkSupportMods();
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(BlueLibCommon.Translation.translate("mod.thank_you"));
                BaseLogger.logBlueLib(BlueLibCommon.Translation.translate("mod.thank_you.subtitle"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    @ApiStatus.Internal
    public static void doRegistration() {
        var networkRegistry = new BlueNetworkRegistry();
        NetworkRegistry.registerC2SPacketProvider(networkRegistry);
        NetworkRegistry.registerS2CPacketProvider(networkRegistry);

        TestEntityReg.init();

        Path assetsPath = BlueLibConstants.PlatformHelper.PLATFORM.getAssetsDir(false);
        System.out.println("Assets path at:" + assetsPath);

        Path dataPath = BlueLibConstants.PlatformHelper.PLATFORM.getDataDir(false);
        System.out.println("Data path at:" + dataPath);
    }

    public static boolean isDeveloperMode() {
        boolean isDevMode = BlueLibConstants.PlatformHelper.PLATFORM.isDevelopmentEnvironment();
        if (isDevMode) {
            BaseLogger.log(true, BaseLogLevel.INFO, Component.literal("Running in Developer mode."));
        }
        return isDevMode;
    }

    @ApiStatus.Internal
    public static class Resource {

        public static ResourceLocation resource(String pPath) {
            return ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, pPath);
        }
    }

    @ApiStatus.Internal
    public static class Translation {

        public static Component translate(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + "." + pString);
        }

        public static Component translate(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + "." + pString, pArgs);
        }

        public static Component log(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString);
        }

        public static Component log(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".log." + pString, pArgs);
        }

        public static Component config(String pString) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString);
        }

        public static Component config(String pString, Object... pArgs) {
            return Component.translatable(BlueLibConstants.MOD_ID + ".config." + pString, pArgs);
        }
    }
}
