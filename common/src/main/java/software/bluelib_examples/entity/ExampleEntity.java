
package software.bluelib_examples.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.api.entity.variant.IVariantEntity;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.entity.variant.IVariantAccessor;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib_examples.BlueLibConstants;

public class ExampleEntity extends PathfinderMob implements GeoEntity, IVariantEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final String entityName = "test";

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
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        NetworkRegistry.sendToAllPlayers(new OpenLoggerPacket());
        if (getVariantName() == null || getVariantName().isEmpty()) {
            setVariantName(getRandomVariant(getEntityVariants(ResourceLocation.fromNamespaceAndPath(BlueLibConstants.MOD_ID, entityName)), "normal"));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }
}
