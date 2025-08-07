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
import software.bluelib.loader.animation.keyframe.data.CustomInstructionKeyframeData;

public class CustomInstructionKeyframeEvent<T extends BlueAnimatable> extends KeyFrameEvent<T, CustomInstructionKeyframeData> {

	public CustomInstructionKeyframeEvent(T pEntity, double pAnimationTick, AnimationController<T> pController,
			CustomInstructionKeyframeData pCustomInstructionKeyframeData) {
		super(pEntity, pAnimationTick, pController, pCustomInstructionKeyframeData);
	}

	@Override
	public CustomInstructionKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
