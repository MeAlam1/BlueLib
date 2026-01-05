package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BoneAnimationCache(
		@NotNull String boneName,
		@Nullable TransformChannelCache rotation,
		@Nullable TransformChannelCache position,
		@Nullable TransformChannelCache scale
) {
}