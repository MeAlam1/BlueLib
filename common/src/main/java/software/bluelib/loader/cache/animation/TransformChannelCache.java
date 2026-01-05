package software.bluelib.loader.cache.animation;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;

public sealed interface TransformChannelCache permits TransformChannelCache.Array, TransformChannelCache.Keyframes {

	record Array(@NotNull List<MoLangValue> values) implements TransformChannelCache {
	}

	record Keyframes(@NotNull KeyframeCache keyframes) implements TransformChannelCache {
	}
}