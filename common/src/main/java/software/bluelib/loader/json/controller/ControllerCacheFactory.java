/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.controller;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.cache.controller.*;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.controller.*;

public interface ControllerCacheFactory extends CacheFactory<ControllerCache, ControllerDeserializer> {

	@NotNull
	Map<String, ControllerCacheFactory> FACTORIES = new Object2ObjectOpenHashMap<>(1);
	@NotNull
	ControllerCacheFactory DEFAULT_FACTORY = new Builtin();

	@NotNull
	CacheFactory.Registry<ControllerCache, ControllerDeserializer, ControllerCacheFactory> REGISTRY = new CacheFactory.Registry<>() {

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
	default @NotNull ControllerCache construct(@NotNull ControllerDeserializer pSource) {
		return constructBlueController(pSource);
	}

	@NotNull
	ControllerCache constructBlueController(@NotNull ControllerDeserializer pControllerDeserializer);

	final class Builtin implements ControllerCacheFactory {

		@Override
		public @NotNull ControllerCache constructBlueController(@NotNull ControllerDeserializer pControllerDeserializer) {
			return new ControllerCache(pControllerDeserializer.formatVersion(), constructGroupCaches(pControllerDeserializer.groupDeserializers()));
		}

		@NotNull
		private List<GroupCache> constructGroupCaches(@NotNull List<GroupDeserializer> pGroupDeserializers) {
			return pGroupDeserializers.stream()
					.map(this::constructGroupCache)
					.toList();
		}

		@NotNull
		private GroupCache constructGroupCache(@NotNull GroupDeserializer pGroupDeserializer) {
			return new GroupCache(constructBehaviourCaches(pGroupDeserializer.behaviours()));
		}

		@NotNull
		private Map<String, BehaviourCache> constructBehaviourCaches(@NotNull Map<String, BehaviourDeserializer> pBehaviours) {
			Map<String, BehaviourCache> behaviourCaches = new Object2ObjectOpenHashMap<>(pBehaviours.size());

			for (Map.Entry<String, BehaviourDeserializer> entry : pBehaviours.entrySet()) {
				String name = entry.getKey();
				BehaviourDeserializer behaviourDeserializer = entry.getValue();
				Map<String, StateCache> stateCache = constructStateCaches(behaviourDeserializer.states());
				BehaviourCache behaviourCache = new BehaviourCache(
						behaviourDeserializer.conditions(),
						behaviourDeserializer.priority(),
						stateCache);
				behaviourCaches.put(name, behaviourCache);
			}

			return behaviourCaches;
		}

		@NotNull
		private Map<String, StateCache> constructStateCaches(@NotNull Map<String, StateDeserializer> pStates) {
			Map<String, StateCache> stateCaches = new Object2ObjectOpenHashMap<>(pStates.size());

			for (Map.Entry<String, StateDeserializer> entry : pStates.entrySet()) {
				String name = entry.getKey();
				StateDeserializer stateDeserializer = entry.getValue();
				StateCache stateCache = constructStateCache(stateDeserializer);
				stateCaches.put(name, stateCache);
			}

			return stateCaches;
		}

		@NotNull
		private StateCache constructStateCache(@NotNull StateDeserializer pStateDeserializer) {
			return new StateCache(
					pStateDeserializer.isOverlay(),
					constructAnimationsCaches(pStateDeserializer.controllerAnimationDeserializers()));
		}

		@NotNull
		private List<AnimationCache> constructAnimationsCaches(@NotNull List<ControllerAnimationDeserializer> pControllerAnimationDeserializers) {
			return pControllerAnimationDeserializers.stream()
					.map(this::constructAnimationCache)
					.toList();
		}

		@NotNull
		private AnimationCache constructAnimationCache(@NotNull ControllerAnimationDeserializer pControllerAnimationDeserializer) {
			return new AnimationCache(
					pControllerAnimationDeserializer.conditions(),
					pControllerAnimationDeserializer.animation(),
					pControllerAnimationDeserializer.priority(),
					pControllerAnimationDeserializer.sound());
		}
	}
}
