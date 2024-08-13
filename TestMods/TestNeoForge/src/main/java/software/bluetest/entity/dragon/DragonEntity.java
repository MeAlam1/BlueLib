package software.bluetest.entity.dragon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bluelib.BlueLib;
import software.bluelib.entity.variant.IVariantEntity;

public class DragonEntity extends PathfinderMob implements IVariantEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DragonEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public String GetEntityName() {
        return "";
    }

    public ResourceLocation GetTextureLocation() {
        return null;
    }

    @Override
    public ResourceLocation GetVariantResource() {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar pControllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
