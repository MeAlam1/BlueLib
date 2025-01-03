// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib_examples.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.entity.EntityStateManager;
import software.bluelib.interfaces.entity.IFlyingEntity;
import software.bluelib.interfaces.entity.ITamableEntity;
import software.bluelib.interfaces.variant.IVariantAccessor;
import software.bluelib.interfaces.variant.IVariantEntity;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Objects;

public class ExampleEntity extends PathfinderMob implements GeoEntity, IVariantEntity, IFlyingEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final String entityName = "example";

    public ExampleEntity(EntityType<? extends ExampleEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setVariantName(String pVariantName) {
        ((IVariantAccessor) this).setEntityVariantName(pVariantName);
    }

    public String getVariantName() {
        return ((IVariantAccessor) this).getEntityVariantName();
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull EntitySpawnReason pReason, @Nullable SpawnGroupData pSpawnData) {
        if (getVariantName() == null || getVariantName().isEmpty()) {
            setVariantName(getRandomVariant(getEntityVariants(entityName), "green"));
        }
        canFly(this, getRandom().nextBoolean());
        Objects.requireNonNull(pLevel.getLevel().getNearestPlayer(this, 5)).displayClientMessage(Component.literal("I can fly: " + canFly(this)), false);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }
}
