package software.bluelib.loader.animatable;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.animatable.instance.AnimatableInstanceCache;
import software.bluelib.loader.animatable.instance.SingletonAnimatableInstanceCache;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.util.GeckoLibUtil;

import java.util.function.Consumer;


public interface SingletonGeoAnimatable extends GeoAnimatable {
    
    static void registerSyncedAnimatable(GeoAnimatable animatable) {
        GeckoLibUtil.registerSyncedAnimatable(animatable);
    }

    
    @ApiStatus.NonExtendable
    @Nullable
    default <D> D getAnimData(long instanceId, SerializableDataTicket<D> dataTicket) {
        return getAnimatableInstanceCache().getManagerForId(instanceId).getData(dataTicket);
    }

    
    @ApiStatus.NonExtendable
    default <D> void setAnimData(Entity relatedEntity, long instanceId, SerializableDataTicket<D> dataTicket, D data) {
        if (relatedEntity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(instanceId).setData(dataTicket, data);
        }
        else {
            syncAnimData(instanceId, dataTicket, data, relatedEntity);
        }
    }

    
    @ApiStatus.NonExtendable
    default <D> void syncAnimData(long instanceId, SerializableDataTicket<D> dataTicket, D data, Entity entityToTrack) {
        GeckoLibServices.NETWORK.syncSingletonAnimData(this, instanceId, dataTicket, data, entityToTrack);
    }

    
    @ApiStatus.NonExtendable
    default <D> void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            if (controllerName != null) {
                getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
            }
            else {
                getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(animName);
            }
        }
        else {
            GeckoLibServices.NETWORK.triggerSingletonAnim(this, relatedEntity, instanceId, controllerName, animName);
        }
    }

    
    @ApiStatus.NonExtendable
    default void stopTriggeredAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, @Nullable String animName) {
        if (relatedEntity.level().isClientSide()) {
            AnimatableManager<GeoAnimatable> animatableManager = getAnimatableInstanceCache().getManagerForId(instanceId);

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
            GeckoLibServices.NETWORK.stopTriggeredSingletonAnim(this, relatedEntity, instanceId, controllerName, animName);
        }
    }

    
    @ApiStatus.NonExtendable
    default void triggerArmorAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        triggerAnim(relatedEntity, -instanceId, controllerName, animName);
    }

    
    @ApiStatus.NonExtendable
    default void stopTriggeredArmorAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, @Nullable String animName) {
        stopTriggeredAnim(relatedEntity, -instanceId, controllerName, animName);
    }

    
    @Override
    default @Nullable AnimatableInstanceCache animatableCacheOverride() {
        return new SingletonAnimatableInstanceCache(this);
    }

    
    default void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {}

    
    default Object getRenderProvider() {
        return getAnimatableInstanceCache().getRenderProvider();
    }
}
