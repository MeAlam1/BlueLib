// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.api.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class EntityStateManager {

    // Flying States

    private static final Map<LivingEntity, Boolean> flyingStateMap = new HashMap<>();

    public static boolean getFlyingState(LivingEntity pEntity) {
        return flyingStateMap.getOrDefault(pEntity, false);
    }

    public static void setFlyingState(LivingEntity pEntity, boolean pState) {
        flyingStateMap.put(pEntity, pState);
    }

    // Can Fly

    private static final Map<LivingEntity, Boolean> canFlyMap = new HashMap<>();

    public static Boolean getCanFly(LivingEntity pEntity) {
        return canFlyMap.getOrDefault(pEntity, true);
    }

    public static void setCanFly(LivingEntity pEntity, boolean pCanFly) {
        canFlyMap.put(pEntity, pCanFly);
    }

    // Flight Cooldowns

    private static final Map<LivingEntity, Integer> flyingCooldownMap = new HashMap<>();

    public static int getFlyingCooldown(LivingEntity pEntity) {
        return flyingCooldownMap.getOrDefault(pEntity, 1);
    }

    public static void setFlyingCooldown(LivingEntity pEntity, int pCooldown) {
        flyingCooldownMap.put(pEntity, pCooldown);
    }

    // Swimming States

    private static final Map<LivingEntity, Boolean> swimmingStateMap = new HashMap<>();

    public static boolean getSwimmingState(LivingEntity pEntity) {
        return swimmingStateMap.getOrDefault(pEntity, false);
    }

    public static void setSwimmingState(LivingEntity pEntity, boolean pState) {
        swimmingStateMap.put(pEntity, pState);
    }

    private static final Map<LivingEntity, Boolean> canSwimMap = new HashMap<>();

    public static Boolean getCanSwim(LivingEntity pEntity) {
        return canSwimMap.getOrDefault(pEntity, true);
    }

    public static void setCanSwim(LivingEntity pEntity, boolean pCanFly) {
        canSwimMap.put(pEntity, pCanFly);
    }

    // Swimming Cooldowns

    private static final Map<LivingEntity, Integer> swimmingCooldownMap = new HashMap<>();

    public static int getSwimmingCooldown(LivingEntity pEntity) {
        return swimmingCooldownMap.getOrDefault(pEntity, 1);
    }

    public static void setSwimmingCooldown(LivingEntity pEntity, int pCooldown) {
        swimmingCooldownMap.put(pEntity, pCooldown);
    }

    // Taming Item

    private static final Map<LivingEntity, List<Item>> tamingItemMap = new HashMap<>();

    public static List<Item> getTamingItems(LivingEntity pEntity) {
        return tamingItemMap.getOrDefault(pEntity, new ArrayList<>());
    }

    @Nullable
    public static Item getSpecificTamingItem(LivingEntity pEntity, Item pItem) {
        List<Item> items = tamingItemMap.getOrDefault(pEntity, new ArrayList<>());
        if (items.isEmpty()) {
            return null;
        }
        for (Item item : items) {
            if (item == pItem) {
                return item;
            }
        }
        return null;
    }

    public static void setTamingItems(LivingEntity pEntity, List<Item> pItem) {
        tamingItemMap.put(pEntity, pItem);
    }

    public static void addTamingItem(LivingEntity pEntity, Item pItem) {
        List<Item> items = tamingItemMap.getOrDefault(pEntity, new ArrayList<>());
        items.add(pItem);
        tamingItemMap.put(pEntity, items);
    }

    // Owner Following

    private static final Map<LivingEntity, Boolean> ownerFollowingStateMap = new HashMap<>();

    public static boolean getFollowingState(LivingEntity pEntity) {
        return ownerFollowingStateMap.getOrDefault(pEntity, false);
    }

    public static void setFollowingState(LivingEntity pEntity, boolean pState) {
        ownerFollowingStateMap.put(pEntity, pState);
    }

    // Loyalty Level

    private static final Map<LivingEntity, Integer> loyaltyLevelMap = new HashMap<>();

    public static int getLoyaltyLevel(LivingEntity pEntity) {
        return loyaltyLevelMap.getOrDefault(pEntity, 5);
    }

    public static void setLoyaltyLevel(LivingEntity pEntity, int pLevel) {
        loyaltyLevelMap.put(pEntity, pLevel);
    }
}
