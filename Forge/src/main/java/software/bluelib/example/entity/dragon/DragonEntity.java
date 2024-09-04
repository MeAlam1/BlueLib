// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.dragon;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.ParameterUtils;

import javax.annotation.Nullable;

/**
 * A {@code DragonEntity} class representing a dragon entity in the game, which extends {@link TameableEntity}
 * and implements {@link IVariantEntity} and {@link IAnimatable}.
 * <p>
 * This class manages the dragon's variant system, its data synchronization, and integrates with the GeckoLib
 * animation system.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #defineSynchedData()} - Defines the synchronized data for the dragon entity, including its variant.</li>
 *   <li>{@link #addAdditionalSaveData(CompoundNBT)} - Adds custom data to the entity's NBT for saving.</li>
 *   <li>{@link #readAdditionalSaveData(CompoundNBT)} - Reads custom data from the entity's NBT for loading.</li>
 *   <li>{@link #finalizeSpawn(IServerWorld, DifficultyInstance, SpawnReason, ILivingEntityData, CompoundNBT)} - Finalizes the spawning process and sets up parameters.</li>
 *   <li>{@link #setVariantName(String)} - Sets the variant name of the dragon.</li>
 *   <li>{@link #getVariantName()} - Retrieves the current variant name of the dragon.</li>
 * </ul>
 * </p>
 *
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class DragonEntity extends TameableEntity implements IVariantEntity, IAnimatable {
    /**
     * Entity data accessor for the variant of the dragon.
     * <p>
     * This is used to store and retrieve the variant data for synchronization between server and client.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    public static final DataParameter<String> VARIANT = EntityDataManager.defineId(DragonEntity.class, DataSerializers.STRING);

    /**
     * The name of the entity.
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    protected final String entityName = "dragon";

    /**
     * Constructs a new {@link DragonEntity} instance with the specified entity type and level.
     *
     * @param pEntityType {@link EntityType} - The type of the entity.
     * @param pLevel {@link World} - The level in which the entity is created.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public DragonEntity(EntityType<? extends TameableEntity> pEntityType, World pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Defines the synchronized data for this dragon entity, including the variant.
     * <p>
     * This method initializes the {@link DataParameter} to handle the variant data.
     * </p>
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
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
     * @param pCompound {@link CompoundNBT} - The NBT tag to which data should be added.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    @Override
    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Variant", getVariantName());
    }

    /**
     * Reads custom data from the entity's NBT tag for loading.
     * <p>
     * This method retrieves the variant name from the NBT data and sets it for the entity.
     * </p>
     *
     * @param pCompound {@link CompoundNBT} - The NBT tag from which data should be read.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    @Override
    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantName(pCompound.getString("Variant"));
    }

    /**
     * Finalizes the spawning of the dragon entity.
     * <p>
     * This method sets up the variant for the entity and connects parameters if needed.
     * </p>
     *
     * @param pLevel {@link IServerWorld} - The level in which the entity is spawned.
     * @param pDifficulty {@link DifficultyInstance} - The difficulty instance for spawning.
     * @param pReason {@link SpawnReason} - The reason for spawning the entity.
     * @param pSpawnData {@link ILivingEntityData} - Data related to the spawn.
     * @param pDataTag {@link CompoundNBT} - Additional data for spawning.
     * @return {@link ILivingEntityData} - Updated spawn data.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        if (getVariantName() == null || getVariantName().isEmpty()) {
            this.setVariantName(getRandomVariant(getEntityVariants(entityName), "normal"));
            ParameterUtils.ParameterBuilder.forVariant(entityName,this.getVariantName())
                    .withParameter("customParameter")
                    .withParameter("int")
                    .withParameter("bool")
                    .withParameter("array")
                    .connect();
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    /**
     * Sets the variant name for the dragon entity.
     *
     * @param pName {@link String} - The name of the variant to set.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public void setVariantName(String pName) {
        this.entityData.set(VARIANT, pName);
    }

    /**
     * Retrieves the current variant name of the dragon entity.
     *
     * @return {@link String} - The current variant name.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public String getVariantName() {
        return this.entityData.get(VARIANT);
    }

    /**
     * All Code below this Fragment is not Library Related!!!
     */

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return TameableEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld pServerWorld, AgeableEntity pAgeableEntity) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData pData) {}

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
