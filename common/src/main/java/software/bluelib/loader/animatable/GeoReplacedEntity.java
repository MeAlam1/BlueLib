package software.bluelib.loader.animatable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;

import java.util.function.Consumer;


public interface GeoReplacedEntity extends SingletonGeoAnimatable {
	
	EntityType<?> getReplacingEntityType();

	
	@ApiStatus.NonExtendable
	@Nullable
	default <D> D getAnimData(Entity entity, SerializableDataTicket<D> dataTicket) {
		return getAnimatableInstanceCache().getManagerForId(entity.getId()).getData(dataTicket);
	}

	
	@ApiStatus.NonExtendable
	default <D> void setAnimData(Entity relatedEntity, SerializableDataTicket<D> dataTicket, D data) {
		if (relatedEntity.level().isClientSide()) {
			getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).setData(dataTicket, data);
		}
		else {
			GeckoLibServices.NETWORK.syncEntityAnimData(relatedEntity, true, dataTicket, data);
		}
	}

	
	@ApiStatus.NonExtendable
	default void triggerAnim(Entity relatedEntity, @Nullable String controllerName, String animName) {
		if (relatedEntity.level().isClientSide()) {
			if (controllerName != null) {
				getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).tryTriggerAnimation(controllerName, animName);
			}
			else {
				getAnimatableInstanceCache().getManagerForId(relatedEntity.getId()).tryTriggerAnimation(animName);
			}
		}
		else {
			GeckoLibServices.NETWORK.triggerEntityAnim(relatedEntity, true, controllerName, animName);
		}
	}

	
	@ApiStatus.NonExtendable
	default void stopTriggeredAnim(Entity relatedEntity, @Nullable String controllerName, @Nullable String animName) {
		if (relatedEntity.level().isClientSide()) {
			AnimatableManager<GeoAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(relatedEntity.getId());

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
			GeckoLibServices.NETWORK.stopTriggeredEntityAnim(relatedEntity, true, controllerName, animName);
		}
	}
	
	
	@Override
	default double getTick(Object entity) {
		return ((Entity)entity).tickCount;
	}

	// These methods aren't used for GeoReplacedEntity
	@ApiStatus.NonExtendable
	@Override
	default void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {}

	// These methods aren't used for GeoReplacedEntity
	@ApiStatus.NonExtendable
	@Override
	default Object getRenderProvider() {
		return null;
	}
}
