/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.loader.BufferUtils;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderLayersContainer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.model.data.EntityModelData;

public class BlueReplacedEntityRenderer<E extends Entity, T extends BlueAnimatable> extends EntityRenderer<E> implements BlueRenderer<T> {

	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	protected final BlueModel<T> model;
	protected final T animatable;

	protected E currentEntity;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f entityRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueReplacedEntityRenderer(EntityRendererProvider.Context pRenderManager, BlueModel<T> pModel, T pAnimatable) {
		super(pRenderManager);

		this.model = pModel;
		this.animatable = pAnimatable;
	}

	@Override
	public BlueModel<T> getBlueModel() {
		return this.model;
	}

	@Override
	public T getAnimatable() {
		return this.animatable;
	}

	public E getCurrentEntity() {
		return this.currentEntity;
	}

	@Override
	public long getInstanceId(IRenderContext<T> pContext) {
		return this.currentEntity.getId();
	}

	@Override
	public ResourceLocation getTextureLocation(E pEntity) {
		return BlueRenderer.super.getTextureLocation(this.animatable);
	}

	@Override
	public List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	public BlueReplacedEntityRenderer<E, T> addRenderLayer(BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	public BlueReplacedEntityRenderer<E, T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	public BlueReplacedEntityRenderer<E, T> withScale(float pScaleWidth, float pScaleHeight) {
		this.scaleWidth = pScaleWidth;
		this.scaleHeight = pScaleHeight;

		return this;
	}

	@Nullable
	@Override
	public RenderType getRenderType(ResourceLocation pTexture, IRenderContext<T> pContext) {
		final boolean invisible = this.currentEntity != null && this.currentEntity.isInvisible();

		if (invisible && !this.currentEntity.isInvisibleTo(PlayerUtils.getClientPlayer()))
			return RenderType.itemEntityTranslucentCull(pTexture);

		if (!invisible)
			return BlueRenderer.super.getRenderType(pTexture, pContext);

		return this.currentEntity != null && Minecraft.getInstance().shouldEntityAppearGlowing(this.currentEntity) ? RenderType.outline(pTexture) : null;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.entityRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext);
	}

	@Override
	@ApiStatus.Internal
	public void render(E pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
		this.currentEntity = pEntity;

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
			T pAnimatable = full.animatable();
			ModelCache pModel = full.model();
			RenderType pRenderType = full.renderType();
			MultiBufferSource pBufferSource = full.bufferSource();
			VertexConsumer pBuffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();
			int pPackedLight = full.packedLight();
			int pPackedOverlay = full.packedOverlay();
			int pColour = full.color();

			pPoseStack.pushPose();

			LivingEntity livingEntity = this.currentEntity instanceof LivingEntity entity ? entity : null;

			if (this.currentEntity instanceof Mob mob && !pIsReRender) {
				Entity leashHolder = mob.getLeashHolder();
				if (leashHolder != null)
					renderLeash(mob, pPartialTick, pPoseStack, pBufferSource, leashHolder);
			}

			boolean shouldSit = this.currentEntity.isPassenger() && (this.currentEntity.getVehicle() != null);
			float lerpBodyRot = livingEntity == null ? 0 : Mth.rotLerp(pPartialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
			float lerpHeadRot = livingEntity == null ? 0 : Mth.rotLerp(pPartialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
			float netHeadYaw = lerpHeadRot - lerpBodyRot;

			if (shouldSit && this.currentEntity.getVehicle() instanceof LivingEntity livingentity) {
				lerpBodyRot = Mth.rotLerp(pPartialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
				netHeadYaw = lerpHeadRot - lerpBodyRot;
				float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
				lerpBodyRot = lerpHeadRot - clampedHeadYaw;
				if (clampedHeadYaw * clampedHeadYaw > 2500f)
					lerpBodyRot += clampedHeadYaw * 0.2f;
				netHeadYaw = lerpHeadRot - lerpBodyRot;
			}

			if (this.currentEntity.getPose() == Pose.SLEEPING && livingEntity != null) {
				Direction bedDirection = livingEntity.getBedOrientation();
				if (bedDirection != null) {
					float eyePosOffset = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;
					pPoseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
				}
			}

			float nativeScale = livingEntity != null ? livingEntity.getScale() : 1;
			float ageInTicks = this.currentEntity.tickCount + pPartialTick;
			float limbSwingAmount = 0;
			float limbSwing = 0;

			pPoseStack.scale(nativeScale, nativeScale, nativeScale);
			applyRotations(pAnimatable, pPoseStack, ageInTicks, lerpBodyRot, pPartialTick, nativeScale);

			if (!shouldSit && this.currentEntity.isAlive() && livingEntity != null) {
				limbSwingAmount = livingEntity.walkAnimation.speed(pPartialTick);
				limbSwing = livingEntity.walkAnimation.position(pPartialTick);
				if (livingEntity.isBaby())
					limbSwing *= 3f;
				if (limbSwingAmount > 1f)
					limbSwingAmount = 1f;
			}

			float headPitch = Mth.lerp(pPartialTick, this.currentEntity.xRotO, this.currentEntity.getXRot());
			float motionThreshold = getMotionAnimThreshold(pContext);
			boolean isMoving;
			if (livingEntity != null) {
				Vec3 velocity = livingEntity.getDeltaMovement();
				float avgVelocity = (float) (Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;
				isMoving = avgVelocity >= motionThreshold && limbSwingAmount != 0;
			} else {
				isMoving = (limbSwingAmount <= -motionThreshold || limbSwingAmount >= motionThreshold);
			}

			if (!pIsReRender) {
				AnimationState<T> animationState = new AnimationState<T>(pAnimatable, limbSwing, limbSwingAmount, pPartialTick, isMoving);
				long instanceId = getInstanceId(pContext);
				BlueModel<T> currentModel = getBlueModel();
				animationState.setData(DataTickets.TICK, pAnimatable.getTick(this.currentEntity));
				animationState.setData(DataTickets.ENTITY, this.currentEntity);
				animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, livingEntity != null && livingEntity.isBaby(), -netHeadYaw, -headPitch));
				currentModel.addAdditionalStateData(pAnimatable, instanceId, animationState::setData);
				currentModel.handleAnimations(pAnimatable, instanceId, animationState, pPartialTick);
			}

			pPoseStack.translate(0, 0.01f, 0);

			this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

			if (pBuffer != null)
				BlueRenderer.super.actuallyRender(full);

			pPoseStack.popPose();
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@Override
	public void applyRenderLayers(IRenderContext<T> pContext) {
		if (!this.currentEntity.isSpectator())
			BlueRenderer.super.applyRenderLayers(pContext);
	}

	@Override
	public void renderFinal(IRenderContext<T> pContext) {
		super.render(this.currentEntity, 0, pContext.partialTick(), pContext.poseStack(), pContext.bufferSource(), pContext.packedLight());

		if (this.currentEntity instanceof Mob mob) {
			Entity leashHolder = mob.getLeashHolder();

			if (leashHolder != null)
				renderLeash(mob, pContext.partialTick(), pContext.poseStack(), pContext.bufferSource(), leashHolder);
		}
	}

	@Override
	public void postRender(IRenderContext<T> pContext) {
		if (!pContext.isReRender())
			super.render(this.currentEntity, 0, pContext.partialTick(), pContext.poseStack(), pContext.bufferSource(), pContext.packedLight());
	}

	@Override
	public void doPostRenderCleanup(IRenderContext<T> pContext) {
		this.currentEntity = null;
	}

	@Override
	public void renderRecursively(BoneCache pBone, FullRenderContext<T> pContext) {
		pContext.poseStack().pushPose();
		RenderUtils.translateMatrixToBone(pContext.poseStack(), pBone);
		RenderUtils.translateToPivotPoint(pContext.poseStack(), pBone);
		RenderUtils.rotateMatrixAroundBone(pContext.poseStack(), pBone);
		RenderUtils.scaleMatrixForBone(pContext.poseStack(), pBone);

		if (pBone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(pContext.poseStack().last().pose());
			Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

			pBone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			pBone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, getRenderOffset(this.currentEntity, 1).toVector3f()));
			pBone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.currentEntity.position().toVector3f()));
		}

		RenderUtils.translateAwayFromPivotPoint(pContext.poseStack(), pBone);

		pContext.setBuffer(BufferUtils.checkAndRefreshBuffer(pContext.isReRender(), pContext.buffer(), pContext.bufferSource(), pContext.renderType()));

		renderCubesOfBone(pBone, pContext);

		if (!pContext.isReRender())
			applyRenderLayersForBone(pBone, pContext);

		renderChildBones(pBone, pContext);

		pContext.poseStack().popPose();
	}

	protected void applyRotations(T pAnimatable, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw,
			float pPartialTick, float pNativeScale) {
		if (isShaking(pAnimatable))
			pRotationYaw += (float) (Math.cos(this.currentEntity.tickCount * 3.25d) * Math.PI * 0.4d);

		if (!this.currentEntity.hasPose(Pose.SLEEPING))
			pPoseStack.mulPose(Axis.YP.rotationDegrees(180f - pRotationYaw));

		if (this.currentEntity instanceof LivingEntity livingEntity) {
			if (livingEntity.deathTime > 0) {
				float deathRotation = (livingEntity.deathTime + pPartialTick - 1f) / 20f * 1.6f;

				pPoseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1) * getDeathMaxRotation(pAnimatable)));
			} else if (livingEntity.isAutoSpinAttack()) {
				pPoseStack.mulPose(Axis.XP.rotationDegrees(-90f - livingEntity.getXRot()));
				pPoseStack.mulPose(Axis.YP.rotationDegrees((livingEntity.tickCount + pPartialTick) * -75f));
			} else if (livingEntity.hasPose(Pose.SLEEPING)) {
				Direction bedOrientation = livingEntity.getBedOrientation();

				pPoseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? RenderUtils.getDirectionAngle(bedOrientation) : pRotationYaw));
				pPoseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(pAnimatable)));
				pPoseStack.mulPose(Axis.YP.rotationDegrees(270f));
			} else if (LivingEntityRenderer.isEntityUpsideDown(livingEntity)) {
				pPoseStack.translate(0, (livingEntity.getBbHeight() + 0.1f) / pNativeScale, 0);
				pPoseStack.mulPose(Axis.ZP.rotationDegrees(180f));
			}
		}
	}

	protected float getDeathMaxRotation(T pAnimatable) {
		return 90f;
	}

	public double getNameRenderCutoffDistance(E pEntity, T pAnimatable) {
		return pEntity.isDiscrete() ? 32d : 64d;
	}

	@Override
	public boolean shouldShowName(E pEntity) {
		if (!(pEntity instanceof LivingEntity))
			return super.shouldShowName(pEntity);

		double nameRenderCutoff = getNameRenderCutoffDistance(pEntity, this.animatable);

		if (this.entityRenderDispatcher.distanceToSqr(pEntity) >= nameRenderCutoff * nameRenderCutoff)
			return false;

		if (pEntity instanceof Mob && (!pEntity.shouldShowName() && (!pEntity.hasCustomName() || pEntity != this.entityRenderDispatcher.crosshairPickEntity)))
			return false;

		final Minecraft minecraft = Minecraft.getInstance();
		boolean visibleToClient = !pEntity.isInvisibleTo(minecraft.player);
		Team entityTeam = pEntity.getTeam();

		if (entityTeam == null)
			return Minecraft.renderNames() && pEntity != minecraft.getCameraEntity() && visibleToClient && !pEntity.isVehicle();

		Team playerTeam = minecraft.player.getTeam();

		return switch (entityTeam.getNameTagVisibility()) {
			case ALWAYS -> visibleToClient;
			case NEVER -> false;
			case HIDE_FOR_OTHER_TEAMS -> playerTeam == null ? visibleToClient : entityTeam.isAlliedTo(playerTeam) && (entityTeam.canSeeFriendlyInvisibles() || visibleToClient);
			case HIDE_FOR_OWN_TEAM -> playerTeam == null ? visibleToClient : !entityTeam.isAlliedTo(playerTeam) && visibleToClient;
		};
	}

	@Override
	public int getPackedOverlay(T pAnimatable, float pU, float pPartialTick) {
		if (!(this.currentEntity instanceof LivingEntity entity))
			return OverlayTexture.NO_OVERLAY;

		return OverlayTexture.pack(OverlayTexture.u(pU),
				OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
	}

	public boolean isShaking(T pAnimatable) {
		return this.currentEntity.isFullyFrozen();
	}

	// TODO: WHAT THE ACTUAL FUCK IS THIS? PLEASE CLEAN IT UP FUTURE ARAM.
	public <H extends Entity, M extends Mob> void renderLeash(M pMob, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, H pLeashHolder) {
		double lerpBodyAngle = (Mth.lerp(pPartialTick, pMob.yBodyRotO, pMob.yBodyRot) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
		Vec3 leashOffset = pMob.getLeashOffset(pPartialTick);
		double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
		double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
		double lerpOriginX = Mth.lerp(pPartialTick, pMob.xo, pMob.getX()) + xAngleOffset;
		double lerpOriginY = Mth.lerp(pPartialTick, pMob.yo, pMob.getY()) + leashOffset.y;
		double lerpOriginZ = Mth.lerp(pPartialTick, pMob.zo, pMob.getZ()) + zAngleOffset;
		Vec3 ropeGripPosition = pLeashHolder.getRopeHoldPosition(pPartialTick);
		float xDif = (float) (ropeGripPosition.x - lerpOriginX);
		float yDif = (float) (ropeGripPosition.y - lerpOriginY);
		float zDif = (float) (ropeGripPosition.z - lerpOriginZ);
		float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.025f / 2f;
		float xOffset = zDif * offsetMod;
		float zOffset = xDif * offsetMod;
		VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.leash());
		BlockPos entityEyePos = BlockPos.containing(pMob.getEyePosition(pPartialTick));
		BlockPos holderEyePos = BlockPos.containing(pLeashHolder.getEyePosition(pPartialTick));
		int entityBlockLight = getBlockLightLevel((E) pMob, entityEyePos);
		int holderBlockLight = pLeashHolder.isOnFire() ? 15 : pLeashHolder.level().getBrightness(LightLayer.BLOCK, holderEyePos);
		int entitySkyLight = pMob.level().getBrightness(LightLayer.SKY, entityEyePos);
		int holderSkyLight = pMob.level().getBrightness(LightLayer.SKY, holderEyePos);

		pPoseStack.pushPose();
		pPoseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

		Matrix4f posMatrix = new Matrix4f(pPoseStack.last().pose());

		for (int segment = 0; segment <= 24; ++segment) {
			renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
					entitySkyLight, holderSkyLight, 0.025f, 0.025f, xOffset, zOffset, segment, false);
		}

		for (int segment = 24; segment >= 0; --segment) {
			renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
					entitySkyLight, holderSkyLight, 0.025f, 0.0f, xOffset, zOffset, segment, true);
		}

		pPoseStack.popPose();
	}

	private static void renderLeashPiece(VertexConsumer pBuffer, Matrix4f pPositionMatrix, float pXDif, float pYDif,
			float pZDif, int pEntityBlockLight, int pHolderBlockLight, int pEntitySkyLight,
			int pHolderSkyLight, float pWidth, float pYOffset, float pXOffset, float pZOffset, int pSegment, boolean pIsLeashKnot) {
		float piecePosPercent = pSegment / 24f;
		int lerpBlockLight = (int) Mth.lerp(piecePosPercent, pEntityBlockLight, pHolderBlockLight);
		int lerpSkyLight = (int) Mth.lerp(piecePosPercent, pEntitySkyLight, pHolderSkyLight);
		int pPackedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
		float knotColourMod = pSegment % 2 == (pIsLeashKnot ? 1 : 0) ? 0.7f : 1f;
		float red = 0.5f * knotColourMod;
		float green = 0.4f * knotColourMod;
		float blue = 0.3f * knotColourMod;
		float x = pXDif * piecePosPercent;
		float y = pYDif > 0.0f ? pYDif * piecePosPercent * piecePosPercent : pYDif - pYDif * (1.0f - piecePosPercent) * (1.0f - piecePosPercent);
		float z = pZDif * piecePosPercent;

		pBuffer.addVertex(pPositionMatrix, x - pXOffset, y + pYOffset, z + pZOffset).setColor(red, green, blue, 1).setLight(pPackedLight);
		pBuffer.addVertex(pPositionMatrix, x + pXOffset, y + pWidth - pYOffset, z - pZOffset).setColor(red, green, blue, 1).setLight(pPackedLight);
	}

	@Override
	public void updateAnimatedTextureFrame(T pAnimatable) {
		AnimatableTexture.setAndUpdate(getTextureLocation(pAnimatable));
	}

	@Override
	public void fireCompileRenderLayersEvent() {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileReplacedEntityRenderLayers(this);
	}

	@Override
	public boolean firePreRenderEvent(IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireReplacedEntityPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireReplacedEntityPostRender(this, pContext);
	}
}
