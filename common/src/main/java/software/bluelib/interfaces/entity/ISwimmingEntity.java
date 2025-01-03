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
 * Interface for managing swimming-related behaviors and properties of entities in the BlueLib library.
 * <p>
 * Purpose: This interface defines methods to manage the swimming state, speed, cooldown, and depth of entities.<br>
 * When: These methods are invoked during gameplay interactions, AI logic, or custom mechanics involving swimming entities.<br>
 * Where: Implemented by classes that manage entities with swimming capabilities.<br>
 * Additional Info: Utilizes {@link EntityStateManager} for managing states and attributes, and {@link Attributes} for swimming-related performance.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #getSwimmingState(LivingEntity)} - Checks if the entity is currently swimming.</li>
 * <li>{@link #setSwimmingState(LivingEntity, boolean)} - Updates the swimming state of the entity.</li>
 * <li>{@link #getSwimmingSpeedMultiplier(LivingEntity)} - Retrieves the swimming speed multiplier of the entity.</li>
 * <li>{@link #setSwimmingSpeedMultiplier(LivingEntity, double)} - Updates the swimming speed multiplier of the entity.</li>
 * <li>{@link #getSwimmingCooldown(LivingEntity)} - Retrieves the cooldown period for the entity's swimming behavior.</li>
 * <li>{@link #setSwimmingCooldown(LivingEntity, int)} - Updates the cooldown period for the entity's swimming behavior.</li>
 * <li>{@link #getDepth(LivingEntity)} - Retrieves the current depth (Y-coordinate) of the entity.</li>
 * </ul>
 *
 * @author Kyradjis
 * @see EntityStateManager
 * @see Attributes
 * @see LivingEntity
 * @since 1.7.0
 */
@SuppressWarnings("unused")
public interface ISwimmingEntity {

    /**
     * Checks if the specified entity is currently swimming.
     * <p>
     * Purpose: Determines whether the {@link LivingEntity} is in a swimming state.<br>
     * When: Invoked during interactions or checks involving swimming mechanics.<br>
     * Where: Used in AI and gameplay logic requiring swimming state validation.<br>
     * Additional Info: The library does not enforce the swimming state; it is up to the developer to manage the swimming state.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} to check.
     * @return {@code true} if the entity is swimming; {@code false} otherwise.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean getSwimmingState(LivingEntity pEntity) {
        return EntityStateManager.getSwimmingState(pEntity);
    }

    /**
     * Updates the swimming state of the specified entity.
     * <p>
     * Purpose: Sets whether the {@link LivingEntity} is swimming.<br>
     * When: Called during gameplay events or interactions that affect swimming behavior.<br>
     * Where: Used to control swimming-related mechanics for entities.<br>
     * Additional Info: The library does not enforce the swimming state; it is up to the developer to manage the swimming state.<br>
     * </p>
     *
     * @param pEntity   The {@link LivingEntity} whose swimming state is to be updated.
     * @param pSwimming {@code true} to set the entity as swimming; {@code false} otherwise.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setSwimmingState(LivingEntity pEntity, boolean pSwimming) {
        EntityStateManager.setSwimmingState(pEntity, pSwimming);
    }

    /**
     * Retrieves the swimming speed multiplier of the specified entity.
     * <p>
     * Purpose: Returns the multiplier that affects the entity's speed while swimming.<br>
     * When: Called during calculations or checks involving swimming speed.<br>
     * Where: Used in AI or movement mechanics for swimming entities.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} to check.
     * @return The swimming speed multiplier as a {@code double} value. Defaults to 1.0 if undefined or unavailable.
     * @author Kyradjis
     * @see Attributes
     * @see LivingEntity
     * @since 1.7.0
     */
    default double getSwimmingSpeedMultiplier(LivingEntity pEntity) {
        return pEntity.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
    }

    /**
     * Updates the swimming speed multiplier for the specified entity.
     * <p>
     * Purpose: Sets the multiplier that affects the entity's swimming speed.<br>
     * When: Invoked during gameplay events or interactions that modify swimming performance.<br>
     * Where: Used in AI or movement mechanics for swimming entities.<br>
     * Additional Info: Throws an {@link IllegalStateException} if the entity lacks the required attribute.<br>
     * </p>
     *
     * @param pEntity          The {@link LivingEntity} whose swimming speed multiplier is to be updated.
     * @param pSpeedMultiplier The swimming speed multiplier as a {@code double}.
     * @throws IllegalStateException If the entity does not have the swimming speed attribute.
     * @author Kyradjis
     * @see Attributes
     * @see BaseLogger
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setSwimmingSpeedMultiplier(LivingEntity pEntity, double pSpeedMultiplier) {
        AttributeInstance swimmingSpeedAttribute = pEntity.getAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY);
        if (swimmingSpeedAttribute != null) {
            swimmingSpeedAttribute.setBaseValue(pSpeedMultiplier);
        } else {
            BaseLogger.log(BaseLogLevel.ERROR, pEntity + " does not have a swimming speed attribute.", true);
            throw new IllegalStateException(pEntity + " does not have a swimming speed attribute.");
        }
    }

    /**
     * Checks if the entity is capable of swimming.
     * <p>
     * Purpose: Determines whether the entity has the ability to swim.<br>
     * When: Called to validate the entity's swimming capabilities.<br>
     * Where: Used in AI or interaction logic involving swimming.<br>
     * Additional Info: The library does not enforce the swimming ability; it is up to the developer to manage the swimming abilities.<br>
     * </p>
     *
     * @return {@code true} if the entity can swim; {@code false} otherwise (e.g., when the entity is a baby).
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean canSwim(LivingEntity pEntity) {
        return EntityStateManager.getCanSwim(pEntity);
    }

    /**
     * Sets whether the entity can swim.
     * <p>
     * Purpose: Updates the entity's ability to swim.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in methods controlling swimming mechanics.<br>
     * Additional Info: The library does not enforce the swimming ability; it is up to the developer to manage the swimming abilities.<br>
     * </p>
     *
     * @param pCanSwim {@code true} to enable swimming for the entity; {@code false} otherwise.
     * @author MeAlam
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void canSwim(LivingEntity pEntity, boolean pCanSwim) {
        EntityStateManager.setCanSwim(pEntity, pCanSwim);
    }

    /**
     * Retrieves the cooldown period for the entity's swimming behavior.
     * <p>
     * Purpose: Returns the cooldown duration that limits the entity's ability to swim.<br>
     * When: Invoked during checks or calculations involving swimming cooldowns.<br>
     * Where: Used in gameplay mechanics that manage swimming intervals.<br>
     * Additional Info: The library does not enforce the Cooldown; it is up to the developer to manage the swimming cooldown.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} to check.
     * @return The swimming cooldown period as an {@code int} value. Defaults to 0 if undefined.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default int getSwimmingCooldown(LivingEntity pEntity) {
        return EntityStateManager.getSwimmingCooldown(pEntity);
    }

    /**
     * Updates the cooldown period for the entity's swimming behavior.
     * <p>
     * Purpose: Sets the cooldown duration that limits the entity's ability to swim.<br>
     * When: Called during interactions or events that modify swimming behavior.<br>
     * Where: Used to control swimming intervals for gameplay mechanics.<br>
     * Additional Info: The library does not enforce the Cooldown; it is up to the developer to manage the swimming cooldown.<br>
     * </p>
     *
     * @param pEntity       The {@link LivingEntity} whose swimming cooldown period is to be updated.
     * @param pSwimCooldown The swimming cooldown period as an {@code int}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setSwimmingCooldown(LivingEntity pEntity, int pSwimCooldown) {
        EntityStateManager.setSwimmingCooldown(pEntity, pSwimCooldown);
    }

    /**
     * Retrieves the current depth (Y-coordinate) of the specified entity.
     * <p>
     * Purpose: Determines the vertical position of the {@link LivingEntity} relative to the game world.<br>
     * When: Invoked during checks or calculations involving entity depth.<br>
     * Where: Used in AI or gameplay mechanics that depend on the entity's altitude.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose depth is to be retrieved.
     * @return The depth of the entity as an {@code int} value, representing the Y-coordinate.
     * @author Kyradjis
     * @see BlockPos
     * @see LivingEntity
     * @since 1.7.0
     */
    default int getDepth(LivingEntity pEntity) {
        BlockPos blockPos = pEntity.getOnPos();
        return blockPos.getY();
    }
}
