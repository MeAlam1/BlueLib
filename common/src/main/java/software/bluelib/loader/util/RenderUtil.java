/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.util;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.cache.object.GeoCube;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.renderer.GeoReplacedEntityRenderer;

@SuppressWarnings("unused")
public final class RenderUtil {

	public static void translateMatrixToBone(PoseStack pPoseStack, GeoBone pBone) {
		pPoseStack.translate(-pBone.getPosX() / 16f, pBone.getPosY() / 16f, pBone.getPosZ() / 16f);
	}

	public static void rotateMatrixAroundBone(PoseStack pPoseStack, GeoBone pBone) {
		if (pBone.getRotZ() != 0)
			pPoseStack.mulPose(Axis.ZP.rotation(pBone.getRotZ()));

		if (pBone.getRotY() != 0)
			pPoseStack.mulPose(Axis.YP.rotation(pBone.getRotY()));

		if (pBone.getRotX() != 0)
			pPoseStack.mulPose(Axis.XP.rotation(pBone.getRotX()));
	}

	public static void rotateMatrixAroundCube(PoseStack pPoseStack, GeoCube pCube) {
		Vec3 rotation = pCube.rotation();

		pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, 0, (float) rotation.z()));
		pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, (float) rotation.y(), 0));
		pPoseStack.mulPose(new Quaternionf().rotationXYZ((float) rotation.x(), 0, 0));
	}

	public static void scaleMatrixForBone(PoseStack pPoseStack, GeoBone pBone) {
		pPoseStack.scale(pBone.getScaleX(), pBone.getScaleY(), pBone.getScaleZ());
	}

	public static void translateToPivotPoint(PoseStack pPoseStack, GeoCube pCube) {
		Vec3 pivot = pCube.pivot();
		pPoseStack.translate(pivot.x() / 16f, pivot.y() / 16f, pivot.z() / 16f);
	}

	public static void translateToPivotPoint(PoseStack pPoseStack, GeoBone pBone) {
		pPoseStack.translate(pBone.getPivotX() / 16f, pBone.getPivotY() / 16f, pBone.getPivotZ() / 16f);
	}

	public static void translateAwayFromPivotPoint(PoseStack pPoseStack, GeoCube pCube) {
		Vec3 pivot = pCube.pivot();

		pPoseStack.translate(-pivot.x() / 16f, -pivot.y() / 16f, -pivot.z() / 16f);
	}

	public static void translateAwayFromPivotPoint(PoseStack pPoseStack, GeoBone pBone) {
		pPoseStack.translate(-pBone.getPivotX() / 16f, -pBone.getPivotY() / 16f, -pBone.getPivotZ() / 16f);
	}

	public static void translateAndRotateMatrixForBone(PoseStack pPoseStack, GeoBone pBone) {
		translateToPivotPoint(pPoseStack, pBone);
		rotateMatrixAroundBone(pPoseStack, pBone);
	}

	public static void prepMatrixForBone(PoseStack pPoseStack, GeoBone pBone) {
		translateMatrixToBone(pPoseStack, pBone);
		translateToPivotPoint(pPoseStack, pBone);
		rotateMatrixAroundBone(pPoseStack, pBone);
		scaleMatrixForBone(pPoseStack, pBone);
		translateAwayFromPivotPoint(pPoseStack, pBone);
	}

	public static Matrix4f invertAndMultiplyMatrices(Matrix4f pBaseMatrix, Matrix4f pInputMatrix) {
		pInputMatrix = new Matrix4f(pInputMatrix);

		pInputMatrix.invert();
		pInputMatrix.mul(pBaseMatrix);

		return pInputMatrix;
	}

	public static void faceRotation(PoseStack pPoseStack, Entity pAnimatable, float pPartialTick) {
		pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pAnimatable.yRotO, pAnimatable.getYRot()) - 90));
		pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pAnimatable.xRotO, pAnimatable.getXRot())));
	}

	public static Matrix4f translateMatrix(Matrix4f pMatrix, Vector3f pVector) {
		return pMatrix.add(new Matrix4f().m30(pVector.x).m31(pVector.y).m32(pVector.z));
	}

	@Nullable
	public static IntIntPair getTextureDimensions(ResourceLocation pTexture) {
		if (pTexture == null)
			return null;

		AbstractTexture originalTexture = null;
		Minecraft mc = Minecraft.getInstance();

		try {
			originalTexture = mc.submit(() -> mc.getTextureManager().getTexture(pTexture)).get();
		} catch (Exception pException) {
			pException.printStackTrace();
		}

		if (originalTexture == null)
			return null;

		NativeImage image = null;

		try {
			image = originalTexture instanceof DynamicTexture dynamicTexture ? dynamicTexture.getPixels()
					: NativeImage.read(mc.getResourceManager().getResource(pTexture).get().open());
		} catch (Exception pException) {
			pException.printStackTrace();
		}

		return image == null ? null : IntIntImmutablePair.of(image.getWidth(), image.getHeight());
	}

	public static double getCurrentSystemTick() {
		return System.nanoTime() / 1E6 / 50d;
	}

	public static double getCurrentTick() {
		return Blaze3D.getTime() * 20d;
	}

	public static float booleanToFloat(boolean pInput) {
		return pInput ? 1f : 0f;
	}

	public static Vec3 arrayToVec(double[] pArray) {
		return new Vec3(pArray[0], pArray[1], pArray[2]);
	}

	public static void matchModelPartRot(ModelPart pModelPart, GeoBone pBone) {
		pBone.updateRotation(-pModelPart.xRot, -pModelPart.yRot, pModelPart.zRot);
	}

	public static void fixInvertedFlatCube(GeoCube pCube, Vector3f pNormal) {
		if (pNormal.x() < 0 && (pCube.size().y() == 0 || pCube.size().z() == 0))
			pNormal.mul(-1, 1, 1);

		if (pNormal.y() < 0 && (pCube.size().x() == 0 || pCube.size().z() == 0))
			pNormal.mul(1, -1, 1);

		if (pNormal.z() < 0 && (pCube.size().x() == 0 || pCube.size().y() == 0))
			pNormal.mul(1, 1, -1);
	}

	public static float getDirectionAngle(Direction pDirection) {
		return switch (pDirection) {
			case SOUTH -> 90f;
			case NORTH -> 270f;
			case EAST -> 180f;
			default -> 0f;
		};
	}

	public static double lerpYaw(double pDelta, double pStart, double pEnd) {
		pStart = Mth.wrapDegrees(pStart);
		pEnd = Mth.wrapDegrees(pEnd);
		double diff = pStart - pEnd;
		pEnd = diff > 180 || diff < -180 ? pStart + Math.copySign(360 - Math.abs(diff), diff) : pEnd;

		return Mth.lerp(pDelta, pStart, pEnd);
	}

	@Nullable
	public static GeoModel<?> getGeoModelForEntityType(EntityType<?> pEntityType) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(pEntityType);

		return renderer instanceof GeoRenderer<?> geoRenderer ? geoRenderer.getGeoModel() : null;
	}

	@Nullable
	public static GeoAnimatable getReplacedAnimatable(EntityType<?> pEntityType) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(pEntityType);

		return renderer instanceof GeoReplacedEntityRenderer<?, ?> replacedEntityRenderer ? replacedEntityRenderer.getAnimatable() : null;
	}

	@Nullable
	public static GeoModel<?> getGeoModelForEntity(Entity pEntity) {
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(pEntity);

		return renderer instanceof GeoRenderer<?> geoRenderer ? geoRenderer.getGeoModel() : null;
	}

	@Nullable
	public static GeoModel<?> getGeoModelForItem(ItemStack pItem) {
		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getGeoModelForItem(pItem);
	}

	@Nullable
	public static GeoModel<?> getGeoModelForBlock(BlockEntity pBlockEntity) {
		BlockEntityRenderer<?> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(pBlockEntity);

		return renderer instanceof GeoRenderer<?> geoRenderer ? geoRenderer.getGeoModel() : null;
	}

	@Nullable
	public static GeoModel<?> getGeoModelForArmor(ItemStack pStack) {
		return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getGeoModelForArmor(pStack);
	}
}
