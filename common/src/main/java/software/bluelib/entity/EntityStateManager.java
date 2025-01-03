// Copyright (c) BlueLib. Licensed under the MIT License.
package software.bluelib.entity;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import software.bluelib.interfaces.entity.ITamableEntity;

/**
 * Manages the state of entities in the BlueLib library.
 * <p>
 * Purpose: This class provides methods and data structures to manage various aspects of entity behavior and mechanics, including flying, swimming and taming.<br>
 * When: These methods are invoked during interactions or gameplay mechanics for entities.<br>
 * Where: This class is used by other classes to manage entity states.<br>
 * Additional Info: The class uses maps to store and retrieve the state information for entities.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #getFlyingState(LivingEntity)} - Retrieves the flying state of the entity.</li>
 * <li>{@link #setFlyingState(LivingEntity, boolean)} - Sets the flying state of the entity.</li>
 * <li>{@link #getCanFly(LivingEntity)} - Checks if the entity is capable of flight.</li>
 * <li>{@link #setCanFly(LivingEntity, boolean)} - Sets whether the entity can fly.</li>
 * <li>{@link #getFlyingCooldown(LivingEntity)} - Retrieves the cooldown period between flights for the entity.</li>
 * <li>{@link #setFlyingCooldown(LivingEntity, int)} - Sets the cooldown period between flights for the entity.</li>
 * <li>{@link #getSwimmingState(LivingEntity)} - Retrieves the swimming state of the entity.</li>
 * <li>{@link #setSwimmingState(LivingEntity, boolean)} - Updates the swimming state of the entity.</li>
 * <li>{@link #getSwimmingCooldown(LivingEntity)} - Retrieves the swimming cooldown period of the entity.</li>
 * <li>{@link #setSwimmingCooldown(LivingEntity, int)} - Updates the swimming cooldown period of the entity.</li>
 * <li>{@link #getTamingItem(LivingEntity)} - Retrieves the taming item associated with the entity.</li>
 * <li>{@link #setTamingItem(LivingEntity, Item)} - Updates the taming item for the entity.</li>
 * <li>{@link #getFollowingState(LivingEntity)} - Retrieves the following state of the entity.</li>
 * <li>{@link #setFollowingState(LivingEntity, boolean)} - Updates the following state of the entity.</li>
 * <li>{@link #getLoyaltyLevel(LivingEntity)} - Retrieves the loyalty level of the entity.</li>
 * <li>{@link #setLoyaltyLevel(LivingEntity, int)} - Updates the loyalty level of the entity.</li>
 * </ul>
 *
 * @author Kyradjis, MeAlam
 * @version 1.7.0
 * @see software.bluelib.interfaces.entity.IFlyingEntity
 * @see software.bluelib.interfaces.entity.ISwimmingEntity
 * @see ITamableEntity
 * @see LivingEntity
 * @see Boolean
 * @see HashMap
 * @see Map
 * @see Integer
 * @since 1.7.0
 */

public class EntityStateManager {

    // Flying States

    /**
     * A map to store the flying state of entities.
     * <p>
     * Purpose: This map holds the flying state of entities.<br>
     * When: The map is populated when the {@link #setFlyingState(LivingEntity, boolean)} method is called.<br>
     * Where: The map is used to store the flying state of entities.<br>
     * Additional Info: The keys are the entities and the values are their flying states.
     * </p>
     *
     * @see Map
     * @see HashMap
     * @see LivingEntity
     * @see Boolean
     * @see #setFlyingState(LivingEntity, boolean)
     * @see #getFlyingState(LivingEntity)
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Boolean> flyingStateMap = new HashMap<>();

    /**
     * Retrieves the flying state of the entity.
     * <p>
     * Purpose: Indicates whether the entity is currently flying.<br>
     * When: Called to check the entity's current flight status.<br>
     * Where: Used in interaction logic for flying behavior.<br>
     * </p>
     *
     * @param pEntity The entity to check the flying state for.
     * @return {@code true} if the entity is flying; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #setFlyingState(LivingEntity, boolean)
     * @see #flyingStateMap
     * @since 1.7.0
     */
    public static boolean getFlyingState(LivingEntity pEntity) {
        return flyingStateMap.getOrDefault(pEntity, false);
    }

    /**
     * Sets the flying state of the entity.
     * <p>
     * Purpose: Updates the entity's state to indicate whether it is flying.<br>
     * When: Invoked during gameplay or AI behavior changes.<br>
     * Where: Used in methods controlling flight mechanics.<br>
     * </p>
     *
     * @param pEntity The entity to set the flying state for.
     * @param pState  {@code true} to set the entity as flying; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #getFlyingState(LivingEntity)
     * @see #flyingStateMap
     * @since 1.7.0
     */
    public static void setFlyingState(LivingEntity pEntity, boolean pState) {
        flyingStateMap.put(pEntity, pState);
    }

    // Can Fly

    /**
     * A map to store the flying capability of entities.
     * <p>
     * Purpose: This map holds the flying capability of entities.<br>
     * When: The map is populated when the {@link #setCanFly(LivingEntity, boolean)} method is called.<br>
     * Where: The map is used to store the flying capability of entities.<br>
     * Additional Info: The keys are the entities and the values are their flying capabilities.
     * </p>
     *
     * @see Map
     * @see HashMap
     * @see LivingEntity
     * @see Boolean
     * @see #setCanFly(LivingEntity, boolean)
     * @see #getCanFly(LivingEntity)
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Boolean> canFlyMap = new HashMap<>();

    /**
     * Checks if the entity is capable of flight.
     * <p>
     * Purpose: Determines whether the entity has the ability to fly.<br>
     * When: Called to validate the entity's flight capabilities.<br>
     * Where: Used in AI or interaction logic involving flight.<br>
     * </p>
     *
     * @param pEntity The entity to check the flying capability for.
     * @return {@code true} if the entity can fly; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #setCanFly(LivingEntity, boolean)
     * @see #canFlyMap
     * @since 1.7.0
     */
    public static Boolean getCanFly(LivingEntity pEntity) {
        return canFlyMap.getOrDefault(pEntity, true);
    }

    /**
     * Sets whether the entity can fly.
     * <p>
     * Purpose: Updates the entity's ability to fly.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in methods controlling flight mechanics.<br>
     * </p>
     *
     * @param pEntity The entity to set the flying capability for.
     * @param pCanFly {@code true} to enable flight for the entity; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #getCanFly(LivingEntity)
     * @see #canFlyMap
     * @since 1.7.0
     */
    public static void setCanFly(LivingEntity pEntity, boolean pCanFly) {
        canFlyMap.put(pEntity, pCanFly);
    }

    // Flight Cooldowns

    /**
     * A map to store the flight cooldowns of entities.
     * <p>
     * Purpose: This map holds the flight cooldowns of entities.<br>
     * When: The map is populated when the {@link #setFlyingCooldown(LivingEntity, int)} method is called.<br>
     * Where: The map is used to store the flight cooldowns of entities.<br>
     * Additional Info: The keys are the entities and the values are their flight cooldowns.
     * </p>
     *
     * @see Map
     * @see HashMap
     * @see LivingEntity
     * @see Integer
     * @see #setFlyingCooldown(LivingEntity, int)
     * @see #getFlyingCooldown(LivingEntity)
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Integer> flyingCooldownMap = new HashMap<>();

    /**
     * Retrieves the cooldown period between flights for the entity.
     * <p>
     * Purpose: Indicates the amount of time (in seconds) the entity must wait before flying again.<br>
     * When: Called during flight cooldown checks. (Up to the Developer)<br>
     * Where: Used in {@link software.bluelib.interfaces.entity.IFlyingEntity}.<br>
     * Additional Info: The library does not enforce the cooldown period; it is up to the developer to manage flight cooldowns.<br>
     * </p>
     *
     * @param pEntity The entity to check the flight cooldown for.
     * @return The flight cooldown period (in seconds) as an {@code int}.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see #setFlyingCooldown(LivingEntity, int)
     * @see #flyingCooldownMap
     * @since 1.7.0
     */
    public static int getFlyingCooldown(LivingEntity pEntity) {
        return flyingCooldownMap.getOrDefault(pEntity, 1);
    }

    /**
     * Sets the cooldown period between flights for the entity.
     * <p>
     * Purpose: Updates the amount of time (in seconds) the entity must wait before flying again.<br>
     * When: Invoked during gameplay or entity configuration. (Up to the Developer)<br>
     * Where: Used in {@link software.bluelib.interfaces.entity.IFlyingEntity}.<br>
     * Additional Info: The library does not enforce the cooldown period; it is up to the developer to manage flight cooldowns.<br>
     * </p>
     *
     * @param pEntity   The entity to set the flight cooldown for.
     * @param pCooldown The flight cooldown period (in seconds) as an {@code int}.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see #getFlyingCooldown(LivingEntity)
     * @see #flyingCooldownMap
     * @since 1.7.0
     */
    public static void setFlyingCooldown(LivingEntity pEntity, int pCooldown) {
        flyingCooldownMap.put(pEntity, pCooldown);
    }

    // Swimming States

    /**
     * A map to store the swimming state of entities.
     * <p>
     * Purpose: This map holds the swimming state of entities, indicating whether they are currently swimming.<br>
     * When: The map is populated or accessed when {@link #getSwimmingState(LivingEntity)} or {@link #setSwimmingState(LivingEntity, boolean)} is invoked.<br>
     * Where: Used to manage and track the swimming state of entities in the system.<br>
     * Additional Info: The keys are {@link LivingEntity} instances, and the values are {@code Boolean} values representing their swimming state.
     * </p>
     *
     * @see LivingEntity
     * @see Boolean
     * @see Map
     * @see HashMap
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Boolean> swimmingStateMap = new HashMap<>();

    /**
     * Retrieves the swimming state of the specified entity.
     * <p>
     * Purpose: Determines whether the given {@link LivingEntity} is currently swimming.<br>
     * When: Invoked during checks or interactions involving the entity's swimming behavior.<br>
     * Where: Used in gameplay mechanics or AI logic requiring validation of swimming state.<br>
     * Additional Info: If the swimming state is not set, the method returns {@code false} by default.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose swimming state is to be retrieved.
     * @return {@code true} if the entity is swimming; {@code false} otherwise.
     * @author Kyradjis
     * @see #swimmingStateMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static boolean getSwimmingState(LivingEntity pEntity) {
        return swimmingStateMap.getOrDefault(pEntity, false);
    }

    /**
     * Sets the swimming state for the specified entity.
     * <p>
     * Purpose: Updates the swimming state of the given {@link LivingEntity}.<br>
     * When: Invoked during gameplay events or interactions that affect the swimming behavior of an entity.<br>
     * Where: Used to control and manage swimming-related mechanics for entities.<br>
     * Additional Info: The swimming state is stored in the {@link #swimmingStateMap}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose swimming state is to be updated.
     * @param pState  {@code true} to set the entity as swimming; {@code false} otherwise.
     * @author Kyradjis
     * @see #swimmingStateMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static void setSwimmingState(LivingEntity pEntity, boolean pState) {
        swimmingStateMap.put(pEntity, pState);
    }

    /**
     * A map to store the swim capability of entities.
     * <p>
     * Purpose: This map holds the swim capability of entities.<br>
     * When: The map is populated when the {@link #setCanSwim(LivingEntity, boolean)} method is called.<br>
     * Where: The map is used to store the swim capability of entities.<br>
     * Additional Info: The keys are the entities and the values are their swim capabilities.
     * </p>
     *
     * @see Map
     * @see HashMap
     * @see LivingEntity
     * @see Boolean
     * @see #setCanSwim(LivingEntity, boolean)
     * @see #getCanSwim(LivingEntity)
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Boolean> canSwimMap = new HashMap<>();

    /**
     * Checks if the entity is capable of swim.
     * <p>
     * Purpose: Determines whether the entity has the ability to swim.<br>
     * When: Called to validate the entity's swim capabilities.<br>
     * Where: Used in AI or interaction logic involving swim.<br>
     * </p>
     *
     * @param pEntity The entity to check the swim capability for.
     * @return {@code true} if the entity can swim; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #setCanSwim(LivingEntity, boolean)
     * @see #canSwimMap
     * @since 1.7.0
     */
    public static Boolean getCanSwim(LivingEntity pEntity) {
        return canSwimMap.getOrDefault(pEntity, true);
    }

    /**
     * Sets whether the entity can swim.
     * <p>
     * Purpose: Updates the entity's ability to swim.<br>
     * When: Invoked during gameplay or entity configuration.<br>
     * Where: Used in methods controlling swim mechanics.<br>
     * </p>
     *
     * @param pEntity The entity to set the swim capability for.
     * @param pCanFly {@code true} to enable swim for the entity; {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.interfaces.entity.IFlyingEntity
     * @see LivingEntity
     * @see Boolean
     * @see #getCanSwim(LivingEntity)
     * @see #canSwimMap
     * @since 1.7.0
     */
    public static void setCanSwim(LivingEntity pEntity, boolean pCanFly) {
        canSwimMap.put(pEntity, pCanFly);
    }

    // Swimming Cooldowns
    /**
     * A map to store the swimming cooldown period of entities.
     * <p>
     * Purpose: This map tracks the cooldown period for entities, limiting how frequently they can swim.<br>
     * When: The map is populated or accessed when {@link #getSwimmingCooldown(LivingEntity)} or {@link #setSwimmingCooldown(LivingEntity, int)} is invoked.<br>
     * Where: Used to manage and enforce swimming cooldown mechanics for entities.<br>
     * Additional Info: The keys are {@link LivingEntity} instances, and the values are {@code Integer} values representing the cooldown period in seconds.
     * </p>
     *
     * @see LivingEntity
     * @see Integer
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Integer> swimmingCooldownMap = new HashMap<>();

    /**
     * Retrieves the swimming cooldown period for the specified entity.
     * <p>
     * Purpose: Returns the cooldown duration that limits how frequently the given {@link LivingEntity} can swim.<br>
     * When: Invoked during checks or calculations involving swimming cooldowns.<br>
     * Where: Used in gameplay systems that manage swimming intervals for entities.<br>
     * Additional Info: If no cooldown is set for the entity, the method returns a default value of {@code 1}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose swimming cooldown is to be retrieved.
     * @return The swimming cooldown period as an {@code int}, representing the duration in seconds.
     * @author Kyradjis
     * @see #swimmingCooldownMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static int getSwimmingCooldown(LivingEntity pEntity) {
        return swimmingCooldownMap.getOrDefault(pEntity, 1);
    }

    /**
     * Sets the swimming cooldown period for the specified entity.
     * <p>
     * Purpose: Updates the cooldown duration that limits how frequently the given {@link LivingEntity} can swim.<br>
     * When: Called during interactions or gameplay events that modify the swimming cooldown of an entity.<br>
     * Where: Used to enforce or adjust swimming intervals in gameplay mechanics.<br>
     * Additional Info: The cooldown period is stored in the {@link #swimmingCooldownMap}.
     * </p>
     *
     * @param pEntity   The {@link LivingEntity} whose swimming cooldown is to be set.
     * @param pCooldown The swimming cooldown period as an {@code int}, representing the duration in seconds.
     * @author Kyradjis
     * @see #swimmingCooldownMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static void setSwimmingCooldown(LivingEntity pEntity, int pCooldown) {
        swimmingCooldownMap.put(pEntity, pCooldown);
    }

    // Taming Item

    /**
     * A map to store the taming item associated with entities.
     * <p>
     * Purpose: This map tracks the specific taming item required for each {@link LivingEntity}.<br>
     * When: The map is populated or accessed when {@link #getTamingItem(LivingEntity)} or {@link #setTamingItem(LivingEntity, Item)} is invoked.<br>
     * Where: Used to manage and validate taming mechanics based on specific items.<br>
     * Additional Info: The keys are {@link LivingEntity} instances, and the values are the taming items.
     * </p>
     *
     * @see LivingEntity
     * @see String
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Item> tamingItemMap = new HashMap<>();

    /**
     * Retrieves the taming item associated with the specified entity.
     * <p>
     * Purpose: Returns the {@link String} representation of the item required to tame the given {@link LivingEntity}.<br>
     * When: Invoked during interactions or checks that require validation of the entity's taming item.<br>
     * Where: Used in taming mechanics or gameplay systems that enforce item-based taming.<br>
     * Additional Info: If no taming item is set for the entity, the method returns {@code null}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose taming item is to be retrieved.
     * @return The taming item.
     * @author Kyradjis
     * @see #tamingItemMap
     * @see LivingEntity
     * @see String
     * @since 1.7.0
     */
    public static Item getTamingItem(LivingEntity pEntity) {
        return tamingItemMap.getOrDefault(pEntity, null);
    }

    /**
     * Sets the taming item for the specified entity.
     * <p>
     * Purpose: Updates the item required to tame the given {@link LivingEntity}.<br>
     * When: Called during interactions or events that define or modify the taming requirements for an entity.<br>
     * Where: Used to manage taming mechanics based on specific items.<br>
     * Additional Info: The taming item is stored in the {@link #tamingItemMap}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose taming item is to be set.
     * @param pItem   The taming item.
     * @author Kyradjis
     * @see #tamingItemMap
     * @see LivingEntity
     * @see String
     * @since 1.7.0
     */
    public static void setTamingItem(LivingEntity pEntity, Item pItem) {
        tamingItemMap.put(pEntity, pItem);
    }

    // Owner Following

    /**
     * A map to store the owner-following state of entities.
     * <p>
     * Purpose: This map tracks whether entities are currently following their owner.<br>
     * When: The map is populated or accessed when {@link #getFollowingState(LivingEntity)} is invoked.<br>
     * Where: Used to manage and validate entity behavior related to following their owner.<br>
     * Additional Info: The keys are {@link LivingEntity} instances, and the values are {@code Boolean} values representing their following state.
     * </p>
     *
     * @see LivingEntity
     * @see Boolean
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Boolean> ownerFollowingStateMap = new HashMap<>();

    /**
     * Retrieves the owner-following state of the specified entity.
     * <p>
     * Purpose: Determines whether the given {@link LivingEntity} is currently following its owner.<br>
     * When: Invoked during interactions or gameplay checks involving the entity's following behavior.<br>
     * Where: Used in gameplay systems or AI logic to validate or enforce following mechanics.<br>
     * Additional Info: If no following state is set for the entity, the method returns {@code false} by default.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose following state is to be retrieved.
     * @return {@code true} if the entity is following its owner; {@code false} otherwise.
     * @author Kyradjis
     * @see #ownerFollowingStateMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static boolean getFollowingState(LivingEntity pEntity) {
        return ownerFollowingStateMap.getOrDefault(pEntity, false);
    }

    /**
     * Sets the owner-following state for the specified entity.
     * <p>
     * Purpose: Updates whether the given {@link LivingEntity} is following its owner.<br>
     * When: Called during gameplay events or interactions that modify the following behavior of an entity.<br>
     * Where: Used to manage following mechanics for entities.<br>
     * Additional Info: The following state is stored in the {@link #ownerFollowingStateMap}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose following state is to be updated.
     * @param pState  {@code true} to set the entity as following its owner; {@code false} otherwise.
     * @author Kyradjis
     * @see #ownerFollowingStateMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static void setFollowingState(LivingEntity pEntity, boolean pState) {
        ownerFollowingStateMap.put(pEntity, pState);
    }

    // Loyalty Level
    /**
     * A map to store the loyalty levels of entities.
     * <p>
     * Purpose: This map tracks the loyalty level of each {@link LivingEntity}, representing its devotion to its owner.<br>
     * When: The map is populated or accessed when {@link #getLoyaltyLevel(LivingEntity)} or {@link #setLoyaltyLevel(LivingEntity, int)} is invoked.<br>
     * Where: Used to manage loyalty-based mechanics for entities.<br>
     * Additional Info: The keys are {@link LivingEntity} instances, and the values are {@code Integer} values representing their loyalty level.
     * </p>
     *
     * @see LivingEntity
     * @see Integer
     * @since 1.7.0
     */
    private static final Map<LivingEntity, Integer> loyaltyLevelMap = new HashMap<>();

    /**
     * Retrieves the loyalty level of the specified entity.
     * <p>
     * Purpose: Returns the loyalty level of the given {@link LivingEntity}, representing its devotion to its owner.<br>
     * When: Invoked during checks or interactions involving loyalty-based mechanics.<br>
     * Where: Used in gameplay systems that depend on entity loyalty.<br>
     * Additional Info: If no loyalty level is set for the entity, the method returns a default value of {@code 5}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose loyalty level is to be retrieved.
     * @return The loyalty level of the entity as an {@code int} value. Default is {@code 5}.
     * @author Kyradjis
     * @see #loyaltyLevelMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static int getLoyaltyLevel(LivingEntity pEntity) {
        return loyaltyLevelMap.getOrDefault(pEntity, 5);
    }

    /**
     * Sets the loyalty level for the specified entity.
     * <p>
     * Purpose: Updates the loyalty level of the given {@link LivingEntity}, representing its devotion to its owner.<br>
     * When: Called during gameplay events or interactions that modify the loyalty of an entity.<br>
     * Where: Used to manage loyalty-based mechanics for entities.<br>
     * Additional Info: The loyalty level is stored in the {@link #loyaltyLevelMap}.
     * </p>
     *
     * @param pEntity The {@link LivingEntity} whose loyalty level is to be updated.
     * @param pLevel  The loyalty level as an {@code int} value to associate with the entity.
     * @author Kyradjis
     * @see #loyaltyLevelMap
     * @see LivingEntity
     * @since 1.7.0
     */
    public static void setLoyaltyLevel(LivingEntity pEntity, int pLevel) {
        loyaltyLevelMap.put(pEntity, pLevel);
    }
}
