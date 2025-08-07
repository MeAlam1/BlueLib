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
import software.bluelib.loader.animation.keyframe.data.SoundKeyframeData;

public class SoundKeyframeEvent<T extends BlueAnimatable> extends KeyFrameEvent<T, SoundKeyframeData> {

	public SoundKeyframeEvent(T pAnimatable, double pAnimationTick, AnimationController<T> pController, SoundKeyframeData pKeyFrameData) {
		super(pAnimatable, pAnimationTick, pController, pKeyFrameData);
	}

	@Override
	public SoundKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
