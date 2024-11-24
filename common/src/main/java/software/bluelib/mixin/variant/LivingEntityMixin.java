// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.mixin.variant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.interfaces.variant.IVariantAccessor;

/**
 * A {@code public} {@link Mixin} class that injects functionality into the {@link LivingEntity} class to
 * provide variant management support within the BlueLib framework.
 * <p>
 * This mixin adds functionality to define, save, and retrieve an entity's variant information
 * using NBT (Named Binary Tag) data. It implements the {@link IVariantAccessor} interface, which
 * allows setting and getting the variant name of a {@link LivingEntity}.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #bluelib$setVariantName(String)} - Sets the variant name of the entity.</li>
 * <li>{@link #bluelib$getVariantName()} - Retrieves the variant name of the entity.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IVariantAccessor {

    /**
     * A {@link EntityDataAccessor} to hold the entity's variant name, which is synchronized
     * across clients and servers using {@link SynchedEntityData}.
     *
     * @since 1.0.0
     */
    @Unique
    private static final EntityDataAccessor<String> bluelib$VARIANT = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);

    /**
     * A {@code public void} that injects into the {@code defineSynchedData} method of {@link LivingEntity} to define
     * the synchronized data that will hold the entity's variant information.
     *
     * @param pBuilder {@link SynchedEntityData.Builder} - The builder to define entity data.
     * @param pCi      {@link CallbackInfo} - Callback information for the injection process.
     * @author MeAlam
     * @since 1.0.0
     */
    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder, CallbackInfo pCi) {
        pBuilder.define(bluelib$VARIANT, "normal");
    }

    /**
     * A {@code public void} that injects into the {@code addAdditionalSaveData} method of {@link LivingEntity} to store
     * the entity's variant in NBT data when saving the entity.
     *
     * @param pCompound {@link CompoundTag} - The NBT tag to save the entity's variant information.
     * @param pCi       {@link CallbackInfo} - Callback information for the injection process.
     * @author MeAlam
     * @since 1.0.0
     */
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound, CallbackInfo pCi) {
        pCompound.putString("Variant", bluelib$getVariantName());
    }

    /**
     * A {@code public void} that injects into the {@code readAdditionalSaveData} method of {@link LivingEntity} to read
     * the entity's variant from NBT data when loading the entity.
     *
     * @param pCompound {@link CompoundTag} - The NBT tag containing the entity's variant information.
     * @param pCi       {@link CallbackInfo} - Callback information for the injection process.
     * @author MeAlam
     * @since 1.0.0
     */
    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound, CallbackInfo pCi) {
        bluelib$setVariantName(pCompound.getString("Variant"));
    }

    /**
     * A {@code public void} method that sets the entity's variant name.
     *
     * @param pName {@link String} - The variant name to assign to the entity.
     * @author MeAlam
     * @since 1.0.0
     */
    @Unique
    public void bluelib$setVariantName(String pName) {
        ((Entity) (Object) this).getEntityData().set(bluelib$VARIANT, pName);
    }

    /**
     * A {@code public} {@link String} method that retrieves the entity's current variant name.
     *
     * @return The current variant name of the entity as a {@link String}.
     * @author MeAlam
     * @since 1.0.0
     */
    @Unique
    public String bluelib$getVariantName() {
        return ((Entity) (Object) this).getEntityData().get(bluelib$VARIANT);
    }

    /**
     * A {@code public void} method that sets the variant name of the entity. This method overrides
     * the {@link IVariantAccessor#setEntityVariantName(String)} interface method.
     *
     * @param pVariantName {@link String} - The variant name to set for the entity.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public void setEntityVariantName(String pVariantName) {
        bluelib$setVariantName(pVariantName);
    }

    /**
     * A {@code public} {@link String} method that retrieves the variant name of the entity. This method overrides
     * the {@link IVariantAccessor#getEntityVariantName()} interface method.
     *
     * @return The current variant name of the entity as a {@link String}.
     * @author MeAlam
     * @since 1.0.0
     */
    @Override
    public String getEntityVariantName() {
        return bluelib$getVariantName();
    }
}
