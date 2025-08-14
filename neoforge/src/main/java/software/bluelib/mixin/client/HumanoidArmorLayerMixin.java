/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.client.utils.RenderUtils;

@Mixin(HumanoidArmorLayer.class)
@OnlyIn(Dist.CLIENT)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

	@Shadow
	protected abstract void setPartVisibility(@NotNull A pBaseModel, @NotNull EquipmentSlot pEquipmentSlot);

	@WrapWithCondition(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"))
	public boolean BlueLib$wrapArmorPieceRender(
			@NotNull HumanoidArmorLayer<T, M, A> pRenderLayer,
			@NotNull PoseStack pPoseStack,
			@NotNull MultiBufferSource pBufferSource,
			@NotNull T pEntity,
			@NotNull EquipmentSlot pEquipmentSlot,
			int pPackedLight,
			@NotNull A pBaseModel,
			float pLimbSwing,
			float pLimbSwingAmount,
			float pPartialTick,
			float pLerpedTickCount,
			float pNetHeadYaw,
			float pHeadPitch) {
		return !RenderUtils.tryRenderArmorPiece(pPoseStack, pBufferSource, pEntity, pEntity.getItemBySlot(pEquipmentSlot), pEquipmentSlot, pRenderLayer.getParentModel(), pBaseModel, pPartialTick, pPackedLight, pLimbSwing, pLimbSwingAmount, pLerpedTickCount, pNetHeadYaw, pHeadPitch, this::setPartVisibility);
	}
}
