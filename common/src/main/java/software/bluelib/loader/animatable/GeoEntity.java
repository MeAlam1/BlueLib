package software.bluelib.loader.animatable;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;


public interface GeoEntity extends GeoAnimatable {
	
	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
		return getAnimatableInstanceCache().getManagerForId(((Entity)this).getId()).getData(dataTicket);
	}

	
	@ApiStatus.NonExtendable
	default <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
		Entity entity = (Entity)this;

		if (entity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(entity.getId()).setData(dataTicket, data);
		}
		else {
			GeckoLibServices.NETWORK.syncEntityAnimData(entity, false, dataTicket, data);
		}
	}

	
	@ApiStatus.NonExtendable
	default void triggerAnim(@Nullable String controllerName, String animName) {
		Entity entity = (Entity)this;

		if (entity.level().isClientSide()) {
			if (controllerName != null) {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(controllerName, animName);
			}
			else {
				getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(animName);
			}
		}
		else {
			GeckoLibServices.NETWORK.triggerEntityAnim(entity, false, controllerName, animName);
		}
	}

	
	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(@Nullable String controllerName, @Nullable String animName) {
		Entity entity = (Entity)this;

		if (entity.level().isClientSide()) {
			AnimatableManager<GeoAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(entity.getId());

			if (animatableManager == null)
				return;

			if (controllerName != null) {
				animatableManager.stopTriggeredAnimation(controllerName, animName);
			}
			else {
				animatableManager.stopTriggeredAnimation(animName);
			}
		}
		else {
			GeckoLibServices.NETWORK.stopTriggeredEntityAnim(entity, false, controllerName, animName);
		}
	}
	
	
	@Override
	default double getTick(Object entity) {
		return ((Entity)entity).tickCount;
	}
}
