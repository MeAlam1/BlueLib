/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.keyframe.frame.AnimationFrameVector;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.loader.cache.model.BoneCache;

public final class BoneAnimationFrame {

	private final BoneCache bone;
	private final AnimationFrameVector rotation;
	private final AnimationFrameVector position;
	private final AnimationFrameVector scale;

	public BoneAnimationFrame(@NotNull BoneCache pBone) {
		this.bone = pBone;
		this.rotation = new AnimationFrameVector();
		this.position = new AnimationFrameVector();
		this.scale = new AnimationFrameVector();
	}

	public void addNextPosition(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
			@NotNull BoneFrame pStartSnapshot,
			@NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		position.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getOffsetX(), pNextX.startValue(),
				pStartSnapshot.getOffsetY(), pNextY.startValue(),
				pStartSnapshot.getOffsetZ(), pNextZ.startValue());
	}

	public void addNextScale(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
			@NotNull BoneFrame pStartSnapshot,
			@NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		scale.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getScaleX(), pNextX.startValue(),
				pStartSnapshot.getScaleY(), pNextY.startValue(),
				pStartSnapshot.getScaleZ(), pNextZ.startValue());
	}

	public void addNextRotation(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
			@NotNull BoneFrame pStartSnapshot, @NotNull BoneFrame pInitialSnapshot,
			@NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		rotation.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getRotX() - pInitialSnapshot.getRotX(), pNextX.startValue(),
				pStartSnapshot.getRotY() - pInitialSnapshot.getRotY(), pNextY.startValue(),
				pStartSnapshot.getRotZ() - pInitialSnapshot.getRotZ(), pNextZ.startValue());
	}

	public void addNextRotation(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		rotation.add(pX, pY, pZ);
	}

	public void addNextPosition(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		position.add(pX, pY, pZ);
	}

	public void addNextScale(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		scale.add(pX, pY, pZ);
	}

	public BoneCache getBone() {
		return bone;
	}

	public AnimationFrameVector getRotation() {
		return rotation;
	}

	public AnimationFrameVector getPosition() {
		return position;
	}

	public AnimationFrameVector getScale() {
		return scale;
	}
}
