/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.variants;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import software.bluelib.loader.cache.variants.EntityCache;
import software.bluelib.loader.cache.variants.VariantCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.variants.Entity;
import software.bluelib.loader.json.deserialize.variants.Variant;

public interface VariantsCacheFactory extends CacheFactory<EntityCache, Entity> {

    Map<String, VariantsCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
    VariantsCacheFactory DEFAULT_FACTORY = new Builtin();

    CacheFactory.Registry<EntityCache, Entity, VariantsCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

        @Override
        public Map<String, VariantsCacheFactory> factories() {
            return FACTORIES;
        }

        @Override
        public VariantsCacheFactory defaultFactory() {
            return DEFAULT_FACTORY;
        }
    };

    @Override
    default EntityCache construct(Entity pSource) {
        return constructVariants(pSource);
    }

    EntityCache constructVariants(Entity pVariants);

    final class Builtin implements VariantsCacheFactory {

        @Override
        public EntityCache constructVariants(Entity pVariants) {
            Map<String, VariantCache> variantCaches = new Object2ObjectOpenHashMap<>();
            for (Map.Entry<String, Variant> entry : pVariants.variants().entrySet()) {
                variantCaches.put(entry.getKey(), constructVariantCache(entry.getValue()));
            }
            return new EntityCache(
                    pVariants.formatVersion(),
                    variantCaches);
        }

        private VariantCache constructVariantCache(Variant pVariant) {
            return new VariantCache(
                    pVariant.parameters());
        }
    }
}
