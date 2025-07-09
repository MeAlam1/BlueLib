/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation.keyframe.event;

import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.keyframe.event.data.KeyFrameData;

public abstract class KeyFrameEvent<T extends BlueAnimatable, E extends KeyFrameData> {

	private final T animatable;
	private final double animationTick;
	private final AnimationController<T> controller;
	private final E eventKeyFrame;

	public KeyFrameEvent(T animatable, double animationTick, AnimationController<T> controller, E eventKeyFrame) {
		this.animatable = animatable;
		this.animationTick = animationTick;
		this.controller = controller;
		this.eventKeyFrame = eventKeyFrame;
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
