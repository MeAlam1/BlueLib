/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe.event;

import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.Keyframe;
import software.bluelib.loader.animation.keyframe.event.data.KeyFrameData;


public abstract class KeyFrameEvent<T extends GeoAnimatable, E extends KeyFrameData> {
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
