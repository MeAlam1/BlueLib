// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.fury;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code nightfuryEntity} class representing a nightfury entity in the game, which extends {@link TamableAnimal}
 * and implements {@link IVariantEntity} and {@link GeoEntity}.
 * <p>
 * This class manages the nightfury's variant system, its data synchronization, and integrates with the GeckoLib
 * animation system.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #finalizeSpawn(ServerLevelAccessor, DifficultyInstance, EntitySpawnReason, SpawnGroupData)} - Finalizes the spawning process and sets up parameters.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class FuryEntity extends TamableAnimal implements IVariantEntity, GeoEntity {

    /**
     * Constructs a new {@link FuryEntity} instance with the specified entity type and level.
     *
     * @param pEntityType {@link EntityType} - The type of the entity.
     * @param pLevel      {@link Level} - The level in which the entity is created.
     * @author MeAlam
     * @since 1.0.0
     */
    public FuryEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /* All Code below this Fragment is not Library Related!!! */

    /**
     * The cache for the animatable instance.
     *
     * @since 1.0.0
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean shouldMove = false;

    private class WalkGoal extends RandomStrollGoal {
        public WalkGoal() {
            super(FuryEntity.this, 0.25, 10);
        }

        @Override
        public boolean canUse() {
            return FuryEntity.this.shouldMove && super.canUse();
        }
    }

    private class StandStillGoal extends Goal {
        @Override
        public boolean canUse() {
            return !FuryEntity.this.shouldMove;
        }

        @Override
        public void tick() {
            FuryEntity.this.getNavigation().stop();
        }
    }

    private class StandStillGoal2 extends Goal {
        @Override
        public boolean canUse() {
            return FuryEntity.this.shouldMove;
        }

        @Override
        public void tick() {
            FuryEntity.this.getNavigation().stop();
        }
    }

    private final WalkGoal walkGoal = new WalkGoal();
    private final StandStillGoal standStillGoal = new StandStillGoal();
    private final StandStillGoal2 standStillGoal2 = new StandStillGoal2();

    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        this.shouldMove = !this.shouldMove;

        if (this.shouldMove) {
            pPlayer.displayClientMessage(Component.nullToEmpty("Fury will now walk!"), true);
            BaseLogger.log(BaseLogLevel.INFO, "Fury will now walk!");
            this.goalSelector.addGoal(1, standStillGoal2);
            this.goalSelector.removeGoal(standStillGoal);
        } else {
            pPlayer.displayClientMessage(Component.nullToEmpty("Fury will now walk!"), true);
            BaseLogger.log(BaseLogLevel.INFO, "Fury will now stand still!");
            this.goalSelector.addGoal(1, standStillGoal2);
            this.goalSelector.removeGoal(walkGoal);
        }

        return InteractionResult.SUCCESS;
    }

    public boolean isShouldMove() {
        return shouldMove;
    }


    public boolean isFlying() {
        return !this.onGround();
    }

    public boolean isMoving() {
        Vec3 velocity = this.getDeltaMovement();
        double velocityThreshold = 0.05;
        return velocity.lengthSqr() > velocityThreshold * velocityThreshold;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    /**
     * Adds custom data to the entity's NBT for saving.
     *
     * @return {@link CompoundTag} - The tag with the custom data.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /**
     * Adds custom data to the entity's NBT for saving.
     *
     * @param pLevel       {@link CompoundTag} - The tag to add the data to.
     * @param pOtherParent {@link CompoundTag} - The other tag to add the data from.
     * @return {@link CompoundTag} - The tag with the custom data.
     * @author MeAlam
     * @since 1.0.0
     */
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return null;
    }

    /**
     * Adds custom data to the entity's NBT for saving.
     *
     * @param pItemStack {@link ItemStack} - The item stack to check.
     * @return {@link boolean} - Whether the item is food or not.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public boolean isFood(@NotNull ItemStack pItemStack) {
        return false;
    }
}
