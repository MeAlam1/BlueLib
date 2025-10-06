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
import software.bluelib.loader.cache.animation.keyframe.ParticleKeyframeCache;
import software.bluelib.loader.geckolib.animations.ParticleKeyframeData;

public class ParticleKeyframeEvent<T extends BlueAnimatable> extends KeyFrameEvent<T, ParticleKeyframeCache> {

	public ParticleKeyframeEvent(@NotNull T pAnimatable, double pAnimationTick, @NotNull AnimationController<T> pController, @NotNull ParticleKeyframeCache pParticleKeyFrameData) {
		super(pAnimatable, pAnimationTick, pController, pParticleKeyFrameData);
	}

	@Override
	@NotNull
	public ParticleKeyframeCache getKeyframeData() {
		return super.getKeyframeData();
	}
}
