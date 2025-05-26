/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.specialty;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.client.loader.json.model.object.VertexData;
import software.bluelib.client.loader.json.model.object.QuadData;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoItemRenderer;
import software.bluelib.loader.util.RenderUtil;

public abstract class DynamicGeoItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {

    protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap<>();

    protected ResourceLocation textureOverride = null;

    public DynamicGeoItemRenderer(GeoModel<T> model) {
        super(model);
    }

    @Nullable
    protected ResourceLocation getTextureOverrideForBone(BoneCache bone, T animatable, float pPartialTick) {
        return null;
    }

    @Nullable
    protected RenderType getRenderTypeOverrideForBone(BoneCache bone, T animatable, ResourceLocation texturePath, MultiBufferSource pBufferSource, float pPartialTick) {
        return null;
    }

    protected boolean boneRenderOverride(PoseStack pPoseStack, BoneCache bone, MultiBufferSource pBufferSource, VertexConsumer buffer,
                                         float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        return false;
    }

    @Override
    public void renderRecursively(PoseStack pPoseStack, T animatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
        pPoseStack.pushPose();
        RenderUtil.translateMatrixToBone(pPoseStack, pBone);
        RenderUtil.translateToPivotPoint(pPoseStack, pBone);
        RenderUtil.rotateMatrixAroundBone(pPoseStack, pBone);
        RenderUtil.scaleMatrixForBone(pPoseStack, pBone);

        if (pBone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());

            pBone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            pBone.setLocalSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.itemRenderTranslations));
        }

        RenderUtil.translateAwayFromPivotPoint(pPoseStack, pBone);

        this.textureOverride = getTextureOverrideForBone(pBone, this.animatable, pPartialTick);
        ResourceLocation texture = this.textureOverride == null ? getTextureLocation(this.animatable) : this.textureOverride;
        RenderType renderTypeOverride = getRenderTypeOverrideForBone(pBone, this.animatable, texture, pBufferSource, pPartialTick);

        if (texture != null && renderTypeOverride == null)
            renderTypeOverride = getRenderType(this.animatable, texture, pBufferSource, pPartialTick);

        if (renderTypeOverride != null)
            pBuffer = pBufferSource.getBuffer(renderTypeOverride);

        if (!boneRenderOverride(pPoseStack, pBone, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay, pColour))
            super.renderCubesOfBone(pPoseStack, pBone, pBuffer, pPackedLight, pPackedOverlay, pColour);

        if (renderTypeOverride != null)
            pBuffer = pBufferSource.getBuffer(pRenderType);

        if (!pIsReRender)
            applyRenderLayersForBone(pPoseStack, animatable, pBone, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);

        pBuffer = checkAndRefreshBuffer(pIsReRender, pBuffer, pBufferSource, pRenderType);

        super.renderChildBones(pPoseStack, animatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, pColour);

        pPoseStack.popPose();
    }

    @Override
    public void postRender(PoseStack pPoseStack, T animatable, ModelCache model, MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        this.textureOverride = null;

        super.postRender(pPoseStack, animatable, model, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, colour);
    }

    @Override
    public void createVerticesOfQuad(QuadData quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer,
                                     int pPackedLight, int pPackedOverlay, int colour) {
        if (this.textureOverride == null) {
            super.createVerticesOfQuad(quad, poseState, normal, buffer, pPackedLight, pPackedOverlay,
                    colour);

            return;
        }

        IntIntPair boneTextureSize = computeTextureSize(this.textureOverride);
        IntIntPair itemTextureSize = computeTextureSize(getTextureLocation(this.animatable));

        if (boneTextureSize == null || itemTextureSize == null) {
            super.createVerticesOfQuad(quad, poseState, normal, buffer, pPackedLight, pPackedOverlay,
                    colour);

            return;
        }

        for (VertexData vertex : quad.vertices()) {
            Vector4f vector4f = poseState.transform(new Vector4f(vertex.position().x(), vertex.position().y(), vertex.position().z(), 1.0f));
            float texU = (vertex.texU() * itemTextureSize.firstInt()) / boneTextureSize.firstInt();
            float texV = (vertex.texV() * itemTextureSize.secondInt()) / boneTextureSize.secondInt();

            buffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), colour, texU, texV,
                    pPackedOverlay, pPackedLight, normal.x(), normal.y(), normal.z());
        }
    }

    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(texture, RenderUtil::getTextureDimensions);
    }
}
