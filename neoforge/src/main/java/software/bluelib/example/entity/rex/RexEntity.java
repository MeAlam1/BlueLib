// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.rex;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

/**
 * A {@code rexEntity} class representing a rex entity in the game, which extends {@link TamableAnimal}
 * and implements {@link IVariantEntity} and {@link GeoEntity}.
 * <p>
 * This class manages the rex's variant system, its data synchronization, and integrates with the GeckoLib
 * animation system.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #defineSynchedData()} - Defines the synchronized data for the rex entity, including its variant.</li>
 *   <li>{@link #addAdditionalSaveData(CompoundTag)} - Adds custom data to the entity's NBT for saving.</li>
 *   <li>{@link #readAdditionalSaveData(CompoundTag)} - Reads custom data from the entity's NBT for loading.</li>
 *   <li>{@link #finalizeSpawn(ServerLevelAccessor, DifficultyInstance, MobSpawnType, SpawnGroupData, CompoundTag)} - Finalizes the spawning process and sets up parameters.</li>
 *   <li>{@link #setVariantName(String)} - Sets the variant name of the rex.</li>
 *   <li>{@link #getVariantName()} - Retrieves the current variant name of the rex.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class RexEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    /**
     * Entity data accessor for the variant of the rex.
     * <p>
     * This is used to store and retrieve the variant data for synchronization between server and client.
     * </p>
     *
     * @since 1.0.0
     */
    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(RexEntity.class, EntityDataSerializers.STRING);

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
     * Defines the synchronized data for this rex entity, including the variant.
     * <p>
     * This method initializes the {@link EntityDataAccessor} to handle the variant data.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, "normal");
    }

    /**
     * Adds custom data to the entity's NBT tag for saving.
     * <p>
     * This method stores the variant name in the NBT data so it can be restored when loading the entity.
     * </p>
     *
     * @param pCompound {@link CompoundTag} - The NBT tag to which data should be added.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Variant", getVariantName());
    }

    /**
     * Reads custom data from the entity's NBT tag for loading.
     * <p>
     * This method retrieves the variant name from the NBT data and sets it for the entity.
     * </p>
     *
     * @param pCompound {@link CompoundTag} - The NBT tag from which data should be read.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantName(pCompound.getString("Variant"));
    }

    /**
     * Finalizes the spawning of the rex entity.
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
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pSpawnTag) {
        if (getVariantName() == null || getVariantName().isEmpty()) {
            this.setVariantName(getRandomVariant(getEntityVariants(entityName), "normal"));
            ParameterUtils.ParameterBuilder.forVariant(entityName, this.getVariantName())
                    .withParameter("customParameter")
                    .withParameter("int")
                    .withParameter("bool")
                    .withParameter("array")
                    .connect();
        }
        BaseLogger.log(BaseLogLevel.SUCCESS, "Rex Spawned with Variant: " + getVariantName(), true);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pSpawnTag);
    }

    /**
     * Sets the variant name for the rex entity.
     *
     * @param pName {@link String} - The name of the variant to set.
     * @author MeAlam
     * @since 1.0.0
     */
    public void setVariantName(String pName) {
        this.entityData.set(VARIANT, pName);
    }

    /**
     * Retrieves the current variant name of the rex entity.
     *
     * @return {@link String} - The current variant name.
     * @author MeAlam
     * @since 1.0.0
     */
    public String getVariantName() {
        return this.entityData.get(VARIANT);
    }
    /* All Code below this Fragment is not Library Related!!! */

    /**
     * The cache for the animatable instance.
     *
     * @since 1.0.0
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /**
     * Defines the synchronized data for the rex entity.
     *
     * @return {@link SynchedEntityData} - The builder for the synchronized data.
     * @author MeAlam
     * @since 1.0.0
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.FLYING_SPEED, 0.3);
    }

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
