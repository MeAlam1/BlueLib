// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib;

import static software.bluelib.BlueLibConstants.SCHEDULER;

import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import software.bluelib.interfaces.platform.IPlatformHelper;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class BlueLibCommon {

    private BlueLibCommon() {}

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> pClazz) {
        return ServiceLoader.load(pClazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + pClazz.getName()));
    }

    public static void init() {
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
}
