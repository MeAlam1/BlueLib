/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe;

import software.bluelib.loader.loading.math.MathValue;


public record BoneAnimation(String boneName,
							KeyframeStack<Keyframe<MathValue>> rotationKeyFrames,
							KeyframeStack<Keyframe<MathValue>> positionKeyFrames,
							KeyframeStack<Keyframe<MathValue>> scaleKeyFrames) {
}
