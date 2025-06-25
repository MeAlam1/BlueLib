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
import software.bluelib.client.loader.json.CacheFactory;
import software.bluelib.client.loader.json.deserialize.controller.Controller;

public interface ControllerCacheFactory extends CacheFactory<ControllerCache, Controller> {

    Map<String, ControllerCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    ControllerCacheFactory DEFAULT_FACTORY = new ControllerCacheFactory.Builtin();

    CacheFactory.Registry<ControllerCache, Controller, ControllerCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

        @Override
        public Map<String, ControllerCacheFactory> factories() {
            return FACTORIES;
        }

        @Override
        public ControllerCacheFactory defaultFactory() {
            return DEFAULT_FACTORY;
        }
    };

    @Override
    default ControllerCache construct(Controller pSource) {
        return constructBlueController(pSource);
    }

    ControllerCache constructBlueController(Controller pController);

    final class Builtin implements ControllerCacheFactory {

        @Override
        public ControllerCache constructBlueController(Controller pController) {
            return null;
        }
    }
}
