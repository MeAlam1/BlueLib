package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.animation.keyframe.TransformKeyframeCache;

public record BoneAnimationCache(
		@NotNull String boneName,
		@Nullable TransformKeyframeCache rotation,
		@Nullable TransformKeyframeCache position,
		@Nullable TransformKeyframeCache scale
) {
}