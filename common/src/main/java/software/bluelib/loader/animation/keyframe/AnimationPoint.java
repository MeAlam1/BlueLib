/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe;


public record AnimationPoint(Keyframe<?> keyFrame, double currentTick, double transitionLength, double animationStartValue, double animationEndValue) {
	@Override
	public String toString() {
		return "Tick: " + this.currentTick +
				" | Transition Length: " + this.transitionLength +
				" | Start Value: " + this.animationStartValue +
				" | End Value: " + this.animationEndValue;
	}
}
