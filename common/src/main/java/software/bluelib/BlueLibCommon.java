// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.event.IEventProxy;
import software.bluelib.api.event.mod.ModIntegration;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.platform.IPlatformHelper;

public class BlueLibCommon {

    private BlueLibCommon() {}

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final IEventProxy EVENT_PROXY = ServiceLoader.load(IEventProxy.class).findFirst().orElseThrow();

    public static <T> T load(Class<T> pClazz) {
        return ServiceLoader.load(pClazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
    }

    public static void init() {
        ModIntegration.checkSupportMods();
        if (isDeveloperMode()) {
            SCHEDULER.schedule(() -> {
                BaseLogger.logBlueLib("**************************************************");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("     Thank you for using BlueLib!                 ");
                BaseLogger.logBlueLib("     We appreciate your support.                  ");
                BaseLogger.logBlueLib("                                                  ");
                BaseLogger.logBlueLib("**************************************************");
                SCHEDULER.shutdown();
            }, 5, TimeUnit.SECONDS);
        }
    }

    public static boolean isDeveloperMode() {
        boolean isDevMode = PLATFORM.isDevelopmentEnvironment();
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
