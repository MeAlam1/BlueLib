package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.NotNull;

public record SoundKeyframeCache(
		double startTick,
		@NotNull String sound
) implements KeyframeDataCache {

	public @NotNull String getSound() {
		return sound;
	}
}