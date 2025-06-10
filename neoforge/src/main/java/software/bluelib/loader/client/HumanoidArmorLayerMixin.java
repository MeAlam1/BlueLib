package software.bluelib.loader.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import software.bluelib.client.utils.RenderUtils;


@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
	@Shadow
	protected abstract void setPartVisibility(A baseModel, EquipmentSlot equipmentSlot);

	@WrapWithCondition(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"))
	public boolean BlueLib$wrapArmorPieceRender(HumanoidArmorLayer<T, M, A> renderLayer, PoseStack pPoseStack, MultiBufferSource pBufferSource, T entity, EquipmentSlot equipmentSlot, int pPackedLight, A baseModel,
	                                             float limbSwing, float limbSwingAmount, float pPartialTick, float lerpedTickCount, float netHeadYaw, float headPitch) {
		return !RenderUtils.tryRenderArmorPiece(pPoseStack, pBufferSource, entity, entity.getItemBySlot(equipmentSlot), equipmentSlot, renderLayer.getParentModel(), baseModel, pPartialTick, pPackedLight, limbSwing, limbSwingAmount, lerpedTickCount, netHeadYaw, headPitch, this::setPartVisibility);
	}
}