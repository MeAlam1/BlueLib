package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;

import java.util.List;

public record TransformKeyframeCache(
		@Nullable List<MoLangValue> staticValue,
		@Nullable KeyframeCache keyframes
) {
	public boolean isStatic() {
		return staticValue != null;
	}

	public boolean hasKeyframes() {
		return keyframes != null;
	}
}