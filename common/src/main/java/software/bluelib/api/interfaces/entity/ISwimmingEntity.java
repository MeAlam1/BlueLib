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
public interface ISwimmingEntity {

    default boolean getSwimmingState(LivingEntity pEntity) {
        return EntityStateManager.getSwimmingState(pEntity);
    }

    default void setSwimmingState(LivingEntity pEntity, boolean pSwimming) {
        EntityStateManager.setSwimmingState(pEntity, pSwimming);
    }

    default double getSwimmingSpeedMultiplier(LivingEntity pEntity) {
        return pEntity.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
    }

    default void setSwimmingSpeedMultiplier(LivingEntity pEntity, double pSpeedMultiplier) {
        AttributeInstance swimmingSpeedAttribute = pEntity.getAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY);
        if (swimmingSpeedAttribute != null) {
            swimmingSpeedAttribute.setBaseValue(pSpeedMultiplier);
        } else {
            BaseLogger.log(BaseLogLevel.ERROR, pEntity + " does not have a swimming speed attribute.", true);
            throw new IllegalStateException(pEntity + " does not have a swimming speed attribute.");
        }
    }

    default boolean canSwim(LivingEntity pEntity) {
        return EntityStateManager.getCanSwim(pEntity);
    }

    default void canSwim(LivingEntity pEntity, boolean pCanSwim) {
        EntityStateManager.setCanSwim(pEntity, pCanSwim);
    }

    default int getSwimmingCooldown(LivingEntity pEntity) {
        return EntityStateManager.getSwimmingCooldown(pEntity);
    }

    default void setSwimmingCooldown(LivingEntity pEntity, int pSwimCooldown) {
        EntityStateManager.setSwimmingCooldown(pEntity, pSwimCooldown);
    }

    default int getDepth(LivingEntity pEntity) {
        BlockPos blockPos = pEntity.getOnPos();
        return blockPos.getY();
    }
}
