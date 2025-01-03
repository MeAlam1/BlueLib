// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.interfaces.entity;

import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import software.bluelib.entity.EntityStateManager;

/**
 * Interface for managing tamable entities in the BlueLib library.
 * <p>
 * Purpose: This interface defines the behaviors and properties for tamable entities, including taming, ownership, loyalty, and interaction mechanics.<br>
 * When: These methods are invoked during gameplay interactions, AI logic, or custom mechanics involving tamable entities.<br>
 * Where: Implemented by classes that manage tamable entities and their associated mechanics.<br>
 * Additional Info: Utilizes {@link EntityStateManager} for state management and {@link LivingEntity} for entity operations.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #isTamed(OwnableEntity)} - Checks if the provided entity is tamed.</li>
 * <li>{@link #ownedBy(OwnableEntity, Player)} - Checks if a specific player owns the entity.</li>
 * <li>{@link #getTamingItem(LivingEntity)} - Retrieves the taming item required for the entity.</li>
 * <li>{@link #setTamingItem(LivingEntity, String)} - Sets the taming item for the entity.</li>
 * <li>{@link #getFollowingStatus(LivingEntity)} - Checks if the entity is following its owner.</li>
 * <li>{@link #setFollowingStatus(LivingEntity, boolean)} - Sets the following status of the entity.</li>
 * <li>{@link #getLoyaltyLevel(LivingEntity)} - Retrieves the loyalty level of the entity.</li>
 * <li>{@link #setLoyaltyLevel(LivingEntity, int)} - Sets the loyalty level of the entity.</li>
 * </ul>
 *
 * @author Kyradjis
 * @version 1.7.0
 * @see EntityStateManager
 * @see LivingEntity
 * @see Player
 * @since 1.7.0
 */

@SuppressWarnings("unused")
public interface ITamableEntity {

    /**
     * Checks if the provided entity is tamed.
     * <p>
     * Purpose: Determines whether the specified {@link LivingEntity} is tamed.<br>
     * When: Invoked during interactions or checks involving the entity's taming status.<br>
     * Where: Used in gameplay mechanics or AI logic requiring taming validation.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} to check.
     * @return {@code true} if the entity is tamed; {@code false} otherwise.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean isTamed(OwnableEntity pEntity) {
        return pEntity.getOwner() != null;
    }

    /**
     * Checks if a specific player owns this entity.
     * <p>
     * Purpose: Verifies ownership by comparing the player's UUID with the entity's owner UUID.<br>
     * When: Triggered during interactions where ownership affects behavior.<br>
     * Where: Used in methods that require ownership validation.<br>
     * </p>
     *
     * @param pEntity The entity to check ownership for.
     * @param pPlayer The player to check ownership for.
     * @return {@code true} if the player owns the entity; {@code false} otherwise.
     * @author Kyradjis
     * @see Player#getUUID()
     * @since 1.7.0
     */
    default boolean isOwnedBy(OwnableEntity pEntity, Player pPlayer) {
        UUID ownerUUID = pEntity.getOwnerUUID();
        return ownerUUID != null && ownerUUID.equals(pPlayer.getUUID());
    }

    /**
     * Retrieves the taming item associated with the specified entity.
     * <p>
     * Purpose: Returns the item required to tame the {@link LivingEntity}.<br>
     * When: Called during interactions or checks related to taming mechanics.<br>
     * Where: Used in gameplay systems that validate or enforce taming requirements.<br>
     * Additional Info: The library does not enforce the Taming Items; it is up to the developer to manage the Taming Items.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose taming item is to be retrieved.
     * @return The name of the taming item as a {@link String}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default Item getTamingItem(LivingEntity pEntity) {
        return EntityStateManager.getTamingItem(pEntity);
    }

    /**
     * Sets the taming item for the specified entity.
     * <p>
     * Purpose: Updates the item required to tame the {@link LivingEntity}.<br>
     * When: Invoked during setup or configuration of taming mechanics.<br>
     * Where: Used to modify the taming requirements for an entity.<br>
     * Additional Info: The library does not enforce the Taming Items; it is up to the developer to manage the Taming Items.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose taming item is to be set.
     * @param pItem   The name of the taming item as a {@link String}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @see String
     * @since 1.7.0
     */
    default void setTamingItem(LivingEntity pEntity, Item pItem) {
        EntityStateManager.setTamingItem(pEntity, pItem);
    }

    /**
     * Retrieves the following status of the specified entity.
     * <p>
     * Purpose: Checks whether the {@link LivingEntity} is currently following its owner.<br>
     * When: Called during interactions or gameplay events that depend on following behavior.<br>
     * Where: Used in AI and movement logic for tamable entities.<br>
     * Additional Info: The library does not enforce the Following; it is up to the developer to manage the following logic.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} to check.
     * @return {@code true} if the entity is following its owner; {@code false} otherwise.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default boolean getFollowingStatus(LivingEntity pEntity) {
        return EntityStateManager.getFollowingState(pEntity);
    }

    /**
     * Sets the following status for the specified entity.
     * <p>
     * Purpose: Updates the {@link LivingEntity}'s behavior to indicate whether it should follow its owner.<br>
     * When: Called during interactions or commands that modify following behavior.<br>
     * Where: Used in AI and movement mechanics for tamable entities.<br>
     * Additional Info: The library does not enforce the Following; it is up to the developer to manage the following logic.<br>
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose following status is to be updated.
     * @param pStatus {@code true} to make the entity follow its owner; {@code false} otherwise.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default void setFollowingStatus(LivingEntity pEntity, boolean pStatus) {
        EntityStateManager.setFollowingState(pEntity, pStatus);
    }

    /**
     * Gets the loyalty level of the entity.
     * <p>
     * Purpose: Retrieves the loyalty level, which determines the entity's level of loyalty to the owner (e.g., decreases if ignored or mistreated)<br>
     * Where: Used in loyalty-based mechanics.<br>
     * Additional Info: The library does not enforce the Loyalty System; it is up to the developer to manage the loyalty logic.<br>
     * </p>
     *
     * @param pEntity The entity for which to get the loyalty level.
     * @return The loyalty level as an {@code int}.
     * @author Kyradjis
     * @see EntityStateManager
     * @see LivingEntity
     * @since 1.7.0
     */
    default int getLoyaltyLevel(LivingEntity pEntity) {
        return EntityStateManager.getLoyaltyLevel(pEntity);
    }

    /**
     * Sets the loyalty level of the entity.
     * <p>
     * Purpose: Updates the loyalty level of the entity.<br>
     * When: Called during interactions or state updates.<br>
     * Where: Used to adjust loyalty-based behavior.<br>
     * Additional Info: The library does not enforce the Loyalty System; it is up to the developer to manage the loyalty logic.<br>
     * </p>
     *
     * @param pEntity The entity for which to set the loyalty level.
     * @param pLevel  The loyalty level as an {@code int}.
     * @author Kyradjis
     * @see LivingEntity
     * @see EntityStateManager
     * @since 1.7.0
     */
    default void setLoyaltyLevel(LivingEntity pEntity, int pLevel) {
        EntityStateManager.setLoyaltyLevel(pEntity, pLevel);
    }
}
