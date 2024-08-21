package software.bluetest.entity.dragon;

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

public class DragonEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    // NOTE. 3 Lines Required for the Wiki
    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(DragonEntity.class, EntityDataSerializers.STRING);

    public DragonEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // NOTE. Required for the Wiki
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, "normal");
    }

    // NOTE. Required for the Wiki
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Variant", getVariantName());
    }

    // NOTE. Required for the Wiki
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantName(pCompound.getString("Variant"));
    }

    // NOTE. Required for the Wiki
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        String variantName = getVariantName();
        if (variantName == null || variantName.isEmpty()) {
            this.setVariantName(getRandomVariant(getEntityVariants(ModEntities.DRAGON.getKey().location().getPath()), "normal"));
            ParameterUtils.ParameterBuilder.forVariant(this.getVariantName())
                    .withParameter("customParameter")
                    .withParameter("int")
                    .withParameter("bool")
                    .withParameter("array")
                    .connect();
            System.out.println("Variant List: " + getEntityVariants(ModEntities.DRAGON.getKey().location().getPath()));
            System.out.println("String: " + ParameterUtils.getParameter(this.getVariantName(), "customParameter"));
            System.out.println("Int: " + ParameterUtils.getParameter(this.getVariantName(), "int"));
            System.out.println("Bool: " + ParameterUtils.getParameter(this.getVariantName(), "bool"));
            System.out.println("Array: " + ParameterUtils.getParameter(this.getVariantName(), "array"));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    // NOTE. Required for the Wiki
    public void setVariantName(String pName) {
        this.entityData.set(VARIANT, pName);
    }

    // NOTE. Required for the Wiki
    @Override
    public String getVariantName() {
        return this.entityData.get(VARIANT);
    }

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
