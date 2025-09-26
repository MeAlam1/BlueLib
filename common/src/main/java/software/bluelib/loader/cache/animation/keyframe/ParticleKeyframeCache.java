package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.NotNull;

public record ParticleKeyframeCache(
		double startTick,
		@NotNull String effect,
		@NotNull String locator,
		@NotNull String script
) implements KeyframeDataCache {
	
	public @NotNull String getEffect() {
		return effect;
	}

	public @NotNull String getLocator() {
		return locator;
	}
}