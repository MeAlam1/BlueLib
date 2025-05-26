package software.bluelib.loader.animation.keyframe.event;

import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.event.data.ParticleKeyframeData;


public class ParticleKeyframeEvent<T extends GeoAnimatable> extends KeyFrameEvent<T, ParticleKeyframeData> {
	public ParticleKeyframeEvent(T animatable, double animationTick, AnimationController<T> controller, ParticleKeyframeData particleKeyFrameData) {
		super(animatable, animationTick, controller, particleKeyFrameData);
	}

	
	@Override
	public ParticleKeyframeData getKeyframeData() {
		return super.getKeyframeData();
	}
}
