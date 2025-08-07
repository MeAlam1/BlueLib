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
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangUtils;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.controller.BehaviourCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.controller.GroupCache;
import software.bluelib.loader.cache.controller.StateCache;

public class ControllerManager<T extends BlueAnimatable> {

	@SuppressWarnings("unchecked")
	public void registerControllers(BlueAnimatable pAnimatable, @NotNull ControllerCache pCache, @NotNull AnimatableManager.ControllerRegistrar<T> pControllers) {
		List<GroupCache> groups = pCache.groups();
		for (GroupCache group : groups) {
			Map<String, BehaviourCache> behaviours = group.behaviours();
			if (behaviours.isEmpty()) continue;

			pControllers.add(new AnimationController<>((T) pAnimatable, "main", 5, k -> {
				List<Map.Entry<String, BehaviourCache>> validBehaviours = new ArrayList<>();
				int maxPriority = Integer.MIN_VALUE;
				for (Map.Entry<String, BehaviourCache> entry : behaviours.entrySet()) {
					BehaviourCache behaviour = entry.getValue();
					if (isOverlay(behaviour)) continue;
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
					return ControllerManager.animationController(k, entry.getValue(), pAnimatable);
				}
				return PlayState.PLAY;
			}));

			for (Map.Entry<String, BehaviourCache> entry : behaviours.entrySet()) {
				BehaviourCache behaviour = entry.getValue();
				if (isOverlay(behaviour)) {
					pControllers.add(new AnimationController<>((T) pAnimatable, "overlay" + "_" + entry.getKey(), 5, k -> {
						int priority = getEffectiveBehaviourPriority(behaviour, pAnimatable);
						if (priority == Integer.MIN_VALUE) return PlayState.PLAY;
						return ControllerManager.animationController(k, behaviour, pAnimatable);
					}));
				}
			}
		}
	}

	private static boolean isOverlay(BehaviourCache pBehaviour) {
		return false;
	}

	protected static <E extends BlueAnimatable> PlayState animationController(final AnimationState<E> pEvent, BehaviourCache pBehaviour, BlueAnimatable pAnimatable) {
		for (Map.Entry<String, List<StateCache>> entry : pBehaviour.states().entrySet()) {
			List<StateCache> states = entry.getValue();
			if (states.isEmpty()) {
				continue;
			}
			List<StateCache> mutableStates = new ArrayList<>(states);
			mutableStates.sort((a, b) -> {
				int pa = getEffectivePriority(a, pAnimatable);
				int pb = getEffectivePriority(b, pAnimatable);
				return Integer.compare(pb, pa);
			});
			StateCache selected = mutableStates.getFirst();
			int selectedPriority = getEffectivePriority(selected, pAnimatable);
			if (selectedPriority == Integer.MIN_VALUE) {
				continue;
			}
			//BaseLogger.log(BaseLogLevel.BLUELIB, "Animation playing: " + selected.animation());
			return pEvent.setAndContinue(Animation.begin().thenLoop(selected.animation()));
		}
		return PlayState.PLAY;
	}

	private static int getEffectivePriority(
			Integer pPriority,
			List<String> pConditions,
			BlueAnimatable pAnimatable) {
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

	private static int getEffectivePriority(StateCache pState, BlueAnimatable pAnimatable) {
		return getEffectivePriority(pState.priority(), pState.conditions(), pAnimatable);
	}

	private static int getEffectiveBehaviourPriority(BehaviourCache pBehaviour, BlueAnimatable pAnimatable) {
		return getEffectivePriority(pBehaviour.priority(), pBehaviour.conditions(), pAnimatable);
	}

	public static ControllerCache getBakedController(ResourceLocation pLocation) {
		ResourceLocation[] attempts = new ResourceLocation[] {
				pLocation,
				LoaderUtils.stripSuffix(".json", pLocation),
				LoaderUtils.stripSuffix(".controller.json", pLocation)
		};

		for (ResourceLocation loc : attempts) {
			ControllerCache controller = ResourceCache.Server.getControllers().get(loc);
			if (controller != null) {
				return controller;
			}
		}

		if (!pLocation.getPath().contains("controller/"))
			throw new RuntimeException("Invalid controller resource path provided - BlueLib controllers must be placed in data/<modid>/controllers/");

		throw new RuntimeException("Unable to find controller file: " + pLocation);
	}
}
