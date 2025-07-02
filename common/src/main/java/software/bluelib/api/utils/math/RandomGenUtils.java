/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.math;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class RandomGenUtils {

    private RandomGenUtils() {}

    public static @NotNull Integer generateRandomInt(@NotNull Integer pMin, @NotNull Integer pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.gen", "random integer"), throwable);
            return 0;
        }
        return pMin + (int) (Math.random() * (pMax - pMin + 1));
    }

    public static @NotNull Double generateRandomDouble(@NotNull Double pMin, @NotNull Double pMax) {
        if (pMin > pMax) {
            Throwable throwable = new IllegalArgumentException("Minimum value must not be greater than maximum value.");
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.gen", "random @NotNull Double"), throwable);
            return 0.0;
        }
        return pMin + Math.random() * (pMax - pMin);
    }

    @NotNull
    public static Boolean generateRandomBoolean() {
        return Math.random() < 0.5;
    }

    @NotNull
    public static String generateRandomString(@NotNull Integer pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.gen", "random string"), throwable);
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

    @NotNull
    public static String generateRandomStringWithPrefix(@NotNull String pPrefix, @NotNull Integer pLength) {
        if (pLength < 0) {
            Throwable throwable = new IllegalArgumentException("Length must be non-negative.");
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("math.error.gen", "random string with prefix"), throwable);
            return "unknown";
        }
        return pPrefix + generateRandomString(pLength - pPrefix.length());
    }
}
