/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.controller.Controller;

public interface ControllerCacheFactory extends CacheFactory<ControllerCache, Controller> {

	@NotNull
	Map<String, ControllerCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	@NotNull
	ControllerCacheFactory DEFAULT_FACTORY = new Builtin();

	@NotNull
	CacheFactory.Registry<ControllerCache, Controller, ControllerCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

		@Override
		public @NotNull Map<String, ControllerCacheFactory> factories() {
			return FACTORIES;
		}

		@Override
		public @NotNull ControllerCacheFactory defaultFactory() {
			return DEFAULT_FACTORY;
		}
	};

	@Override
	default @NotNull ControllerCache construct(@NotNull Controller pSource) {
		return constructBlueController(pSource);
	}

	@NotNull
	ControllerCache constructBlueController(@NotNull Controller pController);

	final class Builtin implements ControllerCacheFactory {

		@Override
		public @NotNull ControllerCache constructBlueController(@NotNull Controller pController) {
			return null;
		}
	}
}
