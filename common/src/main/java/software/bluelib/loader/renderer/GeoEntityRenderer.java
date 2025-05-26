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
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.model.data.EntityModelData;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.renderer.layer.GeoRenderLayersContainer;
import software.bluelib.loader.util.ClientUtil;
import software.bluelib.loader.util.Color;
import software.bluelib.loader.util.RenderUtil;

public class GeoEntityRenderer<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;

    protected T animatable;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    public GeoEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager);

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
        return animatable.getId();
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return GeoRenderer.super.getTextureLocation(animatable);
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);

        return this;
    }

    public GeoEntityRenderer<T> withScale(float scale) {
        return withScale(scale, scale);
    }

    public GeoEntityRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;

        return this;
    }

    @Override
    public Color getRenderColor(T animatable, float pPartialTick, int pPackedLight) {
        Color color = GeoRenderer.super.getRenderColor(animatable, pPartialTick, pPackedLight);

        if (animatable.isInvisible() && !animatable.isInvisibleTo(ClientUtil.getClientPlayer()))
            color = Color.ofARGB(Mth.ceil(color.getAlpha() * 38 / 255f), color.getRed(), color.getGreen(), color.getBlue());

        return color;
    }

    @Nullable
    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource pBufferSource, float pPartialTick) {
        final boolean invisible = animatable.isInvisible();

        if (invisible && !animatable.isInvisibleTo(ClientUtil.getClientPlayer()))
            return RenderType.itemEntityTranslucentCull(texture);

        if (!invisible)
            return GeoRenderer.super.getRenderType(animatable, texture, pBufferSource, pPartialTick);

        return Minecraft.getInstance().shouldEntityAppearGlowing(animatable) ? RenderType.outline(texture) : null;
    }

    @Override
    public void preRender(PoseStack pPoseStack, T animatable, ModelCache model, @Nullable MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        this.entityRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        scaleModelForRender(this.scaleWidth, this.scaleHeight, pPoseStack, animatable, model, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay);
    }

    @Override
    @ApiStatus.Internal
    public void render(T entity, float entityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        this.animatable = entity;

        defaultRender(pPoseStack, entity, pBufferSource, null, null, entityYaw, pPartialTick, pPackedLight);

        this.animatable = null;
    }

    @Override
    public void actuallyRender(PoseStack pPoseStack, T animatable, ModelCache model, @Nullable RenderType pRenderType,
                               MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick,
                               int pPackedLight, int pPackedOverlay, int colour) {
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
            float motionThreshold = getMotionAnimThreshold(animatable);
            Vec3 velocity = animatable.getDeltaMovement();
            float avgVelocity = (float) ((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);
            AnimationState<T> animationState = new AnimationState<T>(animatable, limbSwing, limbSwingAmount, pPartialTick, avgVelocity >= motionThreshold && limbSwingAmount != 0);
            long instanceId = getInstanceId(animatable);
            GeoModel<T> currentModel = getGeoModel();

            animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
            animationState.setData(DataTickets.ENTITY, animatable);
            animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, livingEntity != null && livingEntity.isBaby(), -netHeadYaw, -headPitch));
            currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            currentModel.handleAnimations(animatable, instanceId, animationState, pPartialTick);
        }

        pPoseStack.translate(0, 0.01f, 0);

        this.modelRenderTranslations = new Matrix4f(pPoseStack.last().pose());

        if (buffer != null)
            GeoRenderer.super.actuallyRender(pPoseStack, animatable, model, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick,
                    pPackedLight, pPackedOverlay, colour);

        pPoseStack.popPose();
    }

    @Override
    public void applyRenderLayers(PoseStack pPoseStack, T animatable, ModelCache model, @Nullable RenderType pRenderType,
                                  MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick,
                                  int pPackedLight, int pPackedOverlay) {
        if (!animatable.isSpectator())
            GeoRenderer.super.applyRenderLayers(pPoseStack, animatable, model, pRenderType, pBufferSource, buffer, pPartialTick, pPackedLight, pPackedOverlay);
    }

    @Override
    public void renderFinal(PoseStack pPoseStack, T animatable, ModelCache model, MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        super.render(animatable, 0, pPartialTick, pPoseStack, pBufferSource, pPackedLight);

        if (animatable instanceof Mob mob) {
            Entity leashHolder = mob.getLeashHolder();

            if (leashHolder != null)
                renderLeash(mob, pPartialTick, pPoseStack, pBufferSource, leashHolder);
        }
    }

    @Override
    public void doPostRenderCleanup() {
        this.animatable = null;
    }

    @Override
    public void renderRecursively(PoseStack pPoseStack, T animatable, BoneCache bone, RenderType pRenderType, MultiBufferSource pBufferSource, VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight,
                                  int pPackedOverlay, int colour) {
        pPoseStack.pushPose();
        RenderUtil.translateMatrixToBone(pPoseStack, bone);
        RenderUtil.translateToPivotPoint(pPoseStack, bone);
        RenderUtil.rotateMatrixAroundBone(pPoseStack, bone);
        RenderUtil.scaleMatrixForBone(pPoseStack, bone);

        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(pPoseStack.last().pose());
            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtil.translateMatrix(localMatrix, getRenderOffset(this.animatable, 1).toVector3f()));
            bone.setWorldSpaceMatrix(RenderUtil.translateMatrix(new Matrix4f(localMatrix), this.animatable.position().toVector3f()));
        }

        RenderUtil.translateAwayFromPivotPoint(pPoseStack, bone);

        buffer = checkAndRefreshBuffer(pIsReRender, buffer, pBufferSource, pRenderType);

        renderCubesOfBone(pPoseStack, bone, buffer, pPackedLight, pPackedOverlay, colour);

        if (!pIsReRender)
            applyRenderLayersForBone(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pPartialTick, pPackedLight, pPackedOverlay);

        renderChildBones(pPoseStack, animatable, bone, pRenderType, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, colour);

        pPoseStack.popPose();
    }

    @Deprecated(forRemoval = true)
    protected void applyRotations(T animatable, PoseStack pPoseStack, float ageInTicks, float rotationYaw,
            float pPartialTick) {
        applyRotations(animatable, pPoseStack, ageInTicks, rotationYaw, pPartialTick, 1);
    }

    protected void applyRotations(T animatable, PoseStack pPoseStack, float ageInTicks, float rotationYaw, float pPartialTick, float nativeScale) {
        if (isShaking(animatable))
            rotationYaw += (float) (Math.cos(animatable.tickCount * 3.25d) * Math.PI * 0.4d);

        if (!animatable.hasPose(Pose.SLEEPING))
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180f - rotationYaw));

        if (animatable instanceof LivingEntity livingEntity) {
            if (livingEntity.deathTime > 0) {
                float deathRotation = (livingEntity.deathTime + pPartialTick - 1f) / 20f * 1.6f;

                pPoseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1) * getDeathMaxRotation(animatable)));
            } else if (livingEntity.isAutoSpinAttack()) {
                pPoseStack.mulPose(Axis.XP.rotationDegrees(-90f - livingEntity.getXRot()));
                pPoseStack.mulPose(Axis.YP.rotationDegrees((livingEntity.tickCount + pPartialTick) * -75f));
            } else if (animatable.hasPose(Pose.SLEEPING)) {
                Direction bedOrientation = livingEntity.getBedOrientation();

                pPoseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? RenderUtil.getDirectionAngle(bedOrientation) : rotationYaw));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(animatable)));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270f));
            } else if (LivingEntityRenderer.isEntityUpsideDown(livingEntity)) {
                pPoseStack.translate(0, (animatable.getBbHeight() + 0.1f) / nativeScale, 0);
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            }
        }
    }

    protected float getDeathMaxRotation(T animatable) {
        return 90f;
    }

    public double getNameRenderCutoffDistance(T animatable) {
        return animatable.isDiscrete() ? 32d : 64d;
    }

    @Override
    public boolean shouldShowName(T animatable) {
        if (!(animatable instanceof LivingEntity))
            return super.shouldShowName(animatable);

        double nameRenderCutoff = getNameRenderCutoffDistance(animatable);

        if (this.entityRenderDispatcher.distanceToSqr(animatable) >= nameRenderCutoff * nameRenderCutoff)
            return false;

        if (animatable instanceof Mob && (!animatable.shouldShowName() && (!animatable.hasCustomName() || animatable != this.entityRenderDispatcher.crosshairPickEntity)))
            return false;

        final Minecraft minecraft = Minecraft.getInstance();
        boolean visibleToClient = !animatable.isInvisibleTo(minecraft.player);
        Team entityTeam = animatable.getTeam();

        if (entityTeam == null)
            return Minecraft.renderNames() && animatable != minecraft.getCameraEntity() && visibleToClient && !animatable.isVehicle();

        Team playerTeam = minecraft.player.getTeam();

        return switch (entityTeam.getNameTagVisibility()) {
            case ALWAYS -> visibleToClient;
            case NEVER -> false;
            case HIDE_FOR_OTHER_TEAMS -> playerTeam == null ? visibleToClient : entityTeam.isAlliedTo(playerTeam) && (entityTeam.canSeeFriendlyInvisibles() || visibleToClient);
            case HIDE_FOR_OWN_TEAM -> playerTeam == null ? visibleToClient : !entityTeam.isAlliedTo(playerTeam) && visibleToClient;
        };
    }

    @Override
    public int getPackedOverlay(T animatable, float u, float pPartialTick) {
        if (!(animatable instanceof LivingEntity entity))
            return OverlayTexture.NO_OVERLAY;

        return OverlayTexture.pack(OverlayTexture.u(u),
                OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    public boolean isShaking(T animatable) {
        return animatable.isFullyFrozen();
    }

    public <E extends Entity, M extends Mob> void renderLeash(M mob, float pPartialTick, PoseStack pPoseStack,
            MultiBufferSource pBufferSource, E leashHolder) {
        double lerpBodyAngle = (Mth.lerp(pPartialTick, mob.yBodyRotO, mob.yBodyRot) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
        Vec3 leashOffset = mob.getLeashOffset(pPartialTick);
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = Mth.lerp(pPartialTick, mob.xo, mob.getX()) + xAngleOffset;
        double lerpOriginY = Mth.lerp(pPartialTick, mob.yo, mob.getY()) + leashOffset.y;
        double lerpOriginZ = Mth.lerp(pPartialTick, mob.zo, mob.getZ()) + zAngleOffset;
        Vec3 ropeGripPosition = leashHolder.getRopeHoldPosition(pPartialTick);
        float xDif = (float) (ropeGripPosition.x - lerpOriginX);
        float yDif = (float) (ropeGripPosition.y - lerpOriginY);
        float zDif = (float) (ropeGripPosition.z - lerpOriginZ);
        float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.025f / 2f;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.leash());
        BlockPos entityEyePos = BlockPos.containing(mob.getEyePosition(pPartialTick));
        BlockPos holderEyePos = BlockPos.containing(leashHolder.getEyePosition(pPartialTick));
        int entityBlockLight = getBlockLightLevel((T) mob, entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.level().getBrightness(LightLayer.BLOCK, holderEyePos);
        int entitySkyLight = mob.level().getBrightness(LightLayer.SKY, entityEyePos);
        int holderSkyLight = mob.level().getBrightness(LightLayer.SKY, holderEyePos);

        pPoseStack.pushPose();
        pPoseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        Matrix4f posMatrix = new Matrix4f(pPoseStack.last().pose());

        for (int segment = 0; segment <= 24; ++segment) {
            GeoEntityRenderer.renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.025f, xOffset, zOffset, segment, false);
        }

        for (int segment = 24; segment >= 0; --segment) {
            GeoEntityRenderer.renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.0f, xOffset, zOffset, segment, true);
        }

        pPoseStack.popPose();
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif,
            float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight,
            int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = segment / 24f;
        int lerpBlockLight = (int) Mth.lerp(piecePosPercent, entityBlockLight, holderBlockLight);
        int lerpSkyLight = (int) Mth.lerp(piecePosPercent, entitySkyLight, holderSkyLight);
        int pPackedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1f;
        float red = 0.5f * knotColourMod;
        float green = 0.4f * knotColourMod;
        float blue = 0.3f * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0f ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0f - piecePosPercent) * (1.0f - piecePosPercent);
        float z = zDif * piecePosPercent;

        buffer.addVertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).setColor(red, green, blue, 1).setLight(pPackedLight);
        buffer.addVertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).setColor(red, green, blue, 1).setLight(pPackedLight);
    }

    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireCompileEntityRenderLayers(this);
    }

    @Override
    public boolean firePreRenderEvent(PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        return BlueLibConstants.PlatformHelper.EVENT_PROXY.fireEntityPreRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
    }

    @Override
    public void firePostRenderEvent(PoseStack pPoseStack, ModelCache model, MultiBufferSource pBufferSource, float pPartialTick, int pPackedLight) {
        BlueLibConstants.PlatformHelper.EVENT_PROXY.fireEntityPostRender(this, pPoseStack, model, pBufferSource, pPartialTick, pPackedLight);
    }
}
