/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader;

import java.util.ServiceLoader;
import software.bluelib.loader.service.GeckoLibClient;
import software.bluelib.loader.service.GeckoLibEvents;
import software.bluelib.loader.service.GeckoLibNetworking;
import software.bluelib.loader.service.GeckoLibPlatform;

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
