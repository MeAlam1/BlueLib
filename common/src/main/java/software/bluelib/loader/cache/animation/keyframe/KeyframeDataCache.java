package software.bluelib.loader.cache.animation.keyframe;

public sealed interface KeyframeDataCache
		permits ParticleKeyframeCache, SoundKeyframeCache, CustomInstructionKeyframeCache {

	double startTick();

	default double getStartTick() {
		return startTick();
	}
}