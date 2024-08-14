package software.bluetest.entity.dragon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.entity.variant.IVariantEntity;
import software.bluelib.entity.variant.VariantKeys;
import software.bluelib.entity.variant.VariantRegistry;
import software.bluetest.BlueTest;

import java.util.List;
import java.util.stream.Collectors;

public class DragonEntity extends TamableAnimal implements IVariantEntity, GeoEntity {
    private final String entityName = "dragon";
    private final VariantRegistry texturesLoader = new VariantRegistry();
    private List<String> variantNames;

    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(DragonEntity.class, EntityDataSerializers.STRING);

    public DragonEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        texturesLoader.loadVariantsFromJson(getJSONLocation());
        this.variantNames = List.of(getEntityName());
            List<String> variants = getDragonVariants(getEntityName());
        if (variants != null && !variants.isEmpty()) {
            this.variantNames = variants;
        }
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

    // When Entity is Spawned
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.level().isClientSide) {
            if (getVariantName().isEmpty()) {
                this.setVariantName(getRandomVariant(variantNames, "blue"));
            }
        }
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

    public List<String> getDragonVariants(String variantType) {
        return texturesLoader.getVariants().stream()
                .filter(variant -> variantType.equals(variant.getEntityName()))
                .map(VariantKeys::getVariantName)
                .collect(Collectors.toList());
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
