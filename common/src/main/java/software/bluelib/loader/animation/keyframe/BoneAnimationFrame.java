/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import software.bluelib.loader.animation.AnimationPoint;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.loader.cache.model.BoneCache;

public record BoneAnimationFrame(BoneCache bone, AnimationPointFrame rotationXQueue, AnimationPointFrame rotationYQueue,
		AnimationPointFrame rotationZQueue, AnimationPointFrame positionXQueue,
		AnimationPointFrame positionYQueue,
		AnimationPointFrame positionZQueue, AnimationPointFrame scaleXQueue,
		AnimationPointFrame scaleYQueue,
		AnimationPointFrame scaleZQueue) {

	public BoneAnimationFrame(BoneCache pBone) {
		this(pBone, new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame(),
				new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame(),
				new AnimationPointFrame(), new AnimationPointFrame(), new AnimationPointFrame());
	}

	public void addPosXPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addPosYPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addPosZPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.positionZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextPosition(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, BoneSnapshot pStartSnapshot, AnimationPoint pNextXPoint, AnimationPoint pNextYPoint, AnimationPoint pNextZPoint) {
		addPosXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetX(), pNextXPoint.animationStartValue());
		addPosYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetY(), pNextYPoint.animationStartValue());
		addPosZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getOffsetZ(), pNextZPoint.animationStartValue());
	}

	public void addScaleXPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addScaleYPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addScaleZPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.scaleZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextScale(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, BoneSnapshot pStartSnapshot, AnimationPoint pNextXPoint, AnimationPoint pNextYPoint, AnimationPoint pNextZPoint) {
		addScaleXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleX(), pNextXPoint.animationStartValue());
		addScaleYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleY(), pNextYPoint.animationStartValue());
		addScaleZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getScaleZ(), pNextZPoint.animationStartValue());
	}

	public void addRotationXPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationXQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addRotationYPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationYQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addRotationZPoint(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, double pStartValue, double pEndValue) {
		this.rotationZQueue.add(new AnimationPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartValue, pEndValue));
	}

	public void addNextRotation(KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength, BoneSnapshot pStartSnapshot, BoneSnapshot pInitialSnapshot, AnimationPoint pNextXPoint, AnimationPoint pNextYPoint, AnimationPoint pNextZPoint) {
		addRotationXPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotX() - pInitialSnapshot.getRotX(), pNextXPoint.animationStartValue());
		addRotationYPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotY() - pInitialSnapshot.getRotY(), pNextYPoint.animationStartValue());
		addRotationZPoint(pKeyFrame, pLerpedTick, pTransitionLength, pStartSnapshot.getRotZ() - pInitialSnapshot.getRotZ(), pNextZPoint.animationStartValue());
	}

	public void addPositions(AnimationPoint pXPoint, AnimationPoint pYPoint, AnimationPoint pZPoint) {
		this.positionXQueue.add(pXPoint);
		this.positionYQueue.add(pYPoint);
		this.positionZQueue.add(pZPoint);
	}

	public void addScales(AnimationPoint pXPoint, AnimationPoint pYPoint, AnimationPoint pZPoint) {
		this.scaleXQueue.add(pXPoint);
		this.scaleYQueue.add(pYPoint);
		this.scaleZQueue.add(pZPoint);
	}

	public void addRotations(AnimationPoint pXPoint, AnimationPoint pYPoint, AnimationPoint pZPoint) {
		this.rotationXQueue.add(pXPoint);
		this.rotationYQueue.add(pYPoint);
		this.rotationZQueue.add(pZPoint);
	}
}
