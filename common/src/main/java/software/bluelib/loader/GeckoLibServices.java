/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader;

import java.util.ServiceLoader;

import software.bluelib.loader.service.GeckoLibNetworking;

public final class GeckoLibServices {

    public static final GeckoLibNetworking NETWORK = load(GeckoLibNetworking.class);

    private static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));

        return loadedService;
    }
}
