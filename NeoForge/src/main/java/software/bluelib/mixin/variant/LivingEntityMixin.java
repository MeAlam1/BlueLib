package software.bluelib.mixin.variant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.interfaces.variant.IVariantAccessor;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IVariantAccessor {

    @Unique
    private static final EntityDataAccessor<String> bluelib$VARIANT =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder, CallbackInfo pCi) {
        pBuilder.define(bluelib$VARIANT, "normal");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound, CallbackInfo pCi) {
        pCompound.putString("Variant", bluelib$getVariantName());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound, CallbackInfo pCi) {
        bluelib$setVariantName(pCompound.getString("Variant"));
    }

    @Unique
    public void bluelib$setVariantName(String pName) {
        ((Entity) (Object) this).getEntityData().set(bluelib$VARIANT, pName);
    }

    @Unique
    public String bluelib$getVariantName() {
        return ((Entity) (Object) this).getEntityData().get(bluelib$VARIANT);
    }

    @Override
    public void setEntityVariantName(String pVariantName) {
        bluelib$setVariantName(pVariantName);
    }

    @Override
    public String getEntityVariantName() {
        return bluelib$getVariantName();
    }
}

