package software.bluelib.utils.conversion;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.net.URI;
import java.net.URISyntaxException;

public class LinkUtils {

    public static URI stringToUri(String urlString) {
        try {
            return new URI(urlString);
        } catch (URISyntaxException pUriSyntaxException) {
            BaseLogger.log(BaseLogLevel.ERROR, pUriSyntaxException.getMessage());
            return URI.create("https://www.google.com");
        }
    }
}
