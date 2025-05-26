package software.bluelib.loader.animatable.instance;

import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimatableManager;


public class InstancedAnimatableInstanceCache extends AnimatableInstanceCache {
	protected AnimatableManager<?> manager;

	public InstancedAnimatableInstanceCache(GeoAnimatable animatable) {
		super(animatable);
	}

	
	@Override
	public AnimatableManager<?> getManagerForId(long uniqueId) {
		if (this.manager == null)
			this.manager = new AnimatableManager<>(this.animatable);

		return this.manager;
	}
}