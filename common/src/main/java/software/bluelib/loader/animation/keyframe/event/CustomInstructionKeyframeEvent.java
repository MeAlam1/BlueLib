/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe.event;

import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.event.data.CustomInstructionKeyframeData;


public class CustomInstructionKeyframeEvent<T extends GeoAnimatable> extends KeyFrameEvent<T, CustomInstructionKeyframeData> {
	public CustomInstructionKeyframeEvent(T entity, double animationTick, AnimationController<T> controller,
										  CustomInstructionKeyframeData customInstructionKeyframeData) {
		super(entity, animationTick, controller, customInstructionKeyframeData);
	}

	
	@Override
	public CustomInstructionKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
