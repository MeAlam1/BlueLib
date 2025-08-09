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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderLayersContainer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

public class BlueObjectRenderer<T extends BlueAnimatable> implements BlueRenderer<T> {

	@NotNull
	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	@NotNull
	protected final BlueModel<T> model;

	@Nullable
	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	@NotNull
	protected Matrix4f objectRenderTranslations = new Matrix4f();
	@NotNull
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueObjectRenderer(@NotNull BlueModel<T> pModel) {
		this.model = pModel;
	}

	@Override
	public @NotNull BlueModel<T> getBlueModel() {
		return this.model;
	}

	@Override
	public @Nullable T getOptionalAnimatable() {
		return this.animatable;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T pAnimatable) {
		return BlueRenderer.super.getTextureLocation(pAnimatable);
	}

	@Override
	public @NotNull List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	@NotNull
	public BlueObjectRenderer<T> addRenderLayer(@NotNull BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	@NotNull
	public BlueObjectRenderer<T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	@NotNull
	public BlueObjectRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
		this.scaleWidth = pScaleWidth;
		this.scaleHeight = pScaleHeight;

		return this;
	}

	@ApiStatus.Internal
	public void render(@NotNull IRenderContext<T> pContext) {
		this.animatable = pContext.animatable();
		defaultRender(pContext);
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		this.objectRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext);

		pContext.poseStack().translate(0.5f, 0.51f, 0.5f);
	}

	@Override
	public void actuallyRender(@NotNull IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			PoseStack pPoseStack = full.poseStack();
			T pAnimatable = full.animatable();
			VertexConsumer pBuffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();

			pPoseStack.pushPose();

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<>(pAnimatable, 0, 0, pPartialTick, false);
				long instanceId = getInstanceId(pContext);
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
	public void doPostRenderCleanup(@NotNull IRenderContext<T> pContext) {
		this.animatable = null;
	}

	@Override
	public void renderRecursively(@NotNull BoneCache pBone, @NotNull FullRenderContext<T> pContext) {
		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.objectRenderTranslations));
		}

		BlueRenderer.super.renderRecursively(pBone, pContext);
	}

	@Override
	public void updateAnimatedTextureFrame(@NotNull IRenderContext<T> pContext) {
		AnimatableTexture.setAndUpdate(getTextureLocation(pContext.animatable()));
	}

	@Override
	public void fireCompileRenderLayersEvent() {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileObjectRenderLayers(this);
	}

	@Override
	public boolean firePreRenderEvent(@NotNull IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(@NotNull IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireObjectPostRender(this, pContext);
	}
}
