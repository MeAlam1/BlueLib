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
import software.bluelib.loader.cache.controller.BehaviourCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.controller.GroupCache;
import software.bluelib.loader.cache.controller.StateCache;
import software.bluelib.loader.json.CacheFactory;
import software.bluelib.loader.json.deserialize.controller.Behaviour;
import software.bluelib.loader.json.deserialize.controller.Controller;
import software.bluelib.loader.json.deserialize.controller.Group;
import software.bluelib.loader.json.deserialize.controller.State;

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
			List<GroupCache> groupCaches = constructGroupCaches(pController.groups());
			return new ControllerCache(pController.formatVersion(), groupCaches);
		}

		@NotNull
		private List<GroupCache> constructGroupCaches(@NotNull List<Group> pGroups) {
			return pGroups.stream()
					.map(this::constructGroupCache)
					.toList();
		}

		@NotNull
		private GroupCache constructGroupCache(@NotNull Group pGroup) {
			Map<String, BehaviourCache> behaviourCaches = constructBehaviourCaches(pGroup.behaviours());
			return new GroupCache(behaviourCaches);
		}

		@NotNull
		private Map<String, BehaviourCache> constructBehaviourCaches(@NotNull Map<String, Behaviour> pBehaviours) {
			Map<String, BehaviourCache> behaviourCaches = new Object2ObjectOpenHashMap<>(pBehaviours.size());

			for (Map.Entry<String, Behaviour> entry : pBehaviours.entrySet()) {
				String name = entry.getKey();
				Behaviour behaviour = entry.getValue();
				Map<String, List<StateCache>> stateCache = constructStateCaches(behaviour.states());
				BehaviourCache behaviourCache = new BehaviourCache(stateCache);
				behaviourCaches.put(name, behaviourCache);
			}

			return behaviourCaches;
		}

		@NotNull
		private Map<String, List<StateCache>> constructStateCaches(@NotNull Map<String, List<State>> pStates) {
			Map<String, List<StateCache>> stateCaches = new Object2ObjectOpenHashMap<>(pStates.size());

			for (Map.Entry<String, List<State>> entry : pStates.entrySet()) {
				String name = entry.getKey();
				List<State> states = entry.getValue();
				List<StateCache> stateCacheList = states.stream()
						.map(this::constructStateCache)
						.toList();
				stateCaches.put(name, stateCacheList);
			}

			return stateCaches;
		}

		@NotNull
		private StateCache constructStateCache(@NotNull State pState) {
			return new StateCache(
					pState.conditions(),
					pState.animation(),
					pState.priority(),
					pState.sound());
		}
	}
}
