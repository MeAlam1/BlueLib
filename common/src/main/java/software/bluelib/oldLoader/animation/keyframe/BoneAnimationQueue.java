/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation.keyframe;

import software.bluelib.client.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.oldLoader.animation.state.BoneSnapshot;

public record BoneAnimationQueue(BoneCache bone, AnimationPointQueue rotationXQueue, AnimationPointQueue rotationYQueue,
		AnimationPointQueue rotationZQueue, AnimationPointQueue positionXQueue, AnimationPointQueue positionYQueue,
		AnimationPointQueue positionZQueue, AnimationPointQueue scaleXQueue, AnimationPointQueue scaleYQueue,
		AnimationPointQueue scaleZQueue) {

	public BoneAnimationQueue(BoneCache bone) {
		this(bone, new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(),
				new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(),
				new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue());
	}

	public void addPosXPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.positionXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addPosYPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.positionYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addPosZPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.positionZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addNextPosition(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
		addPosXPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getOffsetX(), nextXPoint.animationStartValue());
		addPosYPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getOffsetY(), nextYPoint.animationStartValue());
		addPosZPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getOffsetZ(), nextZPoint.animationStartValue());
	}

	public void addScaleXPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.scaleXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addScaleYPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.scaleYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addScaleZPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.scaleZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addNextScale(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
		addScaleXPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getScaleX(), nextXPoint.animationStartValue());
		addScaleYPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getScaleY(), nextYPoint.animationStartValue());
		addScaleZPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getScaleZ(), nextZPoint.animationStartValue());
	}

	public void addRotationXPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.rotationXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addRotationYPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.rotationYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addRotationZPoint(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
		this.rotationZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
	}

	public void addNextRotation(KeyframeCache<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, BoneSnapshot initialSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
		addRotationXPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getRotX() - initialSnapshot.getRotX(), nextXPoint.animationStartValue());
		addRotationYPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getRotY() - initialSnapshot.getRotY(), nextYPoint.animationStartValue());
		addRotationZPoint(keyFrame, lerpedTick, transitionLength, startSnapshot.getRotZ() - initialSnapshot.getRotZ(), nextZPoint.animationStartValue());
	}

	public void addPositions(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
		this.positionXQueue.add(xPoint);
		this.positionYQueue.add(yPoint);
		this.positionZQueue.add(zPoint);
	}

	public void addScales(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
		this.scaleXQueue.add(xPoint);
		this.scaleYQueue.add(yPoint);
		this.scaleZQueue.add(zPoint);
	}

	public void addRotations(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
		this.rotationXQueue.add(xPoint);
		this.rotationYQueue.add(yPoint);
		this.rotationZQueue.add(zPoint);
	}
}
