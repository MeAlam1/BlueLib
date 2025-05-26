package software.bluelib.loader.animatable;

import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.AnimationProcessor;
import software.bluelib.loader.cache.object.GeoBone;


public interface GeoAnimatable {
	
	void registerControllers(AnimatableManager.ControllerRegistrar controllers);

	
	AnimatableInstanceCache getAnimatableInstanceCache();

	
	default double getBoneResetTime() {
		return 5;
	}

	
	default boolean shouldPlayAnimsWhileGamePaused() {
		return false;
	}

	
	double getTick(Object object);

	
	default AnimatableInstanceCache animatableCacheOverride() {
		return null;
	}
}
