/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangUtils;
import software.bluelib.api.utils.loader.LoaderUtils;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.controller.*;

public class ControllerManager<T extends BlueAnimatable> {

	@SuppressWarnings("unchecked")
	public void registerControllers(@NotNull BlueAnimatable pAnimatable, @NotNull ControllerCache pCache, @NotNull AnimatableManager.ControllerRegistrar<T> pControllers) {
		List<GroupCache> groups = pCache.groups();
		for (GroupCache group : groups) {
			Map<String, BehaviourCache> behaviours = group.behaviours();
			if (behaviours.isEmpty()) continue;

			pControllers.add(new AnimationController<>((T) pAnimatable, "main", 5, k -> {
				List<Map.Entry<String, BehaviourCache>> validBehaviours = new ArrayList<>();
				int maxPriority = Integer.MIN_VALUE;
				for (Map.Entry<String, BehaviourCache> entry : behaviours.entrySet()) {
					BehaviourCache behaviour = entry.getValue();
					boolean hasNonOverlayState = behaviour.states().values().stream().anyMatch(state -> !state.isOverlay());
					if (!hasNonOverlayState) continue;
					int priority = getEffectiveBehaviourPriority(behaviour, pAnimatable);
					if (priority > maxPriority) {
						validBehaviours.clear();
						maxPriority = priority;
					}
					if (priority == maxPriority && priority != Integer.MIN_VALUE) {
						validBehaviours.add(entry);
					}
				}
				for (Map.Entry<String, BehaviourCache> entry : validBehaviours) {
					return ControllerManager.animationController(k, entry.getValue(), pAnimatable, false);
				}
				return PlayState.PLAY;
			}));

			for (Map.Entry<String, BehaviourCache> entry : behaviours.entrySet()) {
				BehaviourCache behaviour = entry.getValue();
				for (Map.Entry<String, StateCache> stateEntry : behaviour.states().entrySet()) {
					StateCache state = stateEntry.getValue();
					if (state.isOverlay()) {
						pControllers.add(new AnimationController<>((T) pAnimatable, "overlay" + "_" + entry.getKey() + "_" + stateEntry.getKey(), 5, k -> {
							int priority = getEffectiveBehaviourPriority(behaviour, pAnimatable);
							if (priority == Integer.MIN_VALUE) return PlayState.PLAY;
							return ControllerManager.animationController(k, behaviour, pAnimatable, true);
						}));
					}
				}
			}
		}
	}

	@NotNull
	protected static <E extends BlueAnimatable> PlayState animationController(@NotNull final AnimationState<E> pEvent, @NotNull BehaviourCache pBehaviour, @NotNull BlueAnimatable pAnimatable, boolean pOverlayOnly) {
		for (Map.Entry<String, StateCache> entry : pBehaviour.states().entrySet()) {
			StateCache state = entry.getValue();
			if (pOverlayOnly && !state.isOverlay()) continue;
			if (!pOverlayOnly && state.isOverlay()) continue;
			List<AnimationCache> animations = state.animations();
			if (animations.isEmpty()) continue;

			List<AnimationCache> sortedAnimations = new ArrayList<>(animations);
			sortedAnimations.sort((pA, pB) -> {
				int a = getEffectivePriority(pA, pAnimatable);
				int b = getEffectivePriority(pB, pAnimatable);
				return Integer.compare(b, a);
			});
			AnimationCache selected = sortedAnimations.getFirst();

			int selectedPriority = getEffectivePriority(selected, pAnimatable);
			if (selectedPriority == Integer.MIN_VALUE) {
				return PlayState.STOP;
			}

			return pEvent.setAndContinue(Animation.begin().thenLoop(selected.animation()));
		}
		return PlayState.PLAY;
	}

	private static int getEffectivePriority(
			@Nullable Integer pPriority,
			@NotNull List<String> pConditions,
			@NotNull BlueAnimatable pAnimatable) {
		int effectivePriority = pPriority == null ? Integer.MIN_VALUE : pPriority;
		if (pAnimatable instanceof Entity entity) {
			for (String condition : pConditions) {
				Object loadedResult = MoLang.evaluate(condition);
				Object entityResult = MoLangUtils.entity(condition, entity);
				if ((loadedResult instanceof Boolean && (Boolean) loadedResult) ||
						(entityResult instanceof Boolean && (Boolean) entityResult)) {
					return effectivePriority;
				}
			}
			return Integer.MIN_VALUE;
		}
		return effectivePriority;
	}

	private static int getEffectivePriority(@NotNull AnimationCache pState, @NotNull BlueAnimatable pAnimatable) {
		return getEffectivePriority(pState.priority(), pState.conditions(), pAnimatable);
	}

	private static int getEffectiveBehaviourPriority(@NotNull BehaviourCache pBehaviour, @NotNull BlueAnimatable pAnimatable) {
		return getEffectivePriority(pBehaviour.priority(), pBehaviour.conditions(), pAnimatable);
	}

	@NotNull
	public static ControllerCache getBakedController(@NotNull ResourceLocation pLocation) {
		ResourceLocation[] attempts = new ResourceLocation[] {
				pLocation,
				LoaderUtils.stripSuffix(".json", pLocation),
				LoaderUtils.stripSuffix(".controller.json", pLocation)
		};

		final Map<ResourceLocation, ControllerCache> controllerMap = ResourceCache.Client.getControllers();

		for (ResourceLocation loc : attempts) {
			ControllerCache controller = controllerMap.get(loc);
			if (controller != null) {
				return controller;
			}
		}

		if (!pLocation.getPath().contains("controller/"))
			throw new RuntimeException("Invalid controller resource path provided - BlueLib controllers must be placed in data/<modid>/controllers/");

		throw new RuntimeException("Unable to find controller file: " + pLocation);
	}
}
