package software.bluetest.entity.rex;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.ParameterUtils;
import software.bluetest.init.ModEntities;

public class RexEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(RexEntity.class, EntityDataSerializers.STRING);
    protected final String entityName = "rex";

    public RexEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, "normal");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Variant", getVariantName());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantName(pCompound.getString("Variant"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        String variantName = getVariantName();
        if (variantName == null || variantName.isEmpty()) {
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

    public void setVariantName(String pName) {
        this.entityData.set(VARIANT, pName);
    }

    public String getVariantName() {
        return this.entityData.get(VARIANT);
    }

    /**
     * All Code below this Fragment is not Library Related!!!
     */

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.FLYING_SPEED, 0.3);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar pControllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return null;
    }
}
