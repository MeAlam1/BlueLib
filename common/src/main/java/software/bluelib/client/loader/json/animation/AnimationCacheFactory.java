/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.client.loader.json.CacheFactory;
import software.bluelib.client.loader.json.deserialize.animation.AnimationLibrary;

public interface AnimationCacheFactory extends CacheFactory<AnimationLibraryCache, AnimationLibrary> {

    Map<String, AnimationCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    AnimationCacheFactory DEFAULT_FACTORY = new Builtin();

    static AnimationCacheFactory getForNamespace(String pNamespace) {
        return CacheFactory.getForNamespace(FACTORIES, DEFAULT_FACTORY, pNamespace);
    }

    static void register(String pNamespace, AnimationCacheFactory pFactory) {
        CacheFactory.register(FACTORIES, pNamespace, pFactory);
    }

    @Override
    default AnimationLibraryCache construct(AnimationLibrary pSource) {
        return constructBlueAnimator(pSource);
    }

    AnimationLibraryCache constructBlueAnimator(AnimationLibrary pLibrary);

    final class Builtin implements AnimationCacheFactory {

        @Override
        public AnimationLibraryCache constructBlueAnimator(AnimationLibrary pLibrary) {
            return pLibrary.animations();
        }
    }
}
