// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} providing common methods for various operations.
 * <p>
 * This class includes methods for validating URLs and other reusable utility functions.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #isValidURL(String)} - Checks if a string is a valid URL.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @since 1.4.0
 */
public class MiscUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.4.0
     */
    private MiscUtils() {
    }

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
     *     <li>"www.example.com" - Valid URL, will be prefixed with "https://".</li>
     *     <li>"<a href="https://modrinth.com/mod/bluelib">https://modrinth.com/mod/bluelib</a>" - Valid URL.</li>
     *     <li>"invalid-url" - Not a valid URL.</li>
     * </ul>
     *
     * @param pUrl {@link String} - The URL string to validate.
     * @return {@code true} if the string is a valid URL, {@code false} otherwise.
     * @author MeAlam
     * @since 1.4.0
     */
    public static boolean isValidURL(String pUrl) {
        try {
            if (pUrl.startsWith("www.")) {
                pUrl = "https://" + pUrl;
            }
            java.net.URI uri = new java.net.URI(pUrl);

            return uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Invalid URL: " + pUrl, pException, true);
            return false;
        }
    }
}
