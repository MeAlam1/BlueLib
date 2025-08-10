/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.common.variant;

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
import software.bluelib.entity.variant.IVariantAccessor;

@Mixin(LivingEntity.class)
@SuppressWarnings({ "unused" })
public class LivingEntityMixin implements IVariantAccessor {

	@Unique
	@NotNull
	private static final EntityDataAccessor<String> bluelib$VARIANT = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);

	@Inject(method = "defineSynchedData", at = @At("HEAD"))
	protected void defineSynchedData(@NotNull SynchedEntityData.@NotNull Builder pBuilder, @NotNull CallbackInfo pCi) {
		pBuilder.define(bluelib$VARIANT, "normal");
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound, @NotNull CallbackInfo pCi) {
		pCompound.putString("Variant", bluelib$getVariantName());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound, @NotNull CallbackInfo pCi) {
		bluelib$setVariantName(pCompound.getString("Variant"));
	}

	@Unique
	public void bluelib$setVariantName(@NotNull String pName) {
		((Entity) (Object) this).getEntityData().set(bluelib$VARIANT, pName);
	}

	@Unique
	@NotNull
	public String bluelib$getVariantName() {
		return ((Entity) (Object) this).getEntityData().get(bluelib$VARIANT);
	}

	@Override
	public void setEntityVariantName(@NotNull String pVariantName) {
		bluelib$setVariantName(pVariantName);
	}

	@Override
	public @NotNull String getEntityVariantName() {
		return bluelib$getVariantName();
	}
}
