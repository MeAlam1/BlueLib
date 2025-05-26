/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.*;
import software.bluelib.loader.loading.math.MolangQueries;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.util.Color;
import software.bluelib.loader.util.RenderUtil;

// TODO Split sources support

public interface GeoRenderer<T extends GeoAnimatable> {

    GeoModel<T> getGeoModel();

    T getAnimatable();

    default ResourceLocation getTextureLocation(T pAnimatable) {
        return getGeoModel().getTextureResource(pAnimatable, this);
    }

    default List<GeoRenderLayer<T>> getRenderLayers() {
        return List.of();
    }

    @Nullable
    default RenderType getRenderType(T pAnimatable, ResourceLocation pTexture,
            @Nullable MultiBufferSource pBufferSource,
            float pPartialTick) {
        return getGeoModel().getRenderType(pAnimatable, pTexture);
    }

    default Color getRenderColor(T pAnimatable, float pPartialTick, int pPackedLight) {
        return Color.WHITE;
    }

    default int getPackedOverlay(T pAnimatable, float pU, float pPartialTick) {
        return OverlayTexture.NO_OVERLAY;
    }

    default long getInstanceId(T pAnimatable) {
        return pAnimatable.hashCode();
    }

    default float getMotionAnimThreshold(T pAnimatable) {
        return 0.015f;
    }

    default void defaultRender(PoseStack pPoseStack, T pAnimatable, MultiBufferSource pBufferSource, @Nullable RenderType pRenderType, @Nullable VertexConsumer pBuffer,
            float yaw, float pPartialTick, int pPackedLight) {
        pPoseStack.pushPose();

        int renderColor = getRenderColor(pAnimatable, pPartialTick, pPackedLight).argbInt();
        int pPackedOverlay = getPackedOverlay(pAnimatable, 0, pPartialTick);
        BakedGeoModel pModel = getGeoModel().getBakedModel(getGeoModel().getModelResource(pAnimatable, this));

        if (pRenderType == null)
            pRenderType = getRenderType(pAnimatable, getTextureLocation(pAnimatable), pBufferSource, pPartialTick);

        if (pBuffer == null && pRenderType != null)
            pBuffer = pBufferSource.getBuffer(pRenderType);

        preRender(pPoseStack, pAnimatable, pModel, pBufferSource, pBuffer, false, pPartialTick, pPackedLight, pPackedOverlay, renderColor);

        if (firePreRenderEvent(pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight)) {
            preApplyRenderLayers(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, pPackedLight, pPackedLight, pPackedOverlay);
            actuallyRender(pPoseStack, pAnimatable, pModel, pRenderType,
                    pBufferSource, pBuffer, false, pPartialTick, pPackedLight, pPackedOverlay, renderColor);
            applyRenderLayers(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);
            postRender(pPoseStack, pAnimatable, pModel, pBufferSource, pBuffer, false, pPartialTick, pPackedLight, pPackedOverlay, renderColor);
            firePostRenderEvent(pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight);
        }

        pPoseStack.popPose();

        renderFinal(pPoseStack, pAnimatable, pModel, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay, renderColor);
        doPostRenderCleanup();
        MolangQueries.clearActor();
    }

    default void reRender(BakedGeoModel pModel, PoseStack pPoseStack, MultiBufferSource pBufferSource, T pAnimatable,
            RenderType pRenderType, VertexConsumer pBuffer, float pPartialTick,
            int pPackedLight, int pPackedOverlay, int pColour) {
        pPoseStack.pushPose();
        preRender(pPoseStack, pAnimatable, pModel, pBufferSource, pBuffer, true, pPartialTick, pPackedLight, pPackedOverlay, pColour);
        actuallyRender(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, true, pPartialTick, pPackedLight, pPackedOverlay, pColour);
        postRender(pPoseStack, pAnimatable, pModel, pBufferSource, pBuffer, true, pPartialTick, pPackedLight, pPackedOverlay, pColour);
        pPoseStack.popPose();
    }

    default void actuallyRender(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, @Nullable RenderType pRenderType,
            MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick,
            int pPackedLight, int pPackedOverlay, int pColour) {
        if (pBuffer == null) {
            if (pRenderType == null)
                return;

            pBuffer = pBufferSource.getBuffer(pRenderType);
        }

        updateAnimatedTextureFrame(pAnimatable);

        for (GeoBone group : pModel.topLevelBones()) {
            renderRecursively(pPoseStack, pAnimatable, group, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight,
                    pPackedOverlay, pColour);
        }
    }

    default void preApplyRenderLayers(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource,
            @Nullable VertexConsumer pBuffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
            renderLayer.preRender(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);
        }
    }

    default void applyRenderLayersForBone(PoseStack pPoseStack, T pAnimatable, GeoBone bone, RenderType pRenderType, MultiBufferSource pBufferSource,
            VertexConsumer pBuffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
            renderLayer.renderForBone(pPoseStack, pAnimatable, bone, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);
        }
    }

    // TODO append renderColor to layers

    default void applyRenderLayers(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource,
            @Nullable VertexConsumer pBuffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        for (GeoRenderLayer<T> renderLayer : getRenderLayers()) {
            renderLayer.render(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);
        }
    }

    default void preRender(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, @Nullable MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
            int pPackedOverlay, int pColour) {}

    default void postRender(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {}

    default void renderFinal(PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, float pPartialTick, int pPackedLight,
            int pPackedOverlay, int pColour) {}

    default void doPostRenderCleanup() {}

    default void renderRecursively(PoseStack pPoseStack, T pAnimatable, GeoBone pBone, RenderType pRenderType, MultiBufferSource pBufferSource,
            VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
            int pPackedOverlay, int pColour) {
        pPoseStack.pushPose();
        RenderUtil.prepMatrixForBone(pPoseStack, pBone);

        pBuffer = checkAndRefreshBuffer(pIsReRender, pBuffer, pBufferSource, pRenderType);

        renderCubesOfBone(pPoseStack, pBone, pBuffer, pPackedLight, pPackedOverlay, pColour);

        if (!pIsReRender)
            applyRenderLayersForBone(pPoseStack, getAnimatable(), pBone, pRenderType, pBufferSource, pBuffer, pPartialTick, pPackedLight, pPackedOverlay);

        renderChildBones(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, pColour);
        pPoseStack.popPose();
    }

    default void renderCubesOfBone(PoseStack pPoseStack, GeoBone pBone, VertexConsumer pBuffer, int pPackedLight,
            int pPackedOverlay, int pColour) {
        if (pBone.isHidden())
            return;

        for (GeoCube cube : pBone.getCubes()) {
            pPoseStack.pushPose();
            renderCube(pPoseStack, cube, pBuffer, pPackedLight, pPackedOverlay, pColour);
            pPoseStack.popPose();
        }
    }

    default void renderChildBones(PoseStack pPoseStack, T pAnimatable, GeoBone pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer,
            boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
        if (pBone.isHidingChildren())
            return;

        for (GeoBone childBone : pBone.getChildBones()) {
            renderRecursively(pPoseStack, pAnimatable, childBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, pColour);
        }
    }

    default void renderCube(PoseStack pPoseStack, GeoCube pCube, VertexConsumer pBuffer, int pPackedLight,
            int pPackedOverlay, int pColour) {
        RenderUtil.translateToPivotPoint(pPoseStack, pCube);
        RenderUtil.rotateMatrixAroundCube(pPoseStack, pCube);
        RenderUtil.translateAwayFromPivotPoint(pPoseStack, pCube);

        Matrix3f normalisedPoseState = pPoseStack.last().normal();
        Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());

        for (GeoQuad quad : pCube.quads()) {
            if (quad == null)
                continue;

            Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal()));

            RenderUtil.fixInvertedFlatCube(pCube, normal);
            createVerticesOfQuad(quad, poseState, normal, pBuffer, pPackedLight, pPackedOverlay, pColour);
        }
    }

    default void createVerticesOfQuad(GeoQuad pQuad, Matrix4f pPoseState, Vector3f pNormal, VertexConsumer pBuffer,
            int pPackedLight, int pPackedOverlay, int pColour) {
        for (GeoVertex vertex : pQuad.vertices()) {
            Vector3f position = vertex.position();
            Vector4f vector4f = pPoseState.transform(new Vector4f(position.x(), position.y(), position.z(), 1.0f));

            pBuffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), pColour, vertex.texU(),
                    vertex.texV(), pPackedOverlay, pPackedLight, pNormal.x(), pNormal.y(), pNormal.z());
        }
    }

    void fireCompileRenderLayersEvent();

    boolean firePreRenderEvent(PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    void firePostRenderEvent(PoseStack pPoseStack, BakedGeoModel pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight);

    default void scaleModelForRender(float widthScale, float heightScale, PoseStack pPoseStack, T pAnimatable, BakedGeoModel pModel, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        if (!pIsReRender && (widthScale != 1 || heightScale != 1))
            pPoseStack.scale(widthScale, heightScale, widthScale);
    }

    void updateAnimatedTextureFrame(T pAnimatable);

    //@Deprecated(forRemoval = true)
    @ApiStatus.Internal
    default VertexConsumer checkAndRefreshBuffer(boolean pIsReRender, VertexConsumer pBuffer, MultiBufferSource pBufferSource, RenderType pRenderType) {
        if (pIsReRender)
            return pBuffer;

        return switch (pBuffer) {
            case BufferBuilder builder when !builder.building -> pBufferSource.getBuffer(pRenderType);
            case OutlineBufferSource.EntityOutlineGenerator outlines when bufferNeedsRefresh(outlines.delegate()) -> new OutlineBufferSource.EntityOutlineGenerator(pBufferSource.getBuffer(pRenderType), outlines.color());
            case VertexMultiConsumer.Double pair when bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second) -> new VertexMultiConsumer.Double(bufferNeedsRefresh(pair.first) ? pBufferSource.getBuffer(pRenderType) : pair.first, bufferNeedsRefresh(pair.second) ? pBufferSource.getBuffer(pRenderType) : pair.second);
            default -> pBuffer;
        };
    }

    //@Deprecated(forRemoval = true)
    @ApiStatus.Internal
    private boolean bufferNeedsRefresh(VertexConsumer pBuffer) {
        return switch (pBuffer) {
            case BufferBuilder builder -> !builder.building;
            case OutlineBufferSource.EntityOutlineGenerator outlines -> bufferNeedsRefresh(outlines.delegate());
            case VertexMultiConsumer.Double pair -> bufferNeedsRefresh(pair.first) || bufferNeedsRefresh(pair.second);
            default -> false;
        };
    }
}
