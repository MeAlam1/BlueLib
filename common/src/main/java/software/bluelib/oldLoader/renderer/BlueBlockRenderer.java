/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayer;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayersContainer;

import java.util.List;

public class BlueBlockRenderer<T extends BlockEntity & BlueAnimatable> implements BlueRenderer<T>, BlockEntityRenderer<T> {

	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	protected final BlueModel<T> model;

	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f blockRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueBlockRenderer(BlueModel<T> model) {
		this.model = model;
	}

	@Override
	public BlueModel<T> getBlueModel() {
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
	public List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	public BlueBlockRenderer<T> addRenderLayer(BlueRenderLayer<T> renderLayer) {
		this.renderLayers.addLayer(renderLayer);

		return this;
	}

	public BlueBlockRenderer<T> withScale(float scale) {
		return withScale(scale, scale);
	}

	public BlueBlockRenderer<T> withScale(float scaleWidth, float scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;

		return this;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.blockRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		if (!pContext.isReRender())
			pContext.poseStack().translate(0.5, 0, 0.5);

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext.poseStack(), pContext.animatable(), pContext.model(), pContext.isReRender(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay());
	}

	@Override
	@ApiStatus.Internal
	public void render(T animatable, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource,
	                   int pPackedLight, int pPackedOverlay) {
		this.animatable = animatable;

		defaultRender(new BaseRenderContext<>(
				pPoseStack,
				this.animatable,
				this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
				pBufferSource,
				false, // isReRender
				pPartialTick,
				pPackedLight,
				getPackedOverlay(this.animatable, 0, pPartialTick),
				getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt()));
	}

	@Override
	public void actuallyRender(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			PoseStack pPoseStack = full.poseStack();
			T animatable = full.animatable();
			VertexConsumer buffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<>(animatable, 0, 0, pPartialTick, false);
				long instanceId = getInstanceId(animatable);
				BlueModel<T> currentModel = getBlueModel();

				animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
				animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
				currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
				rotateBlock(getFacing(animatable), pPoseStack);
				currentModel.handleAnimations(animatable, instanceId, animationState, pPartialTick);
			}

			this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

			if (buffer != null)
				BlueRenderer.super.actuallyRender(full);
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@Override
	public void doPostRenderCleanup() {
		this.animatable = null;
	}

	@Override
	public void renderRecursively(PoseStack pPoseStack, T animatable, BoneCache bone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
	                              int pPackedOverlay, int colour) {
		if (bone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());
			Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
			Matrix4f worldState = new Matrix4f(localMatrix);
			BlockPos pos = this.animatable.getBlockPos();

			bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			bone.setLocalSpaceMatrix(localMatrix);
			bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
		}

		BlueRenderer.super.renderRecursively(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay,
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
	public boolean firePreRenderEvent(PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireBlockPreRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
	}

	@Override
	public void firePostRenderEvent(PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireBlockPostRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
	}
}
