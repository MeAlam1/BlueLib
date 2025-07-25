package software.bluelib.loader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.controller.BehaviourCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.controller.GroupCache;
import software.bluelib.loader.cache.controller.StateCache;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.animation.PlayState;
import software.bluelib.oldLoader.animation.RawAnimation;

public class ControllerManager {

	public static void registerControllers(BlueAnimatable pAnimatable, @NotNull ControllerCache pCache, @NotNull AnimatableManager.ControllerRegistrar pControllers, @Nullable String pGroupName) {
		GroupCache group = pGroupName == null ? pCache.getMainGroup() : pCache.getGroup(pGroupName);
		Map<String, BehaviourCache> behaviours = group.behaviours();
		if (behaviours.isEmpty()) {
			return;
		}
		for (Map.Entry<String, BehaviourCache> entry : group.behaviours().entrySet()) {
			String behaviourName = entry.getKey();
			BehaviourCache behaviourCache = entry.getValue();
			pControllers.add(new AnimationController<>(pAnimatable, behaviourName, 5, k -> ControllerManager.animationController(k, behaviourCache, pAnimatable)));
		}
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
			BaseLogger.log(BaseLogLevel.BLUELIB, "Animation playing: " + selected.animation());
			return pEvent.setAndContinue(RawAnimation.begin().thenLoop(selected.animation()));
		}
		return PlayState.CONTINUE;
	}

	private static int getEffectivePriority(StateCache pState, BlueAnimatable pAnimatable) {
		int priority = pState.priority() == null ? Integer.MIN_VALUE : pState.priority();
		if (pAnimatable instanceof Entity entity) {
			for (var condition : pState.conditions()) {
				Object result = MoLang.entity(condition, entity);
				BaseLogger.log(BaseLogLevel.BLUELIB, "MoLang result for condition '" + condition + "': " + result);
				if (!(Boolean) result) {
					return Integer.MIN_VALUE;
				}
			}
		}
		return priority;
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

		if (!pLocation.getPath().contains("controllers/"))
			throw new RuntimeException("Invalid controller resource path provided - BlueLib controllers must be placed in data/<modid>/controllers/");

		throw new RuntimeException("Unable to find controller file: " + pLocation);
	}
}
