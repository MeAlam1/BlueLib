package software.bluelib.loader.controller;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.LoaderUtils;
import software.bluelib.internal.BlueResource;
import software.bluelib.loader.animatable.AnimatableManager;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.cache.controller.GroupCache;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.animation.PlayState;
import software.bluelib.oldLoader.animation.RawAnimation;

public class ControllerManager {

	public static void registerControllers(BlueAnimatable pAnimatable, @NotNull ControllerCache pCache, @NotNull AnimatableManager.ControllerRegistrar pControllers, @Nullable String pGroupName) {
		GroupCache group = pGroupName == null ? pCache.getMainGroup() : pCache.getGroup(pGroupName);
		for (String behaviourName : group.behaviours().keySet()) {
			pControllers.add(new AnimationController<>(pAnimatable, "Idle", 5, ControllerManager::idleAnimController));
		}
	}

	protected static <E extends BlueAnimatable> PlayState idleAnimController(final AnimationState<E> pEvent) {
		return pEvent.setAndContinue(RawAnimation.begin().thenLoop(ResourceCache.Server.getControllers().get(BlueResource.resource("controllers/test")).getMainGroup().getBehaviour("idle").getMainState("base").animation()));
	}

	public static ControllerCache getBakedController(ResourceLocation pLocation) {
		ResourceLocation[] attempts = new ResourceLocation[]{
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
