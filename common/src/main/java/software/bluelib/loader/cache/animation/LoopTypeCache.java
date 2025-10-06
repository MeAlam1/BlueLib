/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;

public record LoopTypeCache(@NotNull String name,
		@NotNull Behavior behavior) {

	private static final @NotNull Map<String, LoopTypeCache> ALL_LOOP_TYPES = new ConcurrentHashMap<>(4);

	@FunctionalInterface
	public interface Behavior {

		boolean shouldPlayAgain(@NotNull BlueAnimatable pAnimatable,
				@NotNull AnimationController<? extends BlueAnimatable> pController,
				@NotNull AnimationCache pAnimationCache);
	}

	@NotNull
	public LoopTypeCache register() {
		ALL_LOOP_TYPES.put(name, this);
		return this;
	}

	public static @NotNull LoopTypeCache fromString(@NotNull String pName) {
		return ALL_LOOP_TYPES.getOrDefault(pName, PLAY_ONCE);
	}

	@NotNull
	public static final LoopTypeCache DEFAULT = new LoopTypeCache("default", (animatable, controller, currentAnimation) -> {
		if (currentAnimation.loopType() == null) {
			return false;
		}
		return currentAnimation.loopType().behavior().shouldPlayAgain(animatable, controller, currentAnimation);
	}).register();
	@NotNull
	public static final LoopTypeCache PLAY_ONCE = new LoopTypeCache("play_once",
			(anim, controller, cache) -> false).register();
	@NotNull
	public static final LoopTypeCache LOOP = new LoopTypeCache("loop",
			(anim, controller, cache) -> true).register();
	@NotNull
	public static final LoopTypeCache HOLD_ON_LAST_FRAME = new LoopTypeCache("hold_on_last_frame",
			(anim, controller, cache) -> {
				controller.animationState = AnimationController.State.PAUSED;
				return true;
			}).register();
}
