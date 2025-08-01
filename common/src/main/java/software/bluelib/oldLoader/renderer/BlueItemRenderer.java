/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
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
import software.bluelib.oldLoader.animatable.BlueItem;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayer;
import software.bluelib.oldLoader.renderer.layer.BlueRenderLayersContainer;

import java.util.List;

public class BlueItemRenderer<T extends Item & BlueAnimatable> extends BlockEntityWithoutLevelRenderer implements BlueRenderer<T> {

	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	protected final BlueModel<T> model;

	protected ItemStack currentItemStack;
	protected ItemDisplayContext renderPerspective;
	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;
	protected boolean useEntityGuiLighting = false;

	protected Matrix4f itemRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueItemRenderer(BlueModel<T> pModel) {
		this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(),
				pModel);
	}

	public BlueItemRenderer(BlockEntityRenderDispatcher pDispatcher, EntityModelSet pModelSet, BlueModel<T> pModel) {
		super(pDispatcher, pModelSet);

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

	public ItemStack getCurrentItemStack() {
		return this.currentItemStack;
	}

	public BlueItemRenderer<T> useAlternateGuiLighting() {
		this.useEntityGuiLighting = true;

		return this;
	}

	@Override
	public long getInstanceId(T pAnimatable) {
		return BlueItem.getId(this.currentItemStack);
	}

	@Override
	public ResourceLocation getTextureLocation(T pAnimatable) {
		return BlueRenderer.super.getTextureLocation(pAnimatable);
	}

	@Override
	public List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	public BlueItemRenderer<T> addRenderLayer(BlueRenderLayer<T> renderLayer) {
		this.renderLayers.addLayer(renderLayer);

		return this;
	}

	public BlueItemRenderer<T> withScale(float scale) {
		return withScale(scale, scale);
	}

	public BlueItemRenderer<T> withScale(float scaleWidth, float scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;

		return this;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.itemRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext.poseStack(), pContext.animatable(), pContext.model(), pContext.isReRender(), pContext.partialTick(), pContext.packedLight(), pContext.packedOverlay());

		if (!pContext.isReRender())
			pContext.poseStack().translate(0.5f, 0.51f, 0.5f);
	}

	@Override
	@ApiStatus.Internal
	public void renderByItem(ItemStack pStack, @NotNull ItemDisplayContext pTransformType, @NotNull PoseStack pPoseStack,
	                         @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		this.animatable = (T) pStack.getItem();
		this.currentItemStack = pStack;
		this.renderPerspective = pTransformType;
		float pPartialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);

		if (pTransformType == ItemDisplayContext.GUI) {
			renderInGui(pTransformType, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, pPartialTick);
		} else {
			RenderType pRenderType = getRenderType(this.animatable, getTextureLocation(this.animatable), pBufferSource, pPartialTick);
			VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(pBufferSource, pRenderType, false, this.currentItemStack != null && this.currentItemStack.hasFoil());

			defaultRender(new FullRenderContext<>(
					pPoseStack,
					this.animatable,
					this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
					pRenderType,
					pBufferSource,
					buffer,
					false, // isReRender
					pPartialTick,
					pPackedLight,
					getPackedOverlay(this.animatable, 0, pPartialTick),
					getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt()));
		}

		this.animatable = null;
	}

	protected void renderInGui(ItemDisplayContext pTransformType, PoseStack pPoseStack,
	                           MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, float pPartialTick) {
		setupLightingForGuiRender();

		MultiBufferSource.BufferSource defaultBufferSource = pBufferSource instanceof MultiBufferSource.BufferSource bufferSource2 ? bufferSource2 : Minecraft.getInstance().levelRenderer.renderBuffers.bufferSource();
		RenderType pRenderType = getRenderType(this.animatable, getTextureLocation(this.animatable), defaultBufferSource, pPartialTick);
		VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(pBufferSource, pRenderType, true, this.currentItemStack != null && this.currentItemStack.hasFoil());

		pPoseStack.pushPose();
		defaultRender(new FullRenderContext<>(
				pPoseStack,
				this.animatable,
				this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
				pRenderType,
				pBufferSource,
				buffer,
				false, // isReRender
				pPartialTick,
				pPackedLight,
				getPackedOverlay(this.animatable, 0, pPartialTick),
				getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt()));
		defaultBufferSource.endBatch();
		RenderSystem.enableDepthTest();
		Lighting.setupFor3DItems();
		pPoseStack.popPose();
	}

	@Override
	public void actuallyRender(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			PoseStack pPoseStack = full.poseStack();
			T pAnimatable = full.animatable();
			VertexConsumer pBuffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<>(pAnimatable, 0, 0, pPartialTick, false);
				long instanceId = getInstanceId(pAnimatable);
				BlueModel<T> currentModel = getBlueModel();

				animationState.setData(DataTickets.TICK, pAnimatable.getTick(this.currentItemStack));
				animationState.setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
				animationState.setData(DataTickets.ITEMSTACK, this.currentItemStack);
				pAnimatable.getAnimatableInstanceCache().getManagerForId(instanceId).setData(DataTickets.ITEM_RENDER_PERSPECTIVE, this.renderPerspective);
				currentModel.addAdditionalStateData(pAnimatable, instanceId, animationState::setData);
				currentModel.handleAnimations(pAnimatable, instanceId, animationState, pPartialTick);
			}

			this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

			if (pBuffer != null)
				BlueRenderer.super.actuallyRender(full);
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@Override
	public void doPostRenderCleanup() {
		this.animatable = null;
		this.currentItemStack = null;
		this.renderPerspective = null;
	}

	@Override
	public void renderRecursively(PoseStack pPoseStack, T pAnimatable, BoneCache pBone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer pBuffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
	                              int pPackedOverlay, int pColour) {
		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.itemRenderTranslations));
		}

		BlueRenderer.super.renderRecursively(pPoseStack, pAnimatable, pBone, pRenderType, pBufferSource, pBuffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay,
				pColour);
	}

	public void setupLightingForGuiRender() {
		if (this.useEntityGuiLighting) {
			Lighting.setupForEntityInInventory();
		} else {
			Lighting.setupForFlatItems();
		}
	}

	@Override
	public void updateAnimatedTextureFrame(T pAnimatable) {
		AnimatableTexture.setAndUpdate(getTextureLocation(pAnimatable));
	}

	@Override
	public void fireCompileRenderLayersEvent() {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileItemRenderLayers(this);
	}

	@Override
	public boolean firePreRenderEvent(PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireItemPreRender(this, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight);
	}

	@Override
	public void firePostRenderEvent(PoseStack pPoseStack, ModelCache pModel, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireItemPostRender(this, pPoseStack, pModel, pBufferSource, pPartialTick, pPackedLight);
	}
}
