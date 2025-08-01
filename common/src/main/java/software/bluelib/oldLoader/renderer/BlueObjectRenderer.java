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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
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
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayer;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayersContainer;

import java.util.List;

public class BlueObjectRenderer<T extends BlueAnimatable> implements BlueRenderer<T> {

	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	protected final BlueModel<T> model;

	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f objectRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueObjectRenderer(BlueModel<T> pModel) {
		this.model = pModel;
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
	public ResourceLocation getTextureLocation(T pAnimatable) {
		return BlueRenderer.super.getTextureLocation(pAnimatable);
	}

	@Override
	public List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	public BlueObjectRenderer<T> addRenderLayer(BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	public BlueObjectRenderer<T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	public BlueObjectRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
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

		defaultRender(new FullRenderContext<>(
				pPoseStack,
				this.animatable,
				this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
				pRenderType,
				pBufferSource,
				pBuffer,
				false, // isReRender
				pPartialTick,
				pPackedLight,
				getPackedOverlay(this.animatable, 0, pPartialTick),
				getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt()));
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.objectRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext.poseStack(), pContext.animatable(), pContext.model(), pContext.isReRender(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay());

		pContext.poseStack().translate(0.5f, 0.51f, 0.5f);
	}

	@Override
	public void actuallyRender(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			PoseStack pPoseStack = full.poseStack();
			T pAnimatable = full.animatable();
			VertexConsumer pBuffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();

			pPoseStack.pushPose();

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<>(pAnimatable, 0, 0, pPartialTick, false);
				long instanceId = getInstanceId(pAnimatable);
				BlueModel<T> currentModel = getBlueModel();

				currentModel.addAdditionalStateData(pAnimatable, instanceId, animationState::setData);
				currentModel.handleAnimations(pAnimatable, instanceId, animationState, pPartialTick);
			}

			this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

			if (pBuffer != null)
				BlueRenderer.super.actuallyRender(full);

			pPoseStack.popPose();
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@Override
	public void doPostRenderCleanup(IRenderContext<T> pContext) {
		this.animatable = null;
	}

	@Override
	public void renderRecursively(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
	                              int pPackedOverlay, int pColour) {
		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.objectRenderTranslations));
		}

		BlueRenderer.super.renderRecursively(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay,
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
	public boolean firePreRenderEvent(IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPostRender(this, pContext);
	}
}
