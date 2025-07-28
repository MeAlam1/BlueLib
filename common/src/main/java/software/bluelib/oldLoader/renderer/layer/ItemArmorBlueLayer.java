/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

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
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.CubeCache;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animatable.BlueItem;
import software.bluelib.oldLoader.renderer.BlueArmorRenderer;

public class ItemArmorBlueLayer<T extends LivingEntity & BlueAnimatable> extends BlueRenderLayer<T> {

	protected static final HumanoidModel<LivingEntity> INNER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
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

	public ItemArmorBlueLayer(BlueRenderer<T> blueRenderer) {
		super(blueRenderer);
	}

	@NotNull
	protected EquipmentSlot getEquipmentSlotForBone(BoneCache bone, ItemStack stack, T animatable) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				if (stack == animatable.getItemBySlot(slot))
					return slot;
			}
		}

		return EquipmentSlot.CHEST;
	}

	@NotNull
	protected ModelPart getModelPartForBone(BoneCache bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
		return baseModel.body;
	}

	@Nullable
	protected ItemStack getArmorItemForBone(BoneCache bone, T animatable) {
		return null;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.mainHandStack = pContext.animatable().getItemBySlot(EquipmentSlot.MAINHAND);
		this.offhandStack = pContext.animatable().getItemBySlot(EquipmentSlot.OFFHAND);
		this.helmetStack = pContext.animatable().getItemBySlot(EquipmentSlot.HEAD);
		this.chestplateStack = pContext.animatable().getItemBySlot(EquipmentSlot.CHEST);
		this.leggingsStack = pContext.animatable().getItemBySlot(EquipmentSlot.LEGS);
		this.bootsStack = pContext.animatable().getItemBySlot(EquipmentSlot.FEET);
	}

	@Override
	public void renderForBone(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource,
			VertexConsumer pBuffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		ItemStack armorStack = getArmorItemForBone(pBone, pAnimatable);

		if (armorStack == null)
			return;

		if (armorStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
			renderSkullAsArmor(pPoseStack, pBone, armorStack, skullBlock, pBufferSource, pPackedLight);
		} else {
			EquipmentSlot slot = getEquipmentSlotForBone(pBone, armorStack, pAnimatable);
			HumanoidModel<?> model = getModelForItem(pBone, slot, armorStack, pAnimatable);
			ModelPart modelPart = getModelPartForBone(pBone, slot, armorStack, pAnimatable, model);

			if (!modelPart.cubes.isEmpty()) {
				pPoseStack.pushPose();
				pPoseStack.scale(-1, -1, 1);

				if (model instanceof BlueArmorRenderer<?> BlueArmorRenderer) {
					prepModelPartForRender(pPoseStack, pBone, modelPart);
					BlueArmorRenderer.applyBoneVisibilityByPart(slot, modelPart, model);
					BlueArmorRenderer.renderToBuffer(pPoseStack, null, pPackedLight, pPackedOverlay, Color.WHITE.argbInt());
				} else if (armorStack.getItem() instanceof ArmorItem) {
					prepModelPartForRender(pPoseStack, pBone, modelPart);
					renderVanillaArmorPiece(pPoseStack, pAnimatable, pBone, slot, armorStack, modelPart, pBufferSource, pPartialTick, pPackedLight, pPackedOverlay);
				}

				pPoseStack.popPose();
			}
		}
	}

	protected <I extends Item & BlueItem> void renderVanillaArmorPiece(PoseStack pPoseStack, T animatable, BoneCache bone, EquipmentSlot slot, ItemStack armorStack,
			ModelPart modelPart, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		Holder<ArmorMaterial> material = ((ArmorItem) armorStack.getItem()).getMaterial();

		for (ArmorMaterial.Layer layer : material.value().layers()) {
			int color = armorStack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(armorStack, -6265536) : -1;
			VertexConsumer buffer = getVanillaArmorBuffer(pBufferSource, animatable, armorStack, slot, bone, layer, pPackedLight, pPackedOverlay, false);

			modelPart.render(pPoseStack, buffer, pPackedLight, pPackedOverlay, color);
		}

		ArmorTrim trim = armorStack.get(DataComponents.TRIM);

		if (trim != null) {
			TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(slot == EquipmentSlot.LEGS ? trim.innerTexture(material) : trim.outerTexture(material));
			VertexConsumer buffer = sprite.wrap(pBufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
			modelPart.render(pPoseStack, buffer, pPackedLight, pPackedOverlay);
		}

		if (armorStack.hasFoil())
			modelPart.render(pPoseStack, getVanillaArmorBuffer(pBufferSource, animatable, armorStack, slot, bone, null, pPackedLight, pPackedOverlay, true), pPackedLight, pPackedOverlay, Color.WHITE.argbInt());
	}

	protected VertexConsumer getVanillaArmorBuffer(MultiBufferSource pBufferSource, T animatable, ItemStack stack, EquipmentSlot slot, BoneCache bone, @Nullable ArmorMaterial.Layer layer, int pPackedLight, int pPackedOverlay, boolean forGlint) {
		if (forGlint)
			return pBufferSource.getBuffer(RenderType.armorEntityGlint());

		return pBufferSource.getBuffer(RenderType.armorCutoutNoCull(layer.texture(slot == EquipmentSlot.LEGS)));
	}

	@NotNull
	protected HumanoidModel<?> getModelForItem(BoneCache bone, EquipmentSlot slot, ItemStack stack, T animatable) {
		HumanoidModel<LivingEntity> defaultModel = slot == EquipmentSlot.LEGS ? INNER_ARMOR_MODEL : OUTER_ARMOR_MODEL;

		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getArmorModelForItem(animatable, stack, slot, defaultModel);
	}

	protected void renderSkullAsArmor(PoseStack pPoseStack, BoneCache bone, ItemStack stack, AbstractSkullBlock skullBlock, MultiBufferSource pBufferSource, int pPackedLight) {
		SkullBlock.Type type = skullBlock.getType();
		SkullModelBase model = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(type);
		RenderType pRenderType = SkullBlockRenderer.getRenderType(type, stack.get(DataComponents.PROFILE));

		pPoseStack.pushPose();
		RenderUtils.translateAndRotateMatrixForBone(pPoseStack, bone);
		pPoseStack.scale(1.1875f, 1.1875f, 1.1875f);
		pPoseStack.translate(-0.5f, 0, -0.5f);
		SkullBlockRenderer.renderSkull(null, 0, 0, pPoseStack, pBufferSource, pPackedLight, model, pRenderType);
		pPoseStack.popPose();
	}

	protected void prepModelPartForRender(PoseStack pPoseStack, BoneCache bone, ModelPart sourcePart) {
		final CubeCache firstCube = bone.getCubes().getFirst();
		final Cube armorCube = getReferenceCubeForModel(bone, sourcePart);
		final double armorBoneSizeX = firstCube.size().x();
		final double armorBoneSizeY = firstCube.size().y();
		final double armorBoneSizeZ = firstCube.size().z();
		final double actualArmorSizeX = Math.abs(armorCube.maxX - armorCube.minX);
		final double actualArmorSizeY = Math.abs(armorCube.maxY - armorCube.minY);
		final double actualArmorSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);
		float scaleX = (float) (armorBoneSizeX / actualArmorSizeX);
		float scaleY = (float) (armorBoneSizeY / actualArmorSizeY);
		float scaleZ = (float) (armorBoneSizeZ / actualArmorSizeZ);

		sourcePart.setPos(-(bone.getPivotX() - ((bone.getPivotX() * scaleX) - bone.getPivotX()) / scaleX),
				-(bone.getPivotY() - ((bone.getPivotY() * scaleY) - bone.getPivotY()) / scaleY),
				(bone.getPivotZ() - ((bone.getPivotZ() * scaleZ) - bone.getPivotZ()) / scaleZ));

		sourcePart.xRot = -bone.getRotX();
		sourcePart.yRot = -bone.getRotY();
		sourcePart.zRot = bone.getRotZ();

		pPoseStack.scale(scaleX, scaleY, scaleZ);
	}

	protected Cube getReferenceCubeForModel(BoneCache bone, ModelPart sourcePart) {
		return sourcePart.cubes.getFirst();
	}
}
