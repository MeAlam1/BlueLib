// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.api.interfaces.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import software.bluelib.api.entity.EntityStateManager;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public interface IFlyingEntity {

    default boolean getFlyingState(LivingEntity pEntity) {
        return EntityStateManager.getFlyingState(pEntity);
    }

    default void setFlyingState(LivingEntity pEntity, boolean pFlying) {
        EntityStateManager.setFlyingState(pEntity, pFlying);
    }

    default double getFlyingSpeedMultiplier(LivingEntity pEntity) {
        return pEntity.getAttributeValue(Attributes.FLYING_SPEED);
    }

    default void setFlyingSpeedMultiplier(LivingEntity pEntity, double pSpeedMultiplier) {
        AttributeInstance flyingSpeedAttribute = pEntity.getAttribute(Attributes.FLYING_SPEED);
        if (flyingSpeedAttribute != null) {
            flyingSpeedAttribute.setBaseValue(pSpeedMultiplier);
        } else {
            BaseLogger.log(BaseLogLevel.ERROR, pEntity + " does not have a flying speed attribute.", true);
            throw new IllegalStateException(pEntity + " does not have a flying speed attribute.");
        }
    }

    default boolean canFly(LivingEntity pEntity) {
        return EntityStateManager.getCanFly(pEntity);
    }

    default void canFly(LivingEntity pEntity, boolean pCanFly) {
        EntityStateManager.setCanFly(pEntity, pCanFly);
    }

    default int getFlightCooldown(LivingEntity pEntity) {
        return EntityStateManager.getFlyingCooldown(pEntity);
    }

    default void setFlightCooldown(LivingEntity pEntity, int pFlightCooldown) {
        EntityStateManager.setFlyingCooldown(pEntity, pFlightCooldown);
    }

    default int getAltitude(LivingEntity pEntity) {
        BlockPos blockPos = pEntity.getOnPos();
        return blockPos.getY();
    }
}
