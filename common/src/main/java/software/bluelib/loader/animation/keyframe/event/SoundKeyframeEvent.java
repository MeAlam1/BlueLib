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
import software.bluelib.loader.animation.keyframe.data.SoundKeyframeData;

public class SoundKeyframeEvent<T extends BlueAnimatable> extends KeyFrameEvent<T, SoundKeyframeData> {

	public SoundKeyframeEvent(@NotNull T pAnimatable, double pAnimationTick, @NotNull AnimationController<T> pController, @NotNull SoundKeyframeData pKeyFrameData) {
		super(pAnimatable, pAnimationTick, pController, pKeyFrameData);
	}

	@Override
	@NotNull
	public SoundKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
