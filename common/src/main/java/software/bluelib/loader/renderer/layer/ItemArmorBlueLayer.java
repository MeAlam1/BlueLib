/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.Color;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.item.BlueItem;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.CubeCache;
import software.bluelib.loader.renderer.armor.BlueArmorRenderer;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class ItemArmorBlueLayer<T extends LivingEntity & BlueAnimatable> extends BlueRenderLayer<T> {

	@NotNull
	protected static final HumanoidModel<LivingEntity> INNER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
	@NotNull
	protected static final HumanoidModel<LivingEntity> OUTER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

	@Nullable
	protected ItemStack mainHandStack;
	@Nullable
	protected ItemStack offhandStack;
	@Nullable
	protected ItemStack helmetStack;
	@Nullable
	protected ItemStack chestplateStack;
	@Nullable
	protected ItemStack leggingsStack;
	@Nullable
	protected ItemStack bootsStack;

	public ItemArmorBlueLayer(@NotNull BlueRenderer<T> pBlueRenderer) {
		super(pBlueRenderer);
	}

	@NotNull
	protected EquipmentSlot getEquipmentSlotForBone(@NotNull BoneCache pBone, @NotNull ItemStack pStack, @NotNull T pAnimatable) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				if (pStack == pAnimatable.getItemBySlot(slot))
					return slot;
			}
		}

		return EquipmentSlot.CHEST;
	}

	@NotNull
	protected ModelPart getModelPartForBone(@NotNull BoneCache pBone, @NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack, @NotNull T pAnimatable, @NotNull HumanoidModel<?> pBaseModel) {
		return pBaseModel.body;
	}

	@Nullable
	protected ItemStack getArmorItemForBone(@NotNull BoneCache pBone, @NotNull T pAnimatable) {
		return null;
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		this.mainHandStack = pContext.animatable().getItemBySlot(EquipmentSlot.MAINHAND);
		this.offhandStack = pContext.animatable().getItemBySlot(EquipmentSlot.OFFHAND);
		this.helmetStack = pContext.animatable().getItemBySlot(EquipmentSlot.HEAD);
		this.chestplateStack = pContext.animatable().getItemBySlot(EquipmentSlot.CHEST);
		this.leggingsStack = pContext.animatable().getItemBySlot(EquipmentSlot.LEGS);
		this.bootsStack = pContext.animatable().getItemBySlot(EquipmentSlot.FEET);
	}

	@Override
	public void renderForBone(@NotNull BoneCache pBone, @NotNull IRenderContext<T> pContext) {
		ItemStack armorStack = getArmorItemForBone(pBone, pContext.animatable());

		if (armorStack == null)
			return;

		if (armorStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
			renderSkullAsArmor(pContext.poseStack(), pBone, armorStack, skullBlock, pContext.bufferSource(), pContext.packedLight());
		} else {
			EquipmentSlot slot = getEquipmentSlotForBone(pBone, armorStack, pContext.animatable());
			HumanoidModel<?> model = getModelForItem(pBone, slot, armorStack, pContext.animatable());
			ModelPart modelPart = getModelPartForBone(pBone, slot, armorStack, pContext.animatable(), model);

			if (!modelPart.cubes.isEmpty()) {
				pContext.poseStack().pushPose();
				pContext.poseStack().scale(-1, -1, 1);

				if (model instanceof BlueArmorRenderer<?, ?> BlueArmorRenderer) {
					prepModelPartForRender(pContext.poseStack(), pBone, modelPart);
					BlueArmorRenderer.applyBoneVisibilityByPart(slot, modelPart, model);
					BlueArmorRenderer.renderToBuffer(pContext.poseStack(), null, pContext.packedLight(), pContext.packedOverlay(), Color.WHITE.argbInt());
				} else if (armorStack.getItem() instanceof ArmorItem) {
					prepModelPartForRender(pContext.poseStack(), pBone, modelPart);
					renderVanillaArmorPiece(pContext.poseStack(), pContext.animatable(), pBone, slot, armorStack, modelPart, pContext.bufferSource(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay());
				}

				pContext.poseStack().popPose();
			}
		}
	}

	protected <I extends Item & BlueItem> void renderVanillaArmorPiece(@NotNull PoseStack pPoseStack, @NotNull T pAnimatable, @NotNull BoneCache pBone, @NotNull EquipmentSlot pSlot, @NotNull ItemStack pArmorStack,
			@NotNull ModelPart pModelPart, @NotNull MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		Holder<ArmorMaterial> material = ((ArmorItem) pArmorStack.getItem()).getMaterial();

		for (ArmorMaterial.Layer layer : material.value().layers()) {
			int color = pArmorStack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(pArmorStack, -6265536) : -1;
			VertexConsumer buffer = getVanillaArmorBuffer(pBufferSource, pAnimatable, pArmorStack, pSlot, pBone, layer, pPackedLight, pPackedOverlay, false);

			pModelPart.render(pPoseStack, buffer, pPackedLight, pPackedOverlay, color);
		}

		ArmorTrim trim = pArmorStack.get(DataComponents.TRIM);

		if (trim != null) {
			TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(pSlot == EquipmentSlot.LEGS ? trim.innerTexture(material) : trim.outerTexture(material));
			VertexConsumer buffer = sprite.wrap(pBufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
			pModelPart.render(pPoseStack, buffer, pPackedLight, pPackedOverlay);
		}

		if (pArmorStack.hasFoil())
			pModelPart.render(pPoseStack, getVanillaArmorBuffer(pBufferSource, pAnimatable, pArmorStack, pSlot, pBone, null, pPackedLight, pPackedOverlay, true), pPackedLight, pPackedOverlay, Color.WHITE.argbInt());
	}

	@NotNull
	protected VertexConsumer getVanillaArmorBuffer(@NotNull MultiBufferSource pBufferSource, @NotNull T pAnimatable, @NotNull ItemStack pStack, @NotNull EquipmentSlot pSlot, @NotNull BoneCache pBone, @Nullable ArmorMaterial.Layer pLayer, int pPackedLight, int pPackedOverlay, boolean pForGlint) {
		if (pForGlint)
			return pBufferSource.getBuffer(RenderType.armorEntityGlint());

		// TODO: Find Solution for this
		return pBufferSource.getBuffer(RenderType.armorCutoutNoCull(pLayer.texture(pSlot == EquipmentSlot.LEGS)));
	}

	@NotNull
	protected HumanoidModel<?> getModelForItem(@NotNull BoneCache pBone, @NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack, @NotNull T pAnimatable) {
		HumanoidModel<LivingEntity> defaultModel = pSlot == EquipmentSlot.LEGS ? INNER_ARMOR_MODEL : OUTER_ARMOR_MODEL;

		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getArmorModelForItem(pAnimatable, pStack, pSlot, defaultModel);
	}

	protected void renderSkullAsArmor(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone, @NotNull ItemStack pStack, @NotNull AbstractSkullBlock pSkullBlock, @NotNull MultiBufferSource pBufferSource, int pPackedLight) {
		SkullBlock.Type type = pSkullBlock.getType();
		SkullModelBase model = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(type);
		RenderType pRenderType = SkullBlockRenderer.getRenderType(type, pStack.get(DataComponents.PROFILE));

		pPoseStack.pushPose();
		RenderUtils.translateAndRotateMatrixForBone(pPoseStack, pBone);
		pPoseStack.scale(1.1875f, 1.1875f, 1.1875f);
		pPoseStack.translate(-0.5f, 0, -0.5f);
		SkullBlockRenderer.renderSkull(null, 0, 0, pPoseStack, pBufferSource, pPackedLight, model, pRenderType);
		pPoseStack.popPose();
	}

	protected void prepModelPartForRender(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone, @NotNull ModelPart pSourcePart) {
		final CubeCache firstCube = pBone.getCubes().getFirst();
		final Cube armorCube = getReferenceCubeForModel(pBone, pSourcePart);
		final double armorBoneSizeX = firstCube.size().x();
		final double armorBoneSizeY = firstCube.size().y();
		final double armorBoneSizeZ = firstCube.size().z();
		final double actualArmorSizeX = Math.abs(armorCube.maxX - armorCube.minX);
		final double actualArmorSizeY = Math.abs(armorCube.maxY - armorCube.minY);
		final double actualArmorSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);
		float scaleX = (float) (armorBoneSizeX / actualArmorSizeX);
		float scaleY = (float) (armorBoneSizeY / actualArmorSizeY);
		float scaleZ = (float) (armorBoneSizeZ / actualArmorSizeZ);

		pSourcePart.setPos(-(pBone.getPivotX() - ((pBone.getPivotX() * scaleX) - pBone.getPivotX()) / scaleX),
				-(pBone.getPivotY() - ((pBone.getPivotY() * scaleY) - pBone.getPivotY()) / scaleY),
				(pBone.getPivotZ() - ((pBone.getPivotZ() * scaleZ) - pBone.getPivotZ()) / scaleZ));

		pSourcePart.xRot = -pBone.getRotX();
		pSourcePart.yRot = -pBone.getRotY();
		pSourcePart.zRot = pBone.getRotZ();

		pPoseStack.scale(scaleX, scaleY, scaleZ);
	}

	protected Cube getReferenceCubeForModel(@NotNull BoneCache pBone, @NotNull ModelPart pSourcePart) {
		return pSourcePart.cubes.getFirst();
	}
}
