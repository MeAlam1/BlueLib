// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.utils.conversion;

import java.awt.*;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class ColorConverterUtils {

    public static int parseColorToHexString(@NotNull String pInput) {
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
                int r = Integer.parseInt(matcher.group(1));
                int g = Integer.parseInt(matcher.group(2));
                int b = Integer.parseInt(matcher.group(3));
                if (isValidRGB(r, g, b)) {
                    return toHex(r, g, b);
                }
            }
        }

        if (pInput.matches(argbPattern)) {
            Matcher matcher = Pattern.compile(argbPattern).matcher(pInput);
            if (matcher.matches()) {
                int r = Integer.parseInt(matcher.group(2));
                int g = Integer.parseInt(matcher.group(3));
                int b = Integer.parseInt(matcher.group(4));
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

    private static boolean isValidRGB(int pRed, int pGreen, int pBlue) {
        return isInRange(pRed) && isInRange(pGreen) && isInRange(pBlue);
    }

    private static boolean isInRange(int pValue) {
        return pValue >= 0 && pValue <= 255;
    }

    private static int toHex(int pRed, int pGreen, int pBlue) {
        return (pRed << 16) | (pGreen << 8) | pBlue;
    }

    @NotNull
    public static Optional<Color> getParsedColor(@NotNull String pColor) {
        try {
            if (pColor.contains("#"))
                return Optional.of(Color.decode(pColor));
            return Optional.of(new Color(DyeColor.valueOf(pColor.toUpperCase(Locale.ROOT)).getTextColor()));
        } catch (IllegalArgumentException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("color.notvalid", pColor), true);
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
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("color.notvalid", pColor), true);
            return Optional.empty();
        }
        return Optional.empty();
    }

    public static int rgbToDecimal(int pRed, int pGreen, int pBlue) {
        return (pRed << 16) + (pGreen << 8) + pBlue;
    }
}
