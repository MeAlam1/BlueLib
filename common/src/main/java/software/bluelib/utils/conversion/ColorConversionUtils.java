// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.conversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for color conversion operations.
 * <p>
 * Purpose: This class provides methods to convert and validate various color formats.<br>
 * When: Used when color values need to be parsed or validated.<br>
 * Where: Invoked in contexts where color manipulation is required.<br>
 * Additional Info: Supports RGB, ARGB, and hexadecimal color formats.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #parseColorToHexString(String)} - Converts various color formats into 0xFFFFFF.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @since 1.6.0
 */
public class ColorConversionUtils {

    /**
     * Converts various color formats into 0xFFFFFF.
     * <p>
     * Purpose: This method parses a color string and converts it to a hexadecimal format.<br>
     * When: Called when a color string needs to be converted to a hexadecimal value.<br>
     * Where: Used in contexts where color values are processed.<br>
     * Additional Info: Supports RGB, ARGB, and hexadecimal formats.
     * </p>
     *
     * @param pInput the color string to parse
     * @return the color in 0xFFFFFF format, or -1 if invalid
     * @author MeAlam
     * @since 1.6.0
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
     * <p>
     * Purpose: This method checks if the RGB components are within the valid range.<br>
     * When: Called to validate RGB values.<br>
     * Where: Used internally within the class.<br>
     * Additional Info: Ensures that each component is between 0 and 255.
     * </p>
     *
     * @param pRed   red component
     * @param pGreen green component
     * @param pBlue  blue component
     * @return true if valid, false otherwise
     * @author MeAlam
     * @since 1.6.0
     */
    private static boolean isValidRGB(int pRed, int pGreen, int pBlue) {
        return isInRange(pRed) && isInRange(pGreen) && isInRange(pBlue);
    }

    /**
     * Checks if a value is in the range 0-255.
     * <p>
     * Purpose: This method checks if a value is within the valid range for color components.<br>
     * When: Called to validate individual color components.<br>
     * Where: Used internally within the class.<br>
     * Additional Info: Ensures that the value is between 0 and 255.
     * </p>
     *
     * @param pValue the value to check
     * @return true if in range, false otherwise
     * @author MeAlam
     * @since 1.6.0
     */
    private static boolean isInRange(int pValue) {
        return pValue >= 0 && pValue <= 255;
    }

    /**
     * Converts RGB values to a 0xFFFFFF format.
     * <p>
     * Purpose: This method converts RGB values to a hexadecimal format.<br>
     * When: Called to convert RGB values to a single integer.<br>
     * Where: Used internally within the class.<br>
     * Additional Info: Combines the red, green, and blue components into a single integer.
     * </p>
     *
     * @param pRed   the red component (0-255)
     * @param pGreen the green component (0-255)
     * @param pBlue  the blue component (0-255)
     * @return the color in 0xFFFFFF format
     * @author MeAlam
     * @since 1.6.0
     */
    private static int toHex(int pRed, int pGreen, int pBlue) {
        return (pRed << 16) | (pGreen << 8) | pBlue;
    }
}
