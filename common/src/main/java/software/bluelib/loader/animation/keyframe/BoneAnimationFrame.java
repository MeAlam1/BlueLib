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
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;
import software.bluelib.loader.cache.model.BoneCache;

public record BoneAnimationFrame(@NotNull BoneCache bone, @NotNull AnimationPointFrame rotationXQueue,
		@NotNull AnimationPointFrame rotationYQueue,
		@NotNull AnimationPointFrame rotationZQueue,
		@NotNull AnimationPointFrame positionXQueue,
		@NotNull AnimationPointFrame positionYQueue,
		@NotNull AnimationPointFrame positionZQueue, @NotNull AnimationPointFrame scaleXQueue,
		@NotNull AnimationPointFrame scaleYQueue,
		@NotNull AnimationPointFrame scaleZQueue) {

	public BoneAnimationFrame(@NotNull BoneCache pBone) {
		this(pBone, new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame(),
				new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame(),
				new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame());
	}

	public void addPosXPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addPosYPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addPosZPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextPosition(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, @NotNull BoneSnapshot pStartSnapshot, @NotNull AnimationPoint pNextXPoint, @NotNull AnimationPoint pNextYPoint, @NotNull AnimationPoint pNextZPoint) {
		addPosXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetX(), pNextXPoint.animationStartValue());
		addPosYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetY(), pNextYPoint.animationStartValue());
		addPosZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetZ(), pNextZPoint.animationStartValue());
	}

	public void addScaleXPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addScaleYPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addScaleZPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextScale(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, @NotNull BoneSnapshot pStartSnapshot, @NotNull AnimationPoint pNextXPoint, @NotNull AnimationPoint pNextYPoint, @NotNull AnimationPoint pNextZPoint) {
		addScaleXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleX(), pNextXPoint.animationStartValue());
		addScaleYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleY(), pNextYPoint.animationStartValue());
		addScaleZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleZ(), pNextZPoint.animationStartValue());
	}

	public void addRotationXPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addRotationYPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addRotationZPoint(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextRotation(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, @NotNull BoneSnapshot pStartSnapshot, @NotNull BoneSnapshot pInitialSnapshot, @NotNull AnimationPoint pNextXPoint, @NotNull AnimationPoint pNextYPoint, @NotNull AnimationPoint pNextZPoint) {
		addRotationXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotX() - pInitialSnapshot.getRotX(), pNextXPoint.animationStartValue());
		addRotationYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotY() - pInitialSnapshot.getRotY(), pNextYPoint.animationStartValue());
		addRotationZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotZ() - pInitialSnapshot.getRotZ(), pNextZPoint.animationStartValue());
	}

	public void addPositions(@NotNull AnimationPoint pXPoint, @NotNull AnimationPoint pYPoint, @NotNull AnimationPoint pZPoint) {
		this.positionXQueue.add(pXPoint);
		this.positionYQueue.add(pYPoint);
		this.positionZQueue.add(pZPoint);
	}

	public void addScales(@NotNull AnimationPoint pXPoint, @NotNull AnimationPoint pYPoint, @NotNull AnimationPoint pZPoint) {
		this.scaleXQueue.add(pXPoint);
		this.scaleYQueue.add(pYPoint);
		this.scaleZQueue.add(pZPoint);
	}

	public void addRotations(@NotNull AnimationPoint pXPoint, @NotNull AnimationPoint pYPoint, @NotNull AnimationPoint pZPoint) {
		this.rotationXQueue.add(pXPoint);
		this.rotationYQueue.add(pYPoint);
		this.rotationZQueue.add(pZPoint);
	}
}
