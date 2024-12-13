// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import java.net.URI;
import java.util.regex.Pattern;

/**
 * A {@code class} providing utility methods for various common operations.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #isValidURL(String)} - Checks if a string is a valid URL.</li>
 * <li>{@link #isValidEmail(String)} - Checks if a string is a valid email address.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @since 1.0.0
 */
public class IsValidUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private IsValidUtils() {}

    /**
     * A {@code public static} {@link Boolean} to check if a string is a valid URL.
     * <p>
     * This method checks if a string is a well-formed URL. It supports both `http` and `https` schemes
     * and automatically adds `https://` if the URL starts with "www.".
     * </p>
     *
     * <p>
     * Example Input:
     * <ul>
     * <li>"www.example.com" - Valid URL, will be prefixed with "https://".</li>
     * <li>"<a href="https://modrinth.com/mod/bluelib">https://modrinth.com/mod/bluelib</a>" - Valid URL.</li>
     * <li>"invalid-url" - Not a valid URL.</li>
     * </ul>
     *
     * @param pUrl {@link String} - The URL string to validate.
     * @return {@code true} if the string is a valid URL, {@code false} otherwise.
     * @author MeAlam
     * @since 1.4.0
     */
    public static boolean isValidURL(String pUrl) {
        try {
            if (!pUrl.startsWith("http://") && !pUrl.startsWith("https://")) {
                return false;
            }

            URI uri = new URI(pUrl);

            String domainRegex = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            Pattern pattern = Pattern.compile(domainRegex);
            String host = uri.getHost();

            return uri.isAbsolute() && (pattern.matcher(host).matches());
        } catch (Exception pException) {
            return false;
        }
    }

    /**
     * A {@link Boolean} that checks if a string is a valid email address.
     *
     * @param pEmail {@link String} - The string to be checked.
     * @return {@code true} if the string is a valid email address, {@code false} otherwise.
     * @author MeAlam
     * @since 1.0.0
     */
    public static boolean isValidEmail(String pEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return pEmail != null && pEmail.matches(emailRegex);
    }

    /**
     * Validates if a color string is in a recognized format.
     * <p>
     * Purpose: This method checks if a color string matches any of the supported formats.<br>
     * When: Called to validate color strings.<br>
     * Where: Used in contexts where color validation is required.<br>
     * Additional Info: Supports RGB, ARGB, and hexadecimal formats.
     * </p>
     *
     * @param pInput the color string to validate
     * @return true if valid, false otherwise
     * @author MeAlam
     * @since 1.6.0
     */
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
}
