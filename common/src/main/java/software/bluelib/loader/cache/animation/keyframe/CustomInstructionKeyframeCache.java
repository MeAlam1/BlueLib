package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.NotNull;

public record CustomInstructionKeyframeCache(
		double startTick,
		@NotNull String instructions
) implements KeyframeDataCache {

	public @NotNull String getInstructions() {
		return instructions;
	}
}