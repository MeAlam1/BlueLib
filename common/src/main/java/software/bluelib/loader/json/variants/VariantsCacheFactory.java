/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.variants;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.variants.Entity;

import java.util.Map;

public interface VariantsCacheFactory extends CacheFactory<Entity, Entity> {

	Map<String, VariantsCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	VariantsCacheFactory DEFAULT_FACTORY = new Builtin();

	CacheFactory.Registry<Entity, Entity, VariantsCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

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
	default Entity construct(Entity pSource) {
		return constructVariants(pSource);
	}

	Entity constructVariants(Entity pVariants);

	final class Builtin implements VariantsCacheFactory {

		@Override
		public Entity constructVariants(Entity pVariants) {
			return pVariants;
		}
	}
}
