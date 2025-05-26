package software.bluelib.loader;

import software.bluelib.loader.service.GeckoLibClient;
import software.bluelib.loader.service.GeckoLibEvents;
import software.bluelib.loader.service.GeckoLibNetworking;
import software.bluelib.loader.service.GeckoLibPlatform;

import java.util.ServiceLoader;

public final class GeckoLibServices {
    public static final GeckoLibPlatform PLATFORM = load(GeckoLibPlatform.class);
    public static final GeckoLibNetworking NETWORK = load(GeckoLibNetworking.class);

    public static class Client {
        public static final GeckoLibEvents EVENTS = load(GeckoLibEvents.class);
        public static final GeckoLibClient ITEM_RENDERING = load(GeckoLibClient.class);
    }

    private static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));

        return loadedService;
    }
}
