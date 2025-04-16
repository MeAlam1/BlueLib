// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.interfaces.entity;

import java.util.List;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import software.bluelib.entity.EntityStateManager;

@SuppressWarnings("unused")
public interface ITamableEntity {

    default boolean isTamed(OwnableEntity pEntity) {
        return pEntity.getOwner() != null;
    }

    default boolean isOwnedBy(OwnableEntity pEntity, Player pPlayer) {
        UUID ownerUUID = pEntity.getOwnerUUID();
        return ownerUUID != null && ownerUUID.equals(pPlayer.getUUID());
    }

    default List<Item> getTamingItems(LivingEntity pEntity) {
        return EntityStateManager.getTamingItems(pEntity);
    }

    default Item getSpecificTamingItem(LivingEntity pEntity, Item pItem) {
        return EntityStateManager.getSpecificTamingItem(pEntity, pItem);
    }

    default void setTamingItems(LivingEntity pEntity, List<Item> pItem) {
        EntityStateManager.setTamingItems(pEntity, pItem);
    }

    default void addTamingItem(LivingEntity pEntity, Item pItem) {
        EntityStateManager.addTamingItem(pEntity, pItem);
    }

    default boolean getFollowingStatus(LivingEntity pEntity) {
        return EntityStateManager.getFollowingState(pEntity);
    }

    default void setFollowingStatus(LivingEntity pEntity, boolean pStatus) {
        EntityStateManager.setFollowingState(pEntity, pStatus);
    }

    default int getLoyaltyLevel(LivingEntity pEntity) {
        return EntityStateManager.getLoyaltyLevel(pEntity);
    }

    default void setLoyaltyLevel(LivingEntity pEntity, int pLevel) {
        EntityStateManager.setLoyaltyLevel(pEntity, pLevel);
    }
}
