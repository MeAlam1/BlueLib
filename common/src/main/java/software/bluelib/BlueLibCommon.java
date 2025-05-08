// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

import java.util.concurrent.TimeUnit;

import static software.bluelib.BlueLibConstants.SCHEDULER;

public class BlueLibCommon {

    private BlueLibCommon() {
    }

    public static void init() {
        ModIntegration.checkSupportMods();
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Component.translatable("bluelib.mod.thank_you"));
                BaseLogger.logBlueLib(Component.translatable("bluelib.mod.thank_you.subtitle"));
                BaseLogger.logBlueLib(Component.literal("                                                  "));
                BaseLogger.logBlueLib(Component.literal("**************************************************"));
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    public static void doRegistration() {
        var networkRegistry = new software.bluelib.registry.NetworkRegistry();
        NetworkRegistry.registerC2SPacketProvider(networkRegistry);
        NetworkRegistry.registerS2CPacketProvider(networkRegistry);
    }

    public static boolean isDeveloperMode() {
        boolean isDevMode = BlueLibConstants.PlatformHelper.PLATFORM.isDevelopmentEnvironment();
        if (isDevMode) {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Developer mode.", true);
        } else {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Production mode.", true);
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
    }
}
