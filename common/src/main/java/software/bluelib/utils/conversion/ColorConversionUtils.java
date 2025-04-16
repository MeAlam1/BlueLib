// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ColorConversionUtils {

    public static int parseColorToHexString(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            return 0xFFFFFF;
        }

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
}
