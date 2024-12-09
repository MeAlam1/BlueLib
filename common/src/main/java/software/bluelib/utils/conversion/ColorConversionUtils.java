// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorConversionUtils {

    /**
     * Converts various color formats into 0xFFFFFF.
     *
     * @param pInput the color string to parse
     * @return the color in 0xFFFFFF format, or -1 if invalid
     */
    public static int parseColorToHexString(String pInput) {
        if (pInput == null || pInput.isEmpty()) {
            return 0xFFFFFF;
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

    /**
     * Validates RGB components are in the range 0-255.
     *
     * @param r red component
     * @param g green component
     * @param b blue component
     * @return true if valid, false otherwise
     */
    private static boolean isValidRGB(int r, int g, int b) {
        return isInRange(r) && isInRange(g) && isInRange(b);
    }

    /**
     * Checks if a value is in the range 0-255.
     *
     * @param value the value to check
     * @return true if in range, false otherwise
     */
    private static boolean isInRange(int value) {
        return value >= 0 && value <= 255;
    }

    public static boolean isValidColor(String pInput) {
        if (pInput == null) {
            return false;
        }

        String rgbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
        String argbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
        String hexPattern = "^#([0-9A-Fa-f]{6})$";
        String hex0xPattern = "^0x([0-9A-Fa-f]{6})$";

        return pInput.matches(rgbPattern) || pInput.matches(argbPattern) || pInput.matches(hexPattern) || pInput.matches(hex0xPattern);
    }

    /**
     * Converts RGB values to a 0xFFFFFF format.
     *
     * @param red   the red component (0-255)
     * @param green the green component (0-255)
     * @param blue  the blue component (0-255)
     * @return the color in 0xFFFFFF format
     */
    private static int toHex(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }
}
