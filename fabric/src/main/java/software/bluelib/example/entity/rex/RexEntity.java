// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.interfaces.variant.IVariantAccessor;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

/**
 * A {@code RexEntity} class representing a Rex entity in the game, which extends {@link TamableAnimal}
 * and implements {@link IVariantEntity} and {@link GeoEntity}.
 * <p>
 * This class manages the Rex's variant system, its data synchronization, and integrates with the GeckoLib
 * animation system.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #defineSynchedData(SynchedEntityData.Builder)} - Defines the synchronized data for the Rex entity, including its variant.</li>
 *   <li>{@link #addAdditionalSaveData(CompoundTag)} - Adds custom data to the entity's NBT for saving.</li>
 *   <li>{@link #readAdditionalSaveData(CompoundTag)} - Reads custom data from the entity's NBT for loading.</li>
 *   <li>{@link #finalizeSpawn(ServerLevelAccessor, DifficultyInstance, MobSpawnType, SpawnGroupData)} - Finalizes the spawning process and sets up parameters.</li>
 *   <li>{@link #setVariantName(String)} - Sets the variant name of the Rex.</li>
 *   <li>{@link #getVariantName()} - Retrieves the current variant name of the Rex.</li>
 * </ul>
 * </p>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class RexEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    /**
     * The name of the entity.
     *
     * @since 1.0.0
     */
    protected final String entityName = "rex";

    /**
     * Constructs a new {@link RexEntity} instance with the specified entity type and level.
     *
     * @param pEntityType {@link EntityType} - The type of the entity.
     * @param pLevel      {@link Level} - The level in which the entity is created.
     * @author MeAlam
     * @since 1.0.0
     */
    public RexEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * A {@code public void} that defines the Variant data for the rex entity.
     *
     * @param pVariantName {@link String} - The variant name of the rex entity.
     * @author MeAlam
     * @since 1.0.0
     */
    public void setVariantName(String pVariantName) {
        ((IVariantAccessor) this).setEntityVariantName(pVariantName);
    }

    /**
     * A {@code public} {@link String} that retrieves the Variant data for the rex entity.
     *
     * @return {@link String} - The variant name of the rex entity.
     * @author MeAlam
     * @since 1.0.0
     */
    public String getVariantName() {
        return ((IVariantAccessor) this).getEntityVariantName();
    }

    /**
     * Finalizes the spawning of the Rex entity.
     * <p>
     * This method sets up the variant for the entity and connects parameters if needed.
     * </p>
     *
     * @param pLevel      {@link ServerLevelAccessor} - The level in which the entity is spawned.
     * @param pDifficulty {@link DifficultyInstance} - The difficulty instance for spawning.
     * @param pReason     {@link MobSpawnType} - The reason for spawning the entity.
     * @param pSpawnData  {@link SpawnGroupData} - Data related to the spawn.
     * @return {@link SpawnGroupData} - Updated spawn data.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        if (getVariantName() == null || getVariantName().isEmpty()) {
            this.setVariantName(getRandomVariant(getEntityVariants(entityName), "normal"));
            ParameterUtils.ParameterBuilder.forVariant(entityName, this.getVariantName())
                    .withParameter("customParameter")
                    .withParameter("int")
                    .withParameter("bool")
                    .withParameter("array")
                    .connect();
        }
        BaseLogger.log(BaseLogLevel.SUCCESS, "Dragon Spawned with Variant: " + getVariantName(), true);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    /* All Code below this Fragment is not Library Related!!! */

    /**
     * The cache for the animatable instance.
     *
     * @since 1.0.0
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /**
     * Adds custom data to the entity's NBT for saving.
     *
     * @param pControllerRegistrar {@link CompoundTag} - The tag to add the data to.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar pControllerRegistrar) {
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
