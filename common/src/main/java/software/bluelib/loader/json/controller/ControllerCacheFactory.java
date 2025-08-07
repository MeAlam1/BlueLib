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
			return new GroupCache(
					behaviourCaches);
		}

		@NotNull
		private Map<String, BehaviourCache> constructBehaviourCaches(@NotNull Map<String, Behaviour> pBehaviours) {
			Map<String, BehaviourCache> behaviourCaches = new Object2ObjectOpenHashMap<>(pBehaviours.size());

			for (Map.Entry<String, Behaviour> entry : pBehaviours.entrySet()) {
				String name = entry.getKey();
				Behaviour behaviour = entry.getValue();
				Map<String, StateCache> stateCache = constructStateCaches(behaviour.states());
				BehaviourCache behaviourCache = new BehaviourCache(
						behaviour.conditions(),
						behaviour.priority(),
						stateCache);
				behaviourCaches.put(name, behaviourCache);
			}

			return behaviourCaches;
		}

		@NotNull
		private Map<String, StateCache> constructStateCaches(@NotNull Map<String, State> pStates) {
			Map<String, StateCache> stateCaches = new Object2ObjectOpenHashMap<>(pStates.size());

			for (Map.Entry<String, State> entry : pStates.entrySet()) {
				String name = entry.getKey();
				State state = entry.getValue();
				StateCache stateCache = constructStateCache(state);
				stateCaches.put(name, stateCache);
			}

			return stateCaches;
		}

		@NotNull
		private StateCache constructStateCache(@NotNull State pState) {
			List<AnimationCache> animationCaches = constructAnimationsCaches(pState.animations());
			return new StateCache(
					pState.isOverlay(),
					animationCaches);
		}

		@NotNull
		private List<AnimationCache> constructAnimationsCaches(@NotNull List<Animation> pAnimations) {
			return pAnimations.stream()
					.map(this::constructAnimationCache)
					.toList();
		}

		@NotNull
		private AnimationCache constructAnimationCache(@NotNull Animation pAnimation) {
			return new AnimationCache(
					pAnimation.conditions(),
					pAnimation.animation(),
					pAnimation.priority(),
					pAnimation.sound());
		}
	}
}
