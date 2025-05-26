package software.bluelib.loader;

import software.bluelib.loader.cache.GeckoLibCache;


public final class GeckoLibClient {
    public static void init() {
        GeckoLibCache.registerReloadListener();
    }
}
