package software.bluelib.utils;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class MiscUtils {

    public static boolean isValidURL(String pUrl) {
        try {
            if (pUrl.startsWith("www.")) {
                pUrl = "https://" + pUrl;
            }

            java.net.URI uri = new java.net.URI(pUrl);
            return uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
        } catch (Exception pException) {
            return false;
        }
    }

}
