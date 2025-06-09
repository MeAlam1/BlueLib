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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.Color;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.client.loader.cache.model.CubeCache;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.client.GeoRenderProvider;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoArmorRenderer;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.renderer.GeoReplacedEntityRenderer;

@SuppressWarnings("unused")
public final class RenderUtils {

    public static void translateMatrixToBone(PoseStack pPoseStack, BoneCache pBone) {
        pPoseStack.translate(-pBone.getPosX() / 16f, pBone.getPosY() / 16f, pBone.getPosZ() / 16f);
    }

    public static void rotateMatrixAroundBone(PoseStack pPoseStack, BoneCache pBone) {
        if (pBone.getRotZ() != 0)
            pPoseStack.mulPose(Axis.ZP.rotation(pBone.getRotZ()));

        if (pBone.getRotY() != 0)
            pPoseStack.mulPose(Axis.YP.rotation(pBone.getRotY()));

        if (pBone.getRotX() != 0)
            pPoseStack.mulPose(Axis.XP.rotation(pBone.getRotX()));
    }

    public static void rotateMatrixAroundCube(PoseStack pPoseStack, CubeCache pCube) {
        Vec3 rotation = pCube.rotation();

        pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, 0, (float) rotation.z()));
        pPoseStack.mulPose(new Quaternionf().rotationXYZ(0, (float) rotation.y(), 0));
        pPoseStack.mulPose(new Quaternionf().rotationXYZ((float) rotation.x(), 0, 0));
    }

    public static void scaleMatrixForBone(PoseStack pPoseStack, BoneCache pBone) {
        pPoseStack.scale(pBone.getScaleX(), pBone.getScaleY(), pBone.getScaleZ());
    }

    public static void translateToPivotPoint(PoseStack pPoseStack, CubeCache pCube) {
        Vec3 pivot = pCube.pivot();
        pPoseStack.translate(pivot.x() / 16f, pivot.y() / 16f, pivot.z() / 16f);
    }

    public static void translateToPivotPoint(PoseStack pPoseStack, BoneCache pBone) {
        pPoseStack.translate(pBone.getPivotX() / 16f, pBone.getPivotY() / 16f, pBone.getPivotZ() / 16f);
    }

    public static void translateAwayFromPivotPoint(PoseStack pPoseStack, CubeCache pCube) {
        Vec3 pivot = pCube.pivot();

        pPoseStack.translate(-pivot.x() / 16f, -pivot.y() / 16f, -pivot.z() / 16f);
    }

    public static void translateAwayFromPivotPoint(PoseStack pPoseStack, BoneCache pBone) {
        pPoseStack.translate(-pBone.getPivotX() / 16f, -pBone.getPivotY() / 16f, -pBone.getPivotZ() / 16f);
    }

    public static void translateAndRotateMatrixForBone(PoseStack pPoseStack, BoneCache pBone) {
        translateToPivotPoint(pPoseStack, pBone);
        rotateMatrixAroundBone(pPoseStack, pBone);
    }

    public static void prepMatrixForBone(PoseStack pPoseStack, BoneCache pBone) {
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

        try {
            originalTexture = Minecraft.getInstance().submit(() -> TextureUtils.getTexture(pTexture)).get();
        } catch (Exception pException) {
            pException.printStackTrace();
        }

        if (originalTexture == null)
            return null;

        NativeImage image = null;

        try {
            image = originalTexture instanceof DynamicTexture dynamicTexture ? dynamicTexture.getPixels()
                    : NativeImage.read(ResourceUtils.getResource(pTexture).get().open());
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

    public static Vec3 listToVec(List<Float> pArray) {
        if (pArray.size() < 3) return new Vec3(0, 0, 0);
        return new Vec3(pArray.get(0), pArray.get(1), pArray.get(2));
    }

    public static void matchModelPartRot(ModelPart pModelPart, BoneCache pBone) {
        pBone.updateRotation(-pModelPart.xRot, -pModelPart.yRot, pModelPart.zRot);
    }

    public static void fixInvertedFlatCube(CubeCache pCube, Vector3f pNormal) {
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

    public static <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> boolean tryRenderArmorPiece(PoseStack pPoseStack, MultiBufferSource pBufferSource, T pEntity, ItemStack pStack, EquipmentSlot pEquipmentSlot, M pParentModel, A pBaseModel,
            float pPartialTick, int pPackedLight, float pLimbSwing, float pLimbSwingAmount, float pLerpedTickCount, float pNetHeadYaw, float pHeadPitch,
            BiConsumer<A, EquipmentSlot> pPartVisibilitySetter) {
        final Item item = pStack.getItem();

        if (!(item instanceof Equipable equipable) || equipable.getEquipmentSlot() != pEquipmentSlot)
            return false;

        final HumanoidModel<?> model = GeoRenderProvider.of(item).getGeoArmorRenderer(pEntity, pStack, pEquipmentSlot, pBaseModel);

        if (model == null)
            return false;

        pParentModel.copyPropertiesTo(pBaseModel);
        pPartVisibilitySetter.accept(pBaseModel, pEquipmentSlot);

        if (model instanceof GeoArmorRenderer<?> geoArmorRenderer)
            geoArmorRenderer.prepForRender(pEntity, pStack, pEquipmentSlot, pBaseModel, pBufferSource, pPartialTick, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch);

        pBaseModel.copyPropertiesTo((A) model);
        model.renderToBuffer(pPoseStack, null, pPackedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());

        return true;
    }
}
