/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bluelib.loader.animation.keyframe;


public record KeyframeLocation<T extends Keyframe<?>>(T keyframe, double startTick) { }
