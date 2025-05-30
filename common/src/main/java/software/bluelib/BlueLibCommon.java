/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.util.concurrent.TimeUnit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.registry.BlueEntityRegistry;
import software.bluelib.registry.BlueNetworkRegistry;

public class BlueLibCommon {

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
    public static BlueNetworkRegistry getRegistry() {
        return new BlueNetworkRegistry();
    }

    @ApiStatus.Internal
    public static void doServerRegistration() {
        BlueEntityRegistry.init();
        NetworkRegistry.registerC2SPacketProvider(getRegistry());
    }

    @ApiStatus.Internal
    public static void doClientRegistration() {
        NetworkRegistry.registerS2CPacketProvider(getRegistry());
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
