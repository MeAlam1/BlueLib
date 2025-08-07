/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.event;

import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.data.KeyFrameData;

public abstract class KeyFrameEvent<T extends BlueAnimatable, E extends KeyFrameData> {

	private final T animatable;
	private final double animationTick;
	private final AnimationController<T> controller;
	private final E eventKeyFrame;

	public KeyFrameEvent(T pAnimatable, double pAnimationTick, AnimationController<T> pController, E pEventKeyFrame) {
		this.animatable = pAnimatable;
		this.animationTick = pAnimationTick;
		this.controller = pController;
		this.eventKeyFrame = pEventKeyFrame;
	}

	public double getAnimationTick() {
		return animationTick;
	}

	public T getAnimatable() {
		return animatable;
	}

	public AnimationController<T> getController() {
		return controller;
	}

	public E getKeyframeData() {
		return this.eventKeyFrame;
	}
}
