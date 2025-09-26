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
import software.bluelib.loader.geckolib.animations.CustomInstructionKeyframeData;

public class CustomInstructionKeyframeEvent<T extends BlueAnimatable> extends KeyFrameEvent<T, CustomInstructionKeyframeData> {

	public CustomInstructionKeyframeEvent(@NotNull T pEntity, double pAnimationTick, @NotNull AnimationController<T> pController,
			@NotNull CustomInstructionKeyframeData pCustomInstructionKeyframeData) {
		super(pEntity, pAnimationTick, pController, pCustomInstructionKeyframeData);
	}

	@Override
	public @NotNull CustomInstructionKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
