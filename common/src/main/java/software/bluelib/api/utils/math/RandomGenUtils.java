// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.math;

import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class RandomGenUtils {

    private RandomGenUtils() {}

    public static int generateRandomInt(int pMin, int pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.error.gen", "random integer"), throwable, true);
            return 0;
        }
        return pMin + (int) (Math.random() * (pMax - pMin + 1));
    }

    public static double generateRandomDouble(double pMin, double pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.error.gen", "random double"), throwable, true);
            return 0;
        }
        return pMin + Math.random() * (pMax - pMin);
    }

    public static boolean generateRandomBoolean() {
        return Math.random() < 0.5;
    }

    public static String generateRandomString(int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.error.gen", "random string"), throwable, true);
            return "unknown";
        }
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(pLength);
        for (int i = 0; i < pLength; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomStringWithPrefix(String pPrefix, int pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(BaseLogLevel.WARNING, BlueLibCommon.Translation.log("math.error.gen", "random string with prefix"), throwable, true);
            return "unknown";
        }
        return pPrefix + generateRandomString(pLength - pPrefix.length());
    }
}
