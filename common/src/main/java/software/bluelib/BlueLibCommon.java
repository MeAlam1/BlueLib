package software.bluelib;


import software.bluelib.interfaces.platform.IPlatformHelper;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import static software.bluelib.BlueLibConstants.SCHEDULER;

public class BlueLibCommon {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {

        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
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

    /**
     * A {@code static} {@link Boolean} that checks if the mod is running in developer mode.
     * <p>
     * Developer mode is active when the mod is not running in a production environment.
     * </p>
     *
     * @return {@code true} if running in developer mode, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    static boolean isDeveloperMode() {
        boolean isDevMode = PLATFORM.isDevelopmentEnvironment();
        if (isDevMode) {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Developer mode.", true);
        } else {
            BaseLogger.log(BaseLogLevel.INFO, "Running in Production mode.", true);
        }
        return isDevMode;
    }

}