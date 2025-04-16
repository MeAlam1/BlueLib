// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils;

import java.net.URI;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class IsValidUtils {

    private IsValidUtils() {}

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

    public static boolean isValidEmail(String pEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return pEmail != null && pEmail.matches(emailRegex);
    }

    public static boolean isValidColor(String pInput) {
        if (pInput == null) {
            return false;
        }

        String rgbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
        String argbPattern = "\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)";
        String hexPattern = "^#([0-9A-Fa-f]{6})$";
        String hex0xPattern = "^0x([0-9A-Fa-f]{6})$";
        String plainHexPattern = "^([0-9A-Fa-f]{6})$";

        return pInput.matches(rgbPattern) ||
                pInput.matches(argbPattern) ||
                pInput.matches(hexPattern) ||
                pInput.matches(hex0xPattern) ||
                pInput.matches(plainHexPattern);
    }
}
