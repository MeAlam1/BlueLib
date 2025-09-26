/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.event;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.geckolib.animations.KeyFrameData;

public abstract class KeyFrameEvent<T extends BlueAnimatable, E extends KeyFrameData> {

	@NotNull
	private final T animatable;
	private final double animationTick;
	@NotNull
	private final AnimationController<T> controller;
	@NotNull
	private final E eventKeyFrame;

	public KeyFrameEvent(@NotNull T pAnimatable, double pAnimationTick, @NotNull AnimationController<T> pController, @NotNull E pEventKeyFrame) {
		this.animatable = pAnimatable;
		this.animationTick = pAnimationTick;
		this.controller = pController;
		this.eventKeyFrame = pEventKeyFrame;
	}

	public double getAnimationTick() {
		return animationTick;
	}

	@NotNull
	public T getAnimatable() {
		return animatable;
	}

	@NotNull
	public AnimationController<T> getController() {
		return controller;
	}

	@NotNull
	public E getKeyframeData() {
		return this.eventKeyFrame;
	}
}
