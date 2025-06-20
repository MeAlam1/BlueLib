/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.client.loader.cache.controller.ControllerCache;

public interface ControllerCacheFactory {

    Map<String, ControllerCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    ControllerCacheFactory DEFAULT_FACTORY = new ControllerCacheFactory.Builtin();

    static ControllerCacheFactory getForNamespace(String pNamespace) {
        return FACTORIES.getOrDefault(pNamespace, DEFAULT_FACTORY);
    }

    static void register(String pNamespace, ControllerCacheFactory pFactory) {
        FACTORIES.put(pNamespace, pFactory);
    }

    ControllerCache constructBlueController();

    final class Builtin implements ControllerCacheFactory {

        @Override
        public ControllerCache constructBlueController() {
            return null;
        }
    }
}
