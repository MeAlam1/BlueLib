package software.bluetest.entity.dragon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import software.bluelib.entity.variant.IVariantEntity;
import software.bluelib.entity.variant.VariantKeys;
import software.bluelib.entity.variant.VariantRegistry;
import software.bluelib.entity.variant.VariantUtils;
import software.bluetest.BlueTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragonEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(DragonEntity.class, EntityDataSerializers.STRING);
    private final String entityName = "dragon";
    private final VariantRegistry texturesLoader = new VariantRegistry();

    public DragonEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        texturesLoader.loadVariantsFromJson(getJSONLocation());
        VariantUtils.processVariants(texturesLoader, this::getCustomParameters);
    }

    private Map<String, String> getCustomParameters(VariantKeys pVariant) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("number", pVariant.getParameter("number"));
        return parameters;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, "");
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
        if (getVariantName().isEmpty()) {
            List<String> availableVariants = getEntityVariants(getEntityName(), texturesLoader);
            this.setVariantName(getRandomVariant(availableVariants, "blue"));
            System.out.println("Number: " + VariantUtils.getParameter(getVariantName(), "number"));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    // Set the Variant
    public void setVariantName(String pName) {
        this.entityData.set(VARIANT, pName);
    }

    @Override
    public String getVariantName() {
        return this.entityData.get(VARIANT);
    }

    public ResourceLocation getJSONLocation() {
        return new ResourceLocation(BlueTest.MODID, "variant/entity/dragon.json");
    }

    // Just to get the Entity Working

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
