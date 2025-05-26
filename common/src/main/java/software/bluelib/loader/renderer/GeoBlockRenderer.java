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
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.renderer.layer.GeoRenderLayersContainer;
import software.bluelib.loader.util.RenderUtil;

public class GeoBlockRenderer<T extends BlockEntity & GeoAnimatable> implements GeoRenderer<T>, BlockEntityRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;

    protected T animatable;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f blockRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    public GeoBlockRenderer(GeoModel<T> model) {
        this.model = model;
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
    public long getInstanceId(T animatable) {
        return animatable.getBlockPos().hashCode();
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoBlockRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);

        return this;
    }

    public GeoBlockRenderer<T> withScale(float scale) {
        return withScale(scale, scale);
    }

    public GeoBlockRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;

        return this;
    }

    @Override
    public void preRender(PoseStack pPoseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        this.blockRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        if (!pIsReRender)
            pPoseStack.translate(0.5, 0, 0.5);

        scaleModelForRender(this.scaleWidth, this.scaleHeight, pPoseStack, animatable, model, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay);
    }

    @Override
    @ApiStatus.Internal
    public void render(T animatable, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource,
            int pPackedLight, int pPackedOverlay) {
        this.animatable = animatable;

        defaultRender(pPoseStack, this.animatable, pBufferSource, null, null, 0, pPartialTick, pPackedLight);
    }

    @Override
    public void actuallyRender(PoseStack pPoseStack, T animatable, BakedGeoModel model, @Nullable RenderType pRenderType,
            MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
            int pPackedOverlay, int colour) {
        if (!pIsReRender) {
            AnimationState<T> animationState = new AnimationState<T>(animatable, 0, 0, pPartialTick, false);
            long instanceId = getInstanceId(animatable);
            GeoModel<T> currentModel = getGeoModel();

            animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
            animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
            currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            rotateBlock(getFacing(animatable), pPoseStack);
            currentModel.handleAnimations(animatable, instanceId, animationState, pPartialTick);
        }

        this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        if (buffer != null)
            GeoRenderer.super.actuallyRender(pPoseStack, animatable, model, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick,
                    pPackedLight, pPackedOverlay, colour);
    }

    @Override
    public void doPostRenderCleanup() {
        this.animatable = null;
    }

    @Override
    public void renderRecursively(PoseStack pPoseStack, T animatable, GeoBone bone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
            int pPackedOverlay, int colour) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());
            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
            Matrix4f worldState = new Matrix4f(localMatrix);
            BlockPos pos = this.animatable.getBlockPos();

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(localMatrix);
            bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
        }

        GeoRenderer.super.renderRecursively(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay,
                colour);
    }

    protected void rotateBlock(Direction facing, PoseStack pPoseStack) {
        switch (facing) {
            case SOUTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            case NORTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
            case EAST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
            case UP -> pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
            case DOWN -> pPoseStack.mulPose(Axis.XN.rotationDegrees(90));
        }
    }

    protected Direction getFacing(T block) {
        BlockState blockState = block.getBlockState();

        if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
            return blockState.getValue(HorizontalDirectionalBlock.FACING);

        if (blockState.hasProperty(DirectionalBlock.FACING))
            return blockState.getValue(DirectionalBlock.FACING);

        return Direction.NORTH;
    }

    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileBlockRenderLayers(this);
    }

    @Override
    public boolean firePreRenderEvent(PoseStack pPoseStack, BakedGeoModel model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireBlockPreRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
    }

    @Override
    public void firePostRenderEvent(PoseStack pPoseStack, BakedGeoModel model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireBlockPostRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
    }
}
