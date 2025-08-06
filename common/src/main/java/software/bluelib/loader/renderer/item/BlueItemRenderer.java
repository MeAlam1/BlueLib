/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
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
import software.bluelib.loader.geckolib.constant.DataTickets;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderLayersContainer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animatable.BlueItem;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.model.BlueModel;

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
	public long getInstanceId(IRenderContext<T> pContext) {
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

	public BlueItemRenderer<T> addRenderLayer(BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	public BlueItemRenderer<T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	public BlueItemRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
		this.scaleWidth = pScaleWidth;
		this.scaleHeight = pScaleHeight;

		return this;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.itemRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext);

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
			int color = getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt();
			ModelCache modelCache = this.model.getBakedModel(getBlueModel().getModelResource(animatable, this));
			RenderType pRenderType = getRenderType(getTextureLocation(this.animatable), new BaseRenderContext<>(
					pPoseStack,
					this.animatable,
					modelCache,
					pBufferSource,
					false,
					pPartialTick,
					pPackedLight,
					pPackedOverlay,
					color));
			VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(pBufferSource, pRenderType, false, this.currentItemStack != null && this.currentItemStack.hasFoil());

			defaultRender(new FullRenderContext<>(
					pPoseStack,
					this.animatable,
					this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
					pRenderType,
					pBufferSource,
					buffer,
					false,
					pPartialTick,
					pPackedLight,
					getPackedOverlay(this.animatable, 0, pPartialTick),
					color));
		}

		this.animatable = null;
	}

	protected void renderInGui(ItemDisplayContext pTransformType, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, float pPartialTick) {
		setupLightingForGuiRender();

		MultiBufferSource.BufferSource defaultBufferSource = pBufferSource instanceof MultiBufferSource.BufferSource bufferSource2 ? bufferSource2 : Minecraft.getInstance().levelRenderer.renderBuffers.bufferSource();
		int color = getRenderColor(this.animatable, pPartialTick, pPackedLight).argbInt();
		ModelCache modelCache = this.model.getBakedModel(getBlueModel().getModelResource(animatable, this));
		RenderType pRenderType = getRenderType(getTextureLocation(this.animatable), new BaseRenderContext<>(
				pPoseStack,
				this.animatable,
				modelCache,
				defaultBufferSource,
				false,
				pPartialTick,
				pPackedLight,
				pPackedOverlay,
				color));
		VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(pBufferSource, pRenderType, true, this.currentItemStack != null && this.currentItemStack.hasFoil());

		pPoseStack.pushPose();
		defaultRender(new FullRenderContext<>(
				pPoseStack,
				this.animatable,
				modelCache,
				pRenderType,
				pBufferSource,
				buffer,
				false,
				pPartialTick,
				pPackedLight,
				pPackedOverlay,
				color));
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
				long instanceId = getInstanceId(pContext);
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
	public void doPostRenderCleanup(IRenderContext<T> pContext) {
		this.animatable = null;
		this.currentItemStack = null;
		this.renderPerspective = null;
	}

	@Override
	public void renderRecursively(BoneCache pBone, FullRenderContext<T> pContext) {
		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.itemRenderTranslations));
		}

		BlueRenderer.super.renderRecursively(pBone, pContext);
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
	public boolean firePreRenderEvent(IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireItemPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireItemPostRender(this, pContext);
	}
}
