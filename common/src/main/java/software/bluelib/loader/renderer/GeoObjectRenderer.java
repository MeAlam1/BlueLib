/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.renderer.layer.GeoRenderLayersContainer;
import software.bluelib.loader.util.RenderUtil;

public class GeoObjectRenderer<T extends GeoAnimatable> implements GeoRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;

    protected T animatable;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f objectRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    public GeoObjectRenderer(GeoModel<T> pModel) {
        this.model = pModel;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    @Override
    public T getAnimatable() {
        return this.animatable;
    }

    @Override
    public ResourceLocation getTextureLocation(T pAnimatable) {
        return GeoRenderer.super.getTextureLocation(pAnimatable);
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoObjectRenderer<T> addRenderLayer(GeoRenderLayer<T> pRenderLayer) {
        this.renderLayers.addLayer(pRenderLayer);

        return this;
    }

    public GeoObjectRenderer<T> withScale(float pScale) {
        return withScale(pScale, pScale);
    }

    public GeoObjectRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
        this.scaleWidth = pScaleWidth;
        this.scaleHeight = pScaleHeight;

        return this;
    }

    @ApiStatus.Internal
    public void render(PoseStack pPoseStack, T pAnimatable, @Nullable MultiBufferSource pBufferSource, @Nullable RenderType pRenderType,
            @Nullable VertexConsumer pBuffer, int pPackedLight, float pPartialTick) {
        this.animatable = pAnimatable;

        if (pBuffer == null)
            pBufferSource = Minecraft.getInstance().levelRenderer.renderBuffers.bufferSource();

        defaultRender(pPoseStack, pAnimatable, pBufferSource, pRenderType, pBuffer, 0, pPartialTick, pPackedLight);
    }

    @Override
    public void preRender(PoseStack pPoseStack, T pAnimatable, ModelCache pModel, @Nullable MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int pColour) {
        this.objectRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        scaleModelForRender(this.scaleWidth, this.scaleHeight, pPoseStack, pAnimatable, pModel, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay);

        pPoseStack.translate(0.5f, 0.51f, 0.5f);
    }

    @Override
    public void actuallyRender(PoseStack pPoseStack, T pAnimatable, ModelCache pModel, @Nullable RenderType pRenderType,
                               MultiBufferSource pBufferSource, @Nullable VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick,
                               int pPackedLight, int pPackedOverlay, int pColour) {
        pPoseStack.pushPose();

        if (!pIsReRender) {
            AnimationState<T> animationState = new AnimationState<>(pAnimatable, 0, 0, pPartialTick, false);
            long instanceId = getInstanceId(pAnimatable);
            GeoModel<T> currentModel = getGeoModel();

            currentModel.addAdditionalStateData(pAnimatable, instanceId, animationState::setData);
            currentModel.handleAnimations(pAnimatable, instanceId, animationState, pPartialTick);
        }

        this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        if (pBuffer != null)
            GeoRenderer.super.actuallyRender(pPoseStack, pAnimatable, pModel, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick,
                    pPackedLight, pPackedOverlay, pColour);

        pPoseStack.popPose();
    }

    @Override
    public void doPostRenderCleanup() {
        this.animatable = null;
    }

    @Override
    public void renderRecursively(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
                                  int pPackedOverlay, int pColour) {
        if (pBone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());

            pBone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            pBone.setLocalSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.objectRenderTranslations));
        }

        GeoRenderer.super.renderRecursively(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay,
                pColour);
    }

    @Override
    public void updateAnimatedTextureFrame(T pAnimatable) {
        AnimatableTexture.setAndUpdate(getTextureLocation(pAnimatable));
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileObjectRenderLayers(this);
    }

    @Override
    public boolean firePreRenderEvent(PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPreRender(this, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight);
    }

    @Override
    public void firePostRenderEvent(PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPostRender(this, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight);
    }
}
