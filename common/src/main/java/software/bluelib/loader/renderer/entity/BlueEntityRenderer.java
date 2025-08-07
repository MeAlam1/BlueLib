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
import software.bluelib.api.utils.Color;
import software.bluelib.api.utils.loader.BufferUtils;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.geckolib.constant.DataTickets;
import software.bluelib.loader.geckolib.data.EntityModelData;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderLayersContainer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.BaseRenderContext;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;
import software.bluelib.oldLoader.animation.AnimationState;

public class BlueEntityRenderer<T extends Entity & BlueAnimatable> extends EntityRenderer<T> implements BlueRenderer<T> {

	protected final BlueRenderLayersContainer<T> renderLayers = new BlueRenderLayersContainer<>(this);
	protected final BlueModel<T> model;

	protected T animatable;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f entityRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();

	public BlueEntityRenderer(EntityRendererProvider.Context pRenderManager, BlueModel<T> pModel) {
		super(pRenderManager);

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
	public long getInstanceId(IRenderContext<T> pContext) {
		return pContext.animatable().getId();
	}

	@Override
	public ResourceLocation getTextureLocation(T pAnimatable) {
		return BlueRenderer.super.getTextureLocation(pAnimatable);
	}

	@Override
	public List<BlueRenderLayer<T>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}

	public BlueEntityRenderer<T> addRenderLayer(BlueRenderLayer<T> pRenderLayer) {
		this.renderLayers.addLayer(pRenderLayer);

		return this;
	}

	public BlueEntityRenderer<T> withScale(float pScale) {
		return withScale(pScale, pScale);
	}

	public BlueEntityRenderer<T> withScale(float pScaleWidth, float pScaleHeight) {
		this.scaleWidth = pScaleWidth;
		this.scaleHeight = pScaleHeight;

		return this;
	}

	@Override
	public Color getRenderColor(T pAnimatable, float pPartialTick, int pPackedLight) {
		Color color = BlueRenderer.super.getRenderColor(pAnimatable, pPartialTick, pPackedLight);

		if (pAnimatable.isInvisible() && !pAnimatable.isInvisibleTo(PlayerUtils.getClientPlayer()))
			color = Color.ofARGB(Mth.ceil(color.getAlpha() * 38 / 255f), color.getRed(), color.getGreen(), color.getBlue());

		return color;
	}

	@Nullable
	@Override
	public RenderType getRenderType(ResourceLocation pTexture, IRenderContext<T> pContext) {
		final boolean invisible = pContext.animatable().isInvisible();

		if (invisible && !pContext.animatable().isInvisibleTo(PlayerUtils.getClientPlayer()))
			return RenderType.itemEntityTranslucentCull(pTexture);

		if (!invisible)
			return BlueRenderer.super.getRenderType(pTexture, pContext);

		return Minecraft.getInstance().shouldEntityAppearGlowing(pContext.animatable()) ? RenderType.outline(pTexture) : null;
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		this.entityRenderTranslations = new Matrix4f(pContext.poseStack().last().pose());

		scaleModelForRender(this.scaleWidth, this.scaleHeight, pContext);
	}

	@Override
	@ApiStatus.Internal
	public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
		this.animatable = pEntity;

		defaultRender(new BaseRenderContext<>(
				pPoseStack,
				pEntity,
				this.model.getBakedModel(getBlueModel().getModelResource(animatable, this)),
				pBufferSource,
				false, // isReRender
				pPartialTick,
				pPackedLight,
				getPackedOverlay(pEntity, 0, pPartialTick),
				getRenderColor(pEntity, pPartialTick, pPackedLight).argbInt()));

		this.animatable = null;
	}

	@Override
	public void actuallyRender(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			PoseStack pPoseStack = full.poseStack();
			T animatable = full.animatable();
			VertexConsumer buffer = full.buffer();
			boolean pIsReRender = full.isReRender();
			float pPartialTick = full.partialTick();

			pPoseStack.pushPose();

			LivingEntity livingEntity = animatable instanceof LivingEntity entity ? entity : null;
			boolean shouldSit = animatable.isPassenger() && (animatable.getVehicle() != null);
			float lerpBodyRot = livingEntity == null ? 0 : Mth.rotLerp(pPartialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
			float lerpHeadRot = livingEntity == null ? 0 : Mth.rotLerp(pPartialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
			float netHeadYaw = lerpHeadRot - lerpBodyRot;

			if (shouldSit && animatable.getVehicle() instanceof LivingEntity livingentity) {
				lerpBodyRot = Mth.rotLerp(pPartialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
				netHeadYaw = lerpHeadRot - lerpBodyRot;
				float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
				lerpBodyRot = lerpHeadRot - clampedHeadYaw;

				if (clampedHeadYaw * clampedHeadYaw > 2500f)
					lerpBodyRot += clampedHeadYaw * 0.2f;

				netHeadYaw = lerpHeadRot - lerpBodyRot;
			}

			if (animatable.getPose() == Pose.SLEEPING && livingEntity != null) {
				Direction bedDirection = livingEntity.getBedOrientation();

				if (bedDirection != null) {
					float eyePosOffset = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;
					pPoseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
				}
			}

			float nativeScale = livingEntity != null ? livingEntity.getScale() : 1;
			float ageInTicks = animatable.tickCount + pPartialTick;
			float limbSwingAmount = 0;
			float limbSwing = 0;

			pPoseStack.scale(nativeScale, nativeScale, nativeScale);
			applyRotations(animatable, pPoseStack, ageInTicks, lerpBodyRot, pPartialTick, nativeScale);

			if (!shouldSit && animatable.isAlive() && livingEntity != null) {
				limbSwingAmount = livingEntity.walkAnimation.speed(pPartialTick);
				limbSwing = livingEntity.walkAnimation.position(pPartialTick);

				if (livingEntity.isBaby())
					limbSwing *= 3f;

				if (limbSwingAmount > 1f)
					limbSwingAmount = 1f;
			}

			if (!pIsReRender) {
				float headPitch = Mth.lerp(pPartialTick, animatable.xRotO, animatable.getXRot());
				float motionThreshold = getMotionAnimThreshold(pContext);
				Vec3 velocity = animatable.getDeltaMovement();
				float avgVelocity = (float) ((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);
				AnimationState<T> animationState = new AnimationState<T>(animatable, limbSwing, limbSwingAmount, pPartialTick, avgVelocity >= motionThreshold && limbSwingAmount != 0);
				long instanceId = getInstanceId(pContext);
				BlueModel<T> currentModel = getBlueModel();

				animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
				animationState.setData(DataTickets.ENTITY, animatable);
				animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, livingEntity != null && livingEntity.isBaby(), -netHeadYaw, -headPitch));
				currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
				currentModel.handleAnimations(animatable, instanceId, animationState, pPartialTick);
			}

			pPoseStack.translate(0, 0.01f, 0);

			this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

			if (buffer != null)
				BlueRenderer.super.actuallyRender(full);

			pPoseStack.popPose();
		} else if (pContext instanceof BaseRenderContext<T> base) {
			handleBaseActuallyRenderContext(base, this);
		}
	}

	@Override
	public void applyRenderLayers(IRenderContext<T> pContext) {
		if (!pContext.animatable().isSpectator()) {
			BlueRenderer.super.applyRenderLayers(pContext);
		}
	}

	@Override
	public void renderFinal(IRenderContext<T> pContext) {
		super.render(animatable, 0, pContext.partialTick(), pContext.poseStack(), pContext.bufferSource(), pContext.packedLight());

		if (animatable instanceof Mob mob) {
			Entity leashHolder = mob.getLeashHolder();

			if (leashHolder != null)
				renderLeash(mob, pContext.partialTick(), pContext.poseStack(), pContext.bufferSource(), leashHolder);
		}
	}

	@Override
	public void doPostRenderCleanup(IRenderContext<T> pContext) {
		this.animatable = null;
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
			pBone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, getRenderOffset(this.animatable, 1).toVector3f()));
			pBone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.animatable.position().toVector3f()));
		}

		RenderUtils.translateAwayFromPivotPoint(pContext.poseStack(), pBone);

		pContext.setBuffer(BufferUtils.checkAndRefreshBuffer(pContext.isReRender(), pContext.buffer(), pContext.bufferSource(), pContext.renderType()));

		renderCubesOfBone(pBone, pContext);

		if (!pContext.isReRender())
			applyRenderLayersForBone(pBone, pContext);

		renderChildBones(pBone, pContext);

		pContext.poseStack().popPose();
	}

	protected void applyRotations(T pAnimatable, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTick, float pNativeScale) {
		if (isShaking(pAnimatable))
			pRotationYaw += (float) (Math.cos(pAnimatable.tickCount * 3.25d) * Math.PI * 0.4d);

		if (!pAnimatable.hasPose(Pose.SLEEPING))
			pPoseStack.mulPose(Axis.YP.rotationDegrees(180f - pRotationYaw));

		if (pAnimatable instanceof LivingEntity livingEntity) {
			if (livingEntity.deathTime > 0) {
				float deathRotation = (livingEntity.deathTime + pPartialTick - 1f) / 20f * 1.6f;

				pPoseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1) * getDeathMaxRotation(pAnimatable)));
			} else if (livingEntity.isAutoSpinAttack()) {
				pPoseStack.mulPose(Axis.XP.rotationDegrees(-90f - livingEntity.getXRot()));
				pPoseStack.mulPose(Axis.YP.rotationDegrees((livingEntity.tickCount + pPartialTick) * -75f));
			} else if (pAnimatable.hasPose(Pose.SLEEPING)) {
				Direction bedOrientation = livingEntity.getBedOrientation();

				pPoseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? RenderUtils.getDirectionAngle(bedOrientation) : pRotationYaw));
				pPoseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(pAnimatable)));
				pPoseStack.mulPose(Axis.YP.rotationDegrees(270f));
			} else if (LivingEntityRenderer.isEntityUpsideDown(livingEntity)) {
				pPoseStack.translate(0, (pAnimatable.getBbHeight() + 0.1f) / pNativeScale, 0);
				pPoseStack.mulPose(Axis.ZP.rotationDegrees(180f));
			}
		}
	}

	protected float getDeathMaxRotation(T pAnimatable) {
		return 90f;
	}

	public double getNameRenderCutoffDistance(T pAnimatable) {
		return pAnimatable.isDiscrete() ? 32d : 64d;
	}

	@Override
	public boolean shouldShowName(T pAnimatable) {
		if (!(pAnimatable instanceof LivingEntity))
			return super.shouldShowName(pAnimatable);

		double nameRenderCutoff = getNameRenderCutoffDistance(pAnimatable);

		if (this.entityRenderDispatcher.distanceToSqr(pAnimatable) >= nameRenderCutoff * nameRenderCutoff)
			return false;

		if (pAnimatable instanceof Mob && (!pAnimatable.shouldShowName() && (!pAnimatable.hasCustomName() || pAnimatable != this.entityRenderDispatcher.crosshairPickEntity)))
			return false;

		final Minecraft minecraft = Minecraft.getInstance();
		boolean visibleToClient = !pAnimatable.isInvisibleTo(minecraft.player);
		Team entityTeam = pAnimatable.getTeam();

		if (entityTeam == null)
			return Minecraft.renderNames() && pAnimatable != minecraft.getCameraEntity() && visibleToClient && !pAnimatable.isVehicle();

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
		if (!(pAnimatable instanceof LivingEntity entity))
			return OverlayTexture.NO_OVERLAY;

		return OverlayTexture.pack(OverlayTexture.u(pU),
				OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
	}

	public boolean isShaking(T animatable) {
		return animatable.isFullyFrozen();
	}

	public <E extends Entity, M extends Mob> void renderLeash(
			M pMob, float pPartialTick, PoseStack pPoseStack,
			MultiBufferSource pBufferSource, E pLeashHolder) {
		float bodyAngle = (Mth.lerp(pPartialTick, pMob.yBodyRotO, pMob.yBodyRot) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
		Vec3 leashOffset = pMob.getLeashOffset(pPartialTick);
		float cos = (float) Math.cos(bodyAngle), sin = (float) Math.sin(bodyAngle);

		double xAngleOffset = cos * leashOffset.z + sin * leashOffset.x;
		double zAngleOffset = sin * leashOffset.z - cos * leashOffset.x;

		double lerpOriginX = Mth.lerp(pPartialTick, pMob.xo, pMob.getX()) + xAngleOffset;
		double lerpOriginY = Mth.lerp(pPartialTick, pMob.yo, pMob.getY()) + leashOffset.y;
		double lerpOriginZ = Mth.lerp(pPartialTick, pMob.zo, pMob.getZ()) + zAngleOffset;

		Vec3 ropePos = pLeashHolder.getRopeHoldPosition(pPartialTick);
		float xDif = (float) (ropePos.x - lerpOriginX);
		float yDif = (float) (ropePos.y - lerpOriginY);
		float zDif = (float) (ropePos.z - lerpOriginZ);

		float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.0125f;
		float xOffset = zDif * offsetMod, zOffset = xDif * offsetMod;

		VertexConsumer vc = pBufferSource.getBuffer(RenderType.leash());

		BlockPos mobEye = BlockPos.containing(pMob.getEyePosition(pPartialTick));
		BlockPos holderEye = BlockPos.containing(pLeashHolder.getEyePosition(pPartialTick));

		int mobBlockLight = getBlockLightLevel((T) pMob, mobEye);
		int holderBlockLight = pLeashHolder.isOnFire() ? 15 : pLeashHolder.level().getBrightness(LightLayer.BLOCK, holderEye);
		int mobSkyLight = pMob.level().getBrightness(LightLayer.SKY, mobEye);
		int holderSkyLight = pMob.level().getBrightness(LightLayer.SKY, holderEye);

		pPoseStack.pushPose();
		pPoseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

		Matrix4f matrix = pPoseStack.last().pose();

		for (int i = 0; i <= 24; i++) {
			float t = i / 24f;
			addLeashVertices(vc, matrix, xDif, yDif, zDif, mobBlockLight, holderBlockLight, mobSkyLight, holderSkyLight, xOffset, zOffset, t, false);
		}
		for (int i = 24; i >= 0; i--) {
			float t = i / 24f;
			addLeashVertices(vc, matrix, xDif, yDif, zDif, mobBlockLight, holderBlockLight, mobSkyLight, holderSkyLight, xOffset, zOffset, t, true);
		}

		pPoseStack.popPose();
	}

	private static void addLeashVertices(VertexConsumer pBuffer, Matrix4f pMatrix4f,
			float pXDif, float pYDif, float pZDif,
			int pMobBlockLight, int pHolderBlockLight,
			int pMobSkyLight, int pHolderSkyLight,
			float pXOffset, float pZOffset, float pSegment, boolean pIsKnot) {
		int packedLight = LightTexture.pack(
				(int) Mth.lerp(pSegment, pMobBlockLight, pHolderBlockLight),
				(int) Mth.lerp(pSegment, pMobSkyLight, pHolderSkyLight));

		float colourMod = (Math.round(pSegment * 24) % 2 == (pIsKnot ? 1 : 0)) ? 0.7f : 1f;
		float red = 0.5f * colourMod, green = 0.4f * colourMod, blue = 0.3f * colourMod;

		float x = pXDif * pSegment;
		float y = pYDif > 0 ? pYDif * pSegment * pSegment : pYDif - pYDif * (1 - pSegment) * (1 - pSegment);
		float z = pZDif * pSegment;
		float width = pIsKnot ? 0f : 0.025f;

		pBuffer.addVertex(pMatrix4f, x - pXOffset, y + width, z + pZOffset).setColor(red, green, blue, 1).setLight(packedLight);
		pBuffer.addVertex(pMatrix4f, x + pXOffset, y + 0.025f - width, z - pZOffset).setColor(red, green, blue, 1).setLight(packedLight);
	}

	@Override
	public void updateAnimatedTextureFrame(T pAnimatable) {
		AnimatableTexture.setAndUpdate(getTextureLocation(pAnimatable));
	}

	@Override
	public void fireCompileRenderLayersEvent() {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileEntityRenderLayers(this);
	}

	@Override
	public boolean firePreRenderEvent(IRenderContext<T> pContext) {
		return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireEntityPreRender(this, pContext);
	}

	@Override
	public void firePostRenderEvent(IRenderContext<T> pContext) {
		BlueLibConstants.PlatformHelper.EVENT_PROXY.fireEntityPostRender(this, pContext);
	}
}
