/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.utils;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.Color;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.CubeCache;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animatable.client.BlueRenderProvider;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueArmorRenderer;
import software.bluelib.oldLoader.renderer.BlueRenderer;
import software.bluelib.oldLoader.renderer.BlueReplacedEntityRenderer;

@SuppressWarnings("unused")
public final class RenderUtils {

	public static void translateMatrixToBone(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		pPoseStack.translate(-pBone.getPosX() / 16f, pBone.getPosY() / 16f, pBone.getPosZ() / 16f);
	}

	public static void rotateMatrixAroundBone(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		if (pBone.getRotZ() != 0)
			pPoseStack.mulPose(Axis.ZP.rotation(pBone.getRotZ()));

		if (pBone.getRotY() != 0)
			pPoseStack.mulPose(Axis.YP.rotation(pBone.getRotY()));

		if (pBone.getRotX() != 0)
			pPoseStack.mulPose(Axis.XP.rotation(pBone.getRotX()));
	}

	public static void rotateMatrixAroundCube(@NotNull PoseStack pPoseStack, @NotNull CubeCache pCube) {
		Vec3 rotation = pCube.rotation();

		pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, 0, (float) rotation.z()));
		pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, (float) rotation.y(), 0));
		pPoseStack.mulPose(new Quaternionf().rotationXYZ((float) rotation.x(), 0, 0));
	}

	public static void scaleMatrixForBone(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		pPoseStack.scale(pBone.getScaleX(), pBone.getScaleY(), pBone.getScaleZ());
	}

	public static void translateToPivotPoint(@NotNull PoseStack pPoseStack, @NotNull CubeCache pCube) {
		Vec3 pivot = pCube.pivot();
		pPoseStack.translate(pivot.x() / 16f, pivot.y() / 16f, pivot.z() / 16f);
	}

	public static void translateToPivotPoint(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		pPoseStack.translate(pBone.getPivotX() / 16f, pBone.getPivotY() / 16f, pBone.getPivotZ() / 16f);
	}

	public static void translateAwayFromPivotPoint(@NotNull PoseStack pPoseStack, @NotNull CubeCache pCube) {
		Vec3 pivot = pCube.pivot();

		pPoseStack.translate(-pivot.x() / 16f, -pivot.y() / 16f, -pivot.z() / 16f);
	}

	public static void translateAwayFromPivotPoint(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		pPoseStack.translate(-pBone.getPivotX() / 16f, -pBone.getPivotY() / 16f, -pBone.getPivotZ() / 16f);
	}

	public static void translateAndRotateMatrixForBone(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		translateToPivotPoint(pPoseStack, pBone);
		rotateMatrixAroundBone(pPoseStack, pBone);
	}

	public static void prepMatrixForBone(@NotNull PoseStack pPoseStack, @NotNull BoneCache pBone) {
		translateMatrixToBone(pPoseStack, pBone);
		translateToPivotPoint(pPoseStack, pBone);
		rotateMatrixAroundBone(pPoseStack, pBone);
		scaleMatrixForBone(pPoseStack, pBone);
		translateAwayFromPivotPoint(pPoseStack, pBone);
	}

	public static Matrix4f invertAndMultiplyMatrices(@NotNull Matrix4f pBaseMatrix, @NotNull Matrix4f pInputMatrix) {
		pInputMatrix = new Matrix4f(pInputMatrix);

		pInputMatrix.invert();
		pInputMatrix.mul(pBaseMatrix);

		return pInputMatrix;
	}

	public static void faceRotation(@NotNull PoseStack pPoseStack, @NotNull Entity pAnimatable, @NotNull Float pPartialTick) {
		pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pAnimatable.yRotO, pAnimatable.getYRot()) - 90));
		pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pAnimatable.xRotO, pAnimatable.getXRot())));
	}

	public static Matrix4f translateMatrix(@NotNull Matrix4f pMatrix, @NotNull Vector3f pVector) {
		return pMatrix.add(new Matrix4f().m30(pVector.x).m31(pVector.y).m32(pVector.z));
	}

	@Nullable
	public static IntIntPair getTextureDimensions(@NotNull ResourceLocation pTexture) {
		AbstractTexture originalTexture = null;

		try {
			originalTexture = Minecraft.getInstance().submit(() -> TextureUtils.getTexture(pTexture)).get();
		} catch (@NotNull Exception pException) {
			pException.printStackTrace();
		}

		if (originalTexture == null)
			return null;

		NativeImage image = null;

		try {
			image = originalTexture instanceof DynamicTexture dynamicTexture ? dynamicTexture.getPixels()
					: NativeImage.read(ResourceUtils.getResource(pTexture).get().open());
		} catch (@NotNull Exception pException) {
			pException.printStackTrace();
		}

		return image == null ? null : IntIntImmutablePair.of(image.getWidth(), image.getHeight());
	}

	public static @NotNull Double getCurrentSystemTick() {
		return System.nanoTime() / 1E6 / 50d;
	}

	public static @NotNull Double getCurrentTick() {
		return Blaze3D.getTime() * 20d;
	}

	public static @NotNull Float booleanToFloat(boolean pInput) {
		return pInput ? 1f : 0f;
	}

	public static @NotNull Vec3 listToVec(@NotNull List<Float> pArray) {
		if (pArray.size() < 3) return new Vec3(0, 0, 0);
		return new Vec3(pArray.get(0), pArray.get(1), pArray.get(2));
	}

	public static void matchModelPartRot(@NotNull ModelPart pModelPart, @NotNull BoneCache pBone) {
		pBone.updateRotation(-pModelPart.xRot, -pModelPart.yRot, pModelPart.zRot);
	}

	public static void fixInvertedFlatCube(@NotNull CubeCache pCube, @NotNull Vector3f pNormal) {
		if (pNormal.x() < 0 && (pCube.size().y() == 0 || pCube.size().z() == 0))
			pNormal.mul(-1, 1, 1);

		if (pNormal.y() < 0 && (pCube.size().x() == 0 || pCube.size().z() == 0))
			pNormal.mul(1, -1, 1);

		if (pNormal.z() < 0 && (pCube.size().x() == 0 || pCube.size().y() == 0))
			pNormal.mul(1, 1, -1);
	}

	public static @NotNull Float getDirectionAngle(@NotNull Direction pDirection) {
		return switch (pDirection) {
			case SOUTH -> 90f;
			case NORTH -> 270f;
			case EAST -> 180f;
			default -> 0f;
		};
	}

	public static @NotNull Double lerpYaw(@NotNull Double pDelta, @NotNull Double pStart, @NotNull Double pEnd) {
		pStart = Mth.wrapDegrees(pStart);
		pEnd = Mth.wrapDegrees(pEnd);
		double diff = pStart - pEnd;
		pEnd = diff > 180 || diff < -180 ? pStart + Math.copySign(360 - Math.abs(diff), diff) : pEnd;

		return Mth.lerp(pDelta, pStart, pEnd);
	}

	@Nullable
	public static BlueModel<?> getBlueModelForEntityType(@NotNull EntityType<?> pEntityType) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(pEntityType);

		return renderer instanceof BlueRenderer<?> blueRenderer ? blueRenderer.getBlueModel() : null;
	}

	@Nullable
	public static BlueAnimatable getReplacedAnimatable(@NotNull EntityType<?> pEntityType) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(pEntityType);

		return renderer instanceof BlueReplacedEntityRenderer<?, ?> replacedEntityRenderer ? replacedEntityRenderer.getAnimatable() : null;
	}

	@Nullable
	public static BlueModel<?> getBlueModelForEntity(@NotNull Entity pEntity) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(pEntity);

		return renderer instanceof BlueRenderer<?> blueRenderer ? blueRenderer.getBlueModel() : null;
	}

	@Nullable
	public static BlueModel<?> getBlueModelForItem(@NotNull ItemStack pItem) {
		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getBlueModelForItem(pItem);
	}

	@Nullable
	public static BlueModel<?> getBlueModelForBlock(@NotNull BlockEntity pBlockEntity) {
		BlockEntityRenderer<?> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(pBlockEntity);

		return renderer instanceof BlueRenderer<?> blueRenderer ? blueRenderer.getBlueModel() : null;
	}

	@Nullable
	public static BlueModel<?> getBlueModelForArmor(@NotNull ItemStack pStack) {
		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getBlueModelForArmor(pStack);
	}

	public static @NotNull <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> Boolean tryRenderArmorPiece(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, @NotNull T pEntity, @NotNull ItemStack pStack, @NotNull EquipmentSlot pEquipmentSlot, @NotNull M pParentModel, @NotNull A pBaseModel,
			@NotNull Float pPartialTick, @NotNull Integer pPackedLight, @NotNull Float pLimbSwing, @NotNull Float pLimbSwingAmount, @NotNull Float pLerpedTickCount, @NotNull Float pNetHeadYaw, @NotNull Float pHeadPitch,
			@NotNull BiConsumer<A, EquipmentSlot> pPartVisibilitySetter) {
		final Item item = pStack.getItem();

		if (!(item instanceof Equipable equipable) || equipable.getEquipmentSlot() != pEquipmentSlot)
			return false;

		final HumanoidModel<?> model = BlueRenderProvider.of(item).getBlueArmorRenderer(pEntity, pStack, pEquipmentSlot, pBaseModel);

		if (model == null)
			return false;

		pParentModel.copyPropertiesTo(pBaseModel);
		pPartVisibilitySetter.accept(pBaseModel, pEquipmentSlot);

		if (model instanceof BlueArmorRenderer<?> BlueArmorRenderer)
			BlueArmorRenderer.prepForRender(pEntity, pStack, pEquipmentSlot, pBaseModel, pBufferSource, pPartialTick, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch);

		pBaseModel.copyPropertiesTo((A) model);
		model.renderToBuffer(pPoseStack, null, pPackedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());

		return true;
	}
}
