/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.cache.animation.AnimationCache;

// TODO: WHY GECKOLIB, WHY
@FunctionalInterface
public interface LoopType {

	@NotNull
	Map<String, LoopType> LOOP_TYPES = new ConcurrentHashMap<>(4);

	@NotNull
	LoopType DEFAULT = (animatable, controller, currentAnimation) -> currentAnimation.loopType().behavior().shouldPlayAgain(animatable, controller, currentAnimation);
	@NotNull
	LoopType PLAY_ONCE = register("play_once", register("false", (animatable, controller, currentAnimation) -> false));
	@NotNull
	LoopType HOLD_ON_LAST_FRAME = register("hold_on_last_frame", (animatable, controller, currentAnimation) -> {
		controller.animationState = AnimationController.State.PAUSED;

		return true;
	});
	@NotNull
	LoopType LOOP = register("loop", register("true", (animatable, controller, currentAnimation) -> true));

	boolean shouldPlayAgain(@NotNull BlueAnimatable pAnimatable, @NotNull AnimationController<? extends BlueAnimatable> pController, @NotNull AnimationCache pCurrentAnimationCache);

	@NotNull
	static LoopType register(@NotNull String pName, @NotNull LoopType pLoopType) {
		LOOP_TYPES.put(pName, pLoopType);

		return pLoopType;
	}
}
