/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.Color;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.item.BlueItem;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.geckolib.constant.DataTickets;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderLayersContainer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

public class BlueArmorRenderer<T extends Item & BlueItem> extends HumanoidModel implements BlueRenderer<T> {

	@NotNull
	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	@NotNull
	protected final BlueModel<T> model;

	@Nullable
	protected T animatable;
	@Nullable
	protected HumanoidModel<?> baseModel;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	@NotNull
	protected Matrix4f entityRenderTranslations = new Matrix4f();
	@NotNull
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	@Nullable
	protected ModelCache lastModel = null;
	@Nullable
	protected BoneCache head = null;
	@Nullable
	protected BoneCache body = null;
	@Nullable
	protected BoneCache rightArm = null;
	@Nullable
	protected BoneCache leftArm = null;
	@Nullable
	protected BoneCache rightLeg = null;
	@Nullable
	protected BoneCache leftLeg = null;
	@Nullable
	protected BoneCache rightBoot = null;
	@Nullable
	protected BoneCache leftBoot = null;

	@Nullable
	protected Entity currentEntity = null;
	@Nullable
	protected ItemStack currentStack = null;
	@Nullable
	protected EquipmentSlot currentSlot = null;
	@Nullable
	protected MultiBufferSource bufferSource = null;
	protected float partialTick;
	protected float limbSwing;
	protected float limbSwingAmount;
	protected float netHeadYaw;
	protected float headPitch;

	public BlueArmorRenderer(@NotNull BlueModel<T> pModel) {
		super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));

		this.model = pModel;
		this.young = false;
	}

	@Override
	public @NotNull BlueModel<T> getBlueModel() {
		return this.model;
	}

	public @Nullable T getOptionalAnimatable() {
		return this.animatable;
	}

	public @Nullable Entity getCurrentEntity() {
		return this.currentEntity;
	}

	public @Nullable ItemStack getCurrentStack() {
		return this.currentStack;
	}

	public @Nullable EquipmentSlot getCurrentSlot() {
		return this.currentSlot;
	}

	@Override
	public long getInstanceId(@NotNull IRenderContext<T> pContext) {
		if (this.currentStack == null) {
			return (long) Math.pow(this.currentEntity.getId(), 7) * -(this.currentSlot.ordinal() + 1);
		}

		long stackId = BlueItem.getId(this.currentStack);

		if (stackId == Long.MAX_VALUE)
			return (long) Math.pow(this.currentEntity.getId(), 7) * -(this.currentSlot.ordinal() + 1);

		return -stackId;
	}

	@Override
	@NotNull
	public RenderType getRenderType(@NotNull ResourceLocation pTexture, @NotNull IRenderContext<T> pContext) {
		return RenderType.armorCutoutNoCull(pTexture);
	}

	@Override
	public @NotNull List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	@NotNull
	public BlueArmorRenderer<T> addRenderLayer(@NotNull BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	@NotNull
	public BlueArmorRenderer<T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	@NotNull
	public BlueArmorRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
		this.scaleWidth = pScaleWidth;
		this.scaleHeight = pScaleHeight;

		return this;
	}

	@Nullable
	public BoneCache getHeadBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorHead").orElse(null);
	}

	@Nullable
	public BoneCache getBodyBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorBody").orElse(null);
	}

	@Nullable
	public BoneCache getRightArmBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorRightArm").orElse(null);
	}

	@Nullable
	public BoneCache getLeftArmBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorLeftArm").orElse(null);
	}

	@Nullable
	public BoneCache getRightLegBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorRightLeg").orElse(null);
	}

	@Nullable
	public BoneCache getLeftLegBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorLeftLeg").orElse(null);
	}

	@Nullable
	public BoneCache getRightBootBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorRightBoot").orElse(null);
	}

	@Nullable
	public BoneCache getLeftBootBone(@NotNull BlueModel<T> pModel) {
		return pModel.getBone("armorLeftBoot").orElse(null);
	}

	@Override
	public @NotNull Color getRenderColor(@NotNull T pAnimatable, float pPartialTick, int pPackedLight) {
		return this.currentStack.is(ItemTags.DYEABLE) ? Color.ofOpaque(DyedItemColor.getOrDefault(this.currentStack, -6265536)) : Color.WHITE;
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		this.entityRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		applyBaseModel(this.baseModel);
		grabRelevantBones(pContext.model());
		applyBaseTransformations(this.baseModel);
		scaleModelForBaby(pContext.poseStack(), pContext.animatable(), pContext.partialTick(), pContext.isReRender());
		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext);

		if (!(this.currentEntity instanceof BlueAnimatable))
			applyBoneVisibilityBySlot(this.currentSlot);
	}

	@Override
	@ApiStatus.Internal
	public void renderToBuffer(@NotNull PoseStack pPoseStack, @Nullable VertexConsumer pBuffer, int pPackedLight,
			int pPackedOverlay, int pColor) {
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource pBufferSource = mc.levelRenderer.renderBuffers.bufferSource();

		if (mc.levelRenderer.shouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity))
			pBufferSource = mc.levelRenderer.renderBuffers.outlineBufferSource();

		float pPartialTick = mc.getTimer().getGameTimeDeltaPartialTick(true);
		RenderType pRenderType = getRenderType(getTextureLocation(getAnimatable()), new BaseRenderContext<>(
				pPoseStack,
				getAnimatable(),
				this.model.getBakedModel(getBlueModel().getModelResource(getAnimatable(), this)),
				bufferSource,
				false,
				pPartialTick,
				pPackedLight,
				pPackedOverlay,
				pColor

		));
		pBuffer = ItemRenderer.getArmorFoilBuffer(pBufferSource, pRenderType, this.currentStack.hasFoil());

		defaultRender(new FullRenderContext<>(
				pPoseStack,
				getAnimatable(),
				this.model.getBakedModel(getBlueModel().getModelResource(getAnimatable(), this)),
				pRenderType,
				pBufferSource,
				pBuffer,
				false, // isReRender
				pPartialTick,
				pPackedLight,
				getPackedOverlay(getAnimatable(), 0, pPartialTick),
				getRenderColor(getAnimatable(), pPartialTick, pPackedLight).argbInt()));

		this.animatable = null;
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
			pPoseStack.translate(0, 24 / 16f, 0);
			pPoseStack.scale(-1, -1, 1);

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<>(pAnimatable, 0, 0, pPartialTick, false);
				long instanceId = getInstanceId(pContext);
				BlueModel<T> currentModel = getBlueModel();

				animationState.setData(DataTickets.TICK, pAnimatable.getTick(this.currentEntity));
				animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
				animationState.setData(DataTickets.ENTITY, this.currentEntity);
				animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
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
		this.baseModel = null;
		this.currentEntity = null;
		this.currentStack = null;
		this.animatable = null;
		this.currentSlot = null;
		this.bufferSource = null;
		this.partialTick = 0;
		this.limbSwing = 0;
		this.limbSwingAmount = 0;
		this.netHeadYaw = 0;
		this.headPitch = 0;
	}

	@Override
	public void renderRecursively(@NotNull BoneCache pBone, @NotNull FullRenderContext<T> pContext) {
		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
		}

		BlueRenderer.super.renderRecursively(pBone, pContext);
	}

	protected void grabRelevantBones(@NotNull ModelCache pBakedModel) {
		if (this.lastModel == pBakedModel)
			return;

		BlueModel<T> pModel = getBlueModel();
		this.lastModel = pBakedModel;
		this.head = getHeadBone(pModel);
		this.body = getBodyBone(pModel);
		this.rightArm = getRightArmBone(pModel);
		this.leftArm = getLeftArmBone(pModel);
		this.rightLeg = getRightLegBone(pModel);
		this.leftLeg = getLeftLegBone(pModel);
		this.rightBoot = getRightBootBone(pModel);
		this.leftBoot = getLeftBootBone(pModel);
	}

	protected void applyBaseModel(@NotNull HumanoidModel<?> pBaseModel) {
		HumanoidModel<?> self = (HumanoidModel<?>) this;

		self.young = pBaseModel.young;
		self.crouching = pBaseModel.crouching;
		self.riding = pBaseModel.riding;
		self.rightArmPose = pBaseModel.rightArmPose;
		self.leftArmPose = pBaseModel.leftArmPose;
		self.head.visible = pBaseModel.head.visible;
		self.hat.visible = pBaseModel.hat.visible;
		self.body.visible = pBaseModel.body.visible;
		self.rightArm.visible = pBaseModel.rightArm.visible;
		self.leftArm.visible = pBaseModel.leftArm.visible;
		self.rightLeg.visible = pBaseModel.rightLeg.visible;
		self.leftLeg.visible = pBaseModel.leftLeg.visible;
	}

	protected void applyBoneVisibilityBySlot(@NotNull EquipmentSlot pCurrentSlot) {
		setAllBonesVisible(false);
		HumanoidModel<?> pModel = this;

		switch (pCurrentSlot) {
			case HEAD -> setBoneVisible(this.head, pModel.head.visible);
			case CHEST -> {
				setBoneVisible(this.body, pModel.body.visible);
				setBoneVisible(this.rightArm, pModel.rightArm.visible);
				setBoneVisible(this.leftArm, pModel.leftArm.visible);
			}
			case LEGS -> {
				setBoneVisible(this.rightLeg, pModel.rightLeg.visible);
				setBoneVisible(this.leftLeg, pModel.leftLeg.visible);
			}
			case FEET -> {
				setBoneVisible(this.rightBoot, pModel.rightLeg.visible);
				setBoneVisible(this.leftBoot, pModel.leftLeg.visible);
			}
			default -> {}
		}
	}

	public void applyBoneVisibilityByPart(@NotNull EquipmentSlot pCurrentSlot, @NotNull ModelPart pCurrentPart, @NotNull HumanoidModel<?> pModel) {
		setAllVisible(false);

		pCurrentPart.visible = true;
		BoneCache bone = null;

		if (pCurrentPart == pModel.hat || pCurrentPart == pModel.head) {
			bone = this.head;
		} else if (pCurrentPart == pModel.body) {
			bone = this.body;
		} else if (pCurrentPart == pModel.leftArm) {
			bone = this.leftArm;
		} else if (pCurrentPart == pModel.rightArm) {
			bone = this.rightArm;
		} else if (pCurrentPart == pModel.leftLeg) {
			bone = pCurrentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
		} else if (pCurrentPart == pModel.rightLeg) {
			bone = pCurrentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
		}

		if (bone != null)
			bone.setHidden(false);
	}

	protected void applyBaseTransformations(@NotNull HumanoidModel<?> pBaseModel) {
		if (this.head != null) {
			ModelPart headPart = pBaseModel.head;

			RenderUtils.matchModelPartRot(headPart, this.head);
			this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
		}

		if (this.body != null) {
			ModelPart bodyPart = pBaseModel.body;

			RenderUtils.matchModelPartRot(bodyPart, this.body);
			this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
		}

		if (this.rightArm != null) {
			ModelPart rightArmPart = pBaseModel.rightArm;

			RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
			this.rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
		}

		if (this.leftArm != null) {
			ModelPart leftArmPart = pBaseModel.leftArm;

			RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
			this.leftArm.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
		}

		if (this.rightLeg != null) {
			ModelPart rightLegPart = pBaseModel.rightLeg;

			RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
			this.rightLeg.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);

			if (this.rightBoot != null) {
				RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
				this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
			}
		}

		if (this.leftLeg != null) {
			ModelPart leftLegPart = pBaseModel.leftLeg;

			RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
			this.leftLeg.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);

			if (this.leftBoot != null) {
				RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
				this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
			}
		}
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(pVisible);
		setAllBonesVisible(pVisible);
	}

	protected void setAllBonesVisible(boolean pVisible) {
		setBoneVisible(this.head, pVisible);
		setBoneVisible(this.body, pVisible);
		setBoneVisible(this.rightArm, pVisible);
		setBoneVisible(this.leftArm, pVisible);
		setBoneVisible(this.rightLeg, pVisible);
		setBoneVisible(this.leftLeg, pVisible);
		setBoneVisible(this.rightBoot, pVisible);
		setBoneVisible(this.leftBoot, pVisible);
	}

	public void scaleModelForBaby(@NotNull PoseStack pPoseStack, @NotNull T pAnimatable, float pPartialTick, boolean pIsReRender) {
		if (!this.young || pIsReRender)
			return;

		if (this.currentSlot == EquipmentSlot.HEAD) {
			if (this.baseModel.scaleHead) {
				float headScale = 1.5f / this.baseModel.babyHeadScale;

				pPoseStack.scale(headScale, headScale, headScale);
			}

			pPoseStack.translate(0, this.baseModel.babyYHeadOffset / 16f, this.baseModel.babyZHeadOffset / 16f);
		} else {
			float bodyScale = 1 / this.baseModel.babyBodyScale;

			pPoseStack.scale(bodyScale, bodyScale, bodyScale);
			pPoseStack.translate(0, this.baseModel.bodyYOffset / 16f, 0);
		}
	}

	protected void setBoneVisible(@Nullable BoneCache pBone, boolean pVisible) {
		if (pBone == null)
			return;

		pBone.setHidden(!pVisible);
	}

	@Override
	public void updateAnimatedTextureFrame(@NotNull IRenderContext<T> pContext) {
		if (this.currentEntity != null)
			AnimatableTexture.setAndUpdate(getTextureLocation(pContext.animatable()));
	}

	@Override
	public void fireCompileRenderLayersEvent() {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileArmorRenderLayers(this);
	}

	@Override
	public boolean firePreRenderEvent(@NotNull IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireArmorPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(@NotNull IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireArmorPostRender(this, pContext);
	}
}
