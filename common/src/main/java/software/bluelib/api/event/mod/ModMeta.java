package software.bluelib.api.event.mod;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

public record ModMeta(
        String modId,
        String displayName,
        String version,
        String description,
        String namespace,
        Map<String, Object> properties,
        Optional<URL> updateURL,
        Optional<URL> modURL,
        Optional<String> logoFile

) {}
