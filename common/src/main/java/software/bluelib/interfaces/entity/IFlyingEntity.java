// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.interfaces.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import software.bluelib.entity.EntityStateManager;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * An interface for flying entities in the BlueLib library.
 * <p>
 * Purpose: This interface defines some basic methods for managing the behavior, state, and properties of entities capable of flight.<br>
 * When: These methods are invoked during interactions or gameplay mechanics for flying entities.<br>
 * Where: This interface is implemented by entities that are capable of flying.<br>
 * Additional Info: The interface provides default implementations for common flying-related features such as flight state, speed, and altitude.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #getFlyingState(LivingEntity)} - Retrieves the flying state of the entity.</li>
 * <li>{@link #setFlyingState(LivingEntity, boolean)} - Sets the flying state of the entity.</li>
 * <li>{@link #getFlyingSpeedMultiplier(LivingEntity)} - Retrieves the speed multiplier for the entity while flying.</li>
 * <li>{@link #setFlyingSpeedMultiplier(LivingEntity, double)} - Sets the speed multiplier for the entity while flying.</li>
 * <li>{@link #canFly(LivingEntity)} - Checks if the entity is capable of flight.</li>
 * <li>{@link #getFlightCooldown(LivingEntity)} - Retrieves the cooldown period between flights.</li>
 * <li>{@link #setFlightCooldown(LivingEntity, int)} - Sets the cooldown period between flights.</li>
 * <li>{@link #getAltitude(LivingEntity)} - Retrieves the current altitude of the entity.</li>
 * </ul>
 *
 * @author Kyradjis
 * @version 1.7.0
 * @see EntityStateManager
 * @see LivingEntity
 * @since 1.7.0
 */
@SuppressWarnings("unused")
public interface IFlyingEntity {

    /**
     * Retrieves the flying state of the entity.
     * <p>
     * Purpose: Indicates whether the entity is currently flying.<br>
     * When: Called to check the entity's current flight status.<br>
     * Where: Used in interaction logic for flying behavior.<br>
     * Additional Info: The library does not enforce the flying state; it is up to the developer to manage the flying state.<br>
     * </p>
     *
     * @return {@code true} if the entity is flying; {@code false} otherwise.
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean getFlyingState(LivingEntity pEntity) {
        return EntityStateManager.getFlyingState(pEntity);
    }

    /**
     * Sets the flying state of the entity.
     * <p>
     * Purpose: Updates the entity's state to indicate whether it is flying.<br>
     * When: Invoked during gameplay or AI behavior changes.<br>
     * Where: Used in methods controlling flight mechanics.<br>
     * Additional Info: The library does not enforce the flying state; it is up to the developer to manage the flying state.<br>
     * </p>
     *
     * @param pFlying {@code true} to set the entity as flying; {@code false} otherwise.
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setFlyingState(LivingEntity pEntity, boolean pFlying) {
        EntityStateManager.setFlyingState(pEntity, pFlying);
    }

    /**
     * Retrieves the flying speed multiplier for the entity.
     * <p>
     * Purpose: Determines the speed multiplier applied when the entity is flying.<br>
     * When: Called during movement calculations for flying entities.<br>
     * Where: Used in movement logic.<br>
     * </p>
     *
     * @param pEntity The entity for which to retrieve the flying speed multiplier.
     * @return The flying speed multiplier as a {@code double}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default double getFlyingSpeedMultiplier(LivingEntity pEntity) {
        return pEntity.getAttributeValue(Attributes.FLYING_SPEED);
    }

    /**
     * Sets the flying speed multiplier for the entity.
     * <p>
     * Purpose: Updates the speed multiplier applied when the entity is flying.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in methods controlling movement mechanics.<br>
     * </p>
     *
     * @param pEntity          The entity for which to set the flying speed multiplier.
     * @param pSpeedMultiplier The flying speed multiplier as a {@code double}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setFlyingSpeedMultiplier(LivingEntity pEntity, double pSpeedMultiplier) {
        AttributeInstance flyingSpeedAttribute = pEntity.getAttribute(Attributes.FLYING_SPEED);
        if (flyingSpeedAttribute != null) {
            flyingSpeedAttribute.setBaseValue(pSpeedMultiplier);
        } else {
            BaseLogger.log(BaseLogLevel.ERROR, pEntity + " does not have a flying speed attribute.", true);
            throw new IllegalStateException(pEntity + " does not have a flying speed attribute.");
        }
    }

    /**
     * Checks if the entity is capable of flight.
     * <p>
     * Purpose: Determines whether the entity has the ability to fly.<br>
     * When: Called to validate the entity's flight capabilities.<br>
     * Where: Used in AI or interaction logic involving flight.<br>
     * Additional Info: The library does not enforce the flying ability; it is up to the developer to manage the flying ability.<br>
     * </p>
     *
     * @return {@code true} if the entity can fly; {@code false} otherwise (e.g., when the entity is a baby).
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean canFly(LivingEntity pEntity) {
        return EntityStateManager.getCanFly(pEntity);
    }

    /**
     * Sets whether the entity can fly.
     * <p>
     * Purpose: Updates the entity's ability to fly.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in methods controlling flight mechanics.<br>
     * Additional Info: The library does not enforce the flying ability; it is up to the developer to manage the flying ability.<br>
     * </p>
     *
     * @param pCanFly {@code true} to enable flight for the entity; {@code false} otherwise.
     * @author MeAlam
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void canFly(LivingEntity pEntity, boolean pCanFly) {
        EntityStateManager.setCanFly(pEntity, pCanFly);
    }

    /**
     * Retrieves the cooldown period between flights for the entity.
     * <p>
     * Purpose: Indicates the amount of time (in seconds) the entity must wait before flying again.<br>
     * When: Called during flight cooldown checks.<br>
     * Where: Used in gameplay mechanics.<br>
     * Additional Info: The library does not enforce the cooldown period; it is up to the developer to manage flight cooldowns.<br>
     * </p>
     *
     * @return The flight cooldown period (in seconds) as an {@code int}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default int getFlightCooldown(LivingEntity pEntity) {
        return EntityStateManager.getFlyingCooldown(pEntity);
    }

    /**
     * Sets the cooldown period between flights for the entity.
     * <p>
     * Purpose: Updates the amount of time (in seconds) the entity must wait before flying again.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in cooldown management logic.<br>
     * Additional Info: The library does not enforce the cooldown period; it is up to the developer to manage flight cooldowns.<br>
     * </p>
     *
     * @param pFlightCooldown The flight cooldown period (in seconds) as an {@code int}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setFlightCooldown(LivingEntity pEntity, int pFlightCooldown) {
        EntityStateManager.setFlyingCooldown(pEntity, pFlightCooldown);
    }

    /**
     * Retrieves the current altitude of the entity.
     * <p>
     * Purpose: Indicates the current height of the entity above the ground (measured in blocks).<br>
     * When: Called during calculations or checks involving altitude.<br>
     * Where: Used in AI and movement logic.<br>
     * </p>
     *
     * @return The current altitude (in blocks) of the entity as an {@code int}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default int getAltitude(LivingEntity pEntity) {
        BlockPos blockPos = pEntity.getOnPos();
        return blockPos.getY();
    }
}
