/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.conversion;

import java.awt.*;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

@SuppressWarnings("unused")
public class ColorConverterUtils {

    @NotNull
    public static Integer parseColorToHexString(@NotNull String pInput) {
        if (pInput.matches("^([0-9A-Fa-f]{6})$")) {
            pInput = "#" + pInput;
        }

        String rgbPattern = "\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)";
        String argbPattern = "\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)";
        String hexPattern = "^#([0-9A-Fa-f]{6})$";
        String hex0xPattern = "^0x([0-9A-Fa-f]{6})$";

        if (pInput.matches(rgbPattern)) {
            Matcher matcher = Pattern.compile(rgbPattern).matcher(pInput);
            if (matcher.matches()) {
                @NotNull
                Integer r = Integer.parseInt(matcher.group(1));
                @NotNull
                Integer g = Integer.parseInt(matcher.group(2));
                @NotNull
                Integer b = Integer.parseInt(matcher.group(3));
                if (isValidRGB(r, g, b)) {
                    return toHex(r, g, b);
                }
            }
        }

        if (pInput.matches(argbPattern)) {
            Matcher matcher = Pattern.compile(argbPattern).matcher(pInput);
            if (matcher.matches()) {
                @NotNull
                Integer r = Integer.parseInt(matcher.group(2));
                @NotNull
                Integer g = Integer.parseInt(matcher.group(3));
                @NotNull
                Integer b = Integer.parseInt(matcher.group(4));
                if (isValidRGB(r, g, b)) {
                    return toHex(r, g, b);
                }
            }
        }

        if (pInput.matches(hexPattern)) {
            return Integer.parseInt(pInput.substring(1), 16);
        }

        if (pInput.matches(hex0xPattern)) {
            return Integer.parseInt(pInput.substring(2), 16);
        }

        return 0xFFFFFF;
    }

    @NotNull
    private static Boolean isValidRGB(@NotNull Integer pRed, @NotNull Integer pGreen, @NotNull Integer pBlue) {
        return isInRange(pRed) && isInRange(pGreen) && isInRange(pBlue);
    }

    private static boolean isInRange(@NotNull Integer pValue) {
        return pValue >= 0 && pValue <= 255;
    }

    private static @NotNull Integer toHex(@NotNull Integer pRed, @NotNull Integer pGreen, @NotNull Integer pBlue) {
        return (pRed << 16) | (pGreen << 8) | pBlue;
    }

    @NotNull
    public static Optional<Color> getParsedColor(@NotNull String pColor) {
        try {
            if (pColor.contains("#"))
                return Optional.of(Color.decode(pColor));
            return Optional.of(new Color(DyeColor.valueOf(pColor.toUpperCase(Locale.ROOT)).getTextColor()));
        } catch (IllegalArgumentException pException) {
            BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("color.notvalid", pColor));
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<String> getParsedColorName(@NotNull String pColor) {
        try {
            if (!pColor.contains("#")) {
                return Optional.of(DyeColor.valueOf(pColor.toUpperCase(Locale.ROOT)).getName());
            }
        } catch (IllegalArgumentException pException) {
            BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.log("color.notvalid", pColor));
            return Optional.empty();
        }
        return Optional.empty();
    }

    public static @NotNull Integer rgbToDecimal(@NotNull Integer pRed, @NotNull Integer pGreen, @NotNull Integer pBlue) {
        return (pRed << 16) + (pGreen << 8) + pBlue;
    }
}
