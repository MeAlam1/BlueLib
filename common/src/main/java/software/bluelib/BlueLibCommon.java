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
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.launch.MixinBootstrap;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.BlueRegistryBuilder;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.registry.BlueNetworkRegistry;
import software.bluelib.registry.TestEntityReg;
import software.bluelib.internal.Translation;
import software.bluelib.internal.registry.BlueNetworkRegistry;
import software.bluelib.internal.registry.BlueRecipeSerializerRegistry;
import software.bluelib.internal.registry.BlueRecipeTypeRegistry;

@ApiStatus.Internal
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

    public static void init() {
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                ModIntegration.checkSupportMods();
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Translation.translate("mod.thank_you"));
                BaseLogger.logBlueLib(Translation.translate("mod.thank_you.subtitle"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    public static void doRegistration() {
        MixinBootstrap.init();
        InternalNetworkRegistry.networkServer();
        BlueRecipeTypeRegistry.init();
        BlueRecipeSerializerRegistry.init();
    }

    public static void doClientRegistration() {
        InternalNetworkRegistry.networkClient();
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

    protected static class InternalNetworkRegistry {

        private static BlueNetworkRegistry getNetwork() {
            return new BlueNetworkRegistry();
        }

        private static void networkServer() {
            NetworkRegistry.registerC2SPacketProvider(getNetwork());
        }

        private static void networkClient() {
            NetworkRegistry.registerS2CPacketProvider(getNetwork());
        }
    }
}
