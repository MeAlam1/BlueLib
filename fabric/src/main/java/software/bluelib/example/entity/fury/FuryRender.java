// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.fury;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} that extends {@link GeoEntityRenderer} for rendering the nightfury entity.
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
public class FuryRender extends GeoEntityRenderer<FuryEntity> {

    private float lastRotationAngle = 0; // Make sure this is a class-level variable to persist its value.
    private long lastUpdateTime = Minecraft.getInstance().getFrameTimeNs();
    private static Boolean areBonesLogged = false;
    private static FuryEntity entity;

    /**
     * Constructor
     *
     * @param pRenderManager {@link EntityRendererProvider.Context} - The render manager.
     * @author MeAlam
     * @since 1.0.0
     */
    public FuryRender(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager, new FuryModel());
    }

    @Override
    public void render(EntityRenderState pAnimatable, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        super.render(pAnimatable, pPoseStack, pBufferSource, pPackedLight);

        if (Minecraft.getInstance().isPaused()) {
            return;
        }


        GeoBone legLeft2 = this.getGeoModel().getBone("leg_left2").orElse(null);
        GeoBone leftHipWingC = this.getGeoModel().getBone("leftHipWingC").orElse(null);
        GeoBone leftHipWingB = this.getGeoModel().getBone("leftHipWingB").orElse(null);
        GeoBone rightWing2 = this.getGeoModel().getBone("right_wing2").orElse(null);
        GeoBone pupilLeft = this.getGeoModel().getBone("pupil_left").orElse(null);
        GeoBone claw9 = this.getGeoModel().getBone("Claw9").orElse(null);
        GeoBone leftWing = this.getGeoModel().getBone("left_wing").orElse(null);
        GeoBone leftHipWingA = this.getGeoModel().getBone("leftHipWingA").orElse(null);
        GeoBone legLeft3 = this.getGeoModel().getBone("leg_left3").orElse(null);
        GeoBone claw8 = this.getGeoModel().getBone("Claw8").orElse(null);
        GeoBone claw7 = this.getGeoModel().getBone("Claw7").orElse(null);
        GeoBone claw10 = this.getGeoModel().getBone("claw10").orElse(null);
        GeoBone bigEarR3 = this.getGeoModel().getBone("BigEarR3").orElse(null);
        GeoBone leftEyelid = this.getGeoModel().getBone("left_eyelid").orElse(null);
        GeoBone rider = this.getGeoModel().getBone("rider").orElse(null);
        GeoBone sideEarR7 = this.getGeoModel().getBone("SideEarR7").orElse(null);
        GeoBone eyelidEmoteHappy2 = this.getGeoModel().getBone("eyelid_emote_happy2").orElse(null);
        GeoBone torso = this.getGeoModel().getBone("torso").orElse(null);
        GeoBone leftLeg = this.getGeoModel().getBone("leftleg").orElse(null);
        GeoBone claw11 = this.getGeoModel().getBone("claw11").orElse(null);
        GeoBone claw12 = this.getGeoModel().getBone("claw12").orElse(null);
        GeoBone leftTWing4 = this.getGeoModel().getBone("leftTWIng4").orElse(null);
        GeoBone jaw = this.getGeoModel().getBone("jaw").orElse(null);
        GeoBone bigEarR2 = this.getGeoModel().getBone("BigEarR2").orElse(null);
        GeoBone neck = this.getGeoModel().getBone("neck").orElse(null);
        GeoBone tailPattern = this.getGeoModel().getBone("tailPattern").orElse(null);
        GeoBone sideEarR4 = this.getGeoModel().getBone("SideEarR4").orElse(null);
        GeoBone rightWing5 = this.getGeoModel().getBone("right_wing5").orElse(null);
        GeoBone eyeLeft = this.getGeoModel().getBone("eye_left").orElse(null);
        GeoBone claw3 = this.getGeoModel().getBone("Claw3").orElse(null);
        GeoBone claw5 = this.getGeoModel().getBone("Claw5").orElse(null);
        GeoBone leftPupil = this.getGeoModel().getBone("leftpupil").orElse(null);
        GeoBone leftArm = this.getGeoModel().getBone("leftArm").orElse(null);
        GeoBone tail1 = this.getGeoModel().getBone("tail1").orElse(null);
        GeoBone footLeft3 = this.getGeoModel().getBone("foot_left3").orElse(null);
        GeoBone rightWingEnd = this.getGeoModel().getBone("right_wing_end").orElse(null);
        GeoBone rightEyelid = this.getGeoModel().getBone("right_eyelid").orElse(null);
        GeoBone tail3 = this.getGeoModel().getBone("tail3").orElse(null);
        GeoBone leftWing2 = this.getGeoModel().getBone("left_wing2").orElse(null);
        GeoBone rightHipWingB = this.getGeoModel().getBone("rightHipWingB").orElse(null);
        GeoBone leftWing6 = this.getGeoModel().getBone("left_wing6").orElse(null);
        GeoBone body2 = this.getGeoModel().getBone("body2").orElse(null);
        GeoBone allClaw3 = this.getGeoModel().getBone("allclaw3").orElse(null);
        GeoBone topEarR2 = this.getGeoModel().getBone("TopEarR2").orElse(null);
        GeoBone legLeft = this.getGeoModel().getBone("leg_left").orElse(null);
        GeoBone rightWing3 = this.getGeoModel().getBone("right_wing3").orElse(null);
        GeoBone teeth = this.getGeoModel().getBone("teeth").orElse(null);
        GeoBone fArmDetR2 = this.getGeoModel().getBone("FArmDetR2").orElse(null);
        GeoBone rightLeg = this.getGeoModel().getBone("rightleg").orElse(null);
        GeoBone neckBase = this.getGeoModel().getBone("neckbase").orElse(null);
        GeoBone leftWingEnd = this.getGeoModel().getBone("left_wing_end").orElse(null);
        GeoBone nightFury = this.getGeoModel().getBone("nightfury").orElse(null);
        GeoBone nubs = this.getGeoModel().getBone("nubs").orElse(null);
        GeoBone footRight3 = this.getGeoModel().getBone("foot_right3").orElse(null);
        GeoBone teeth2 = this.getGeoModel().getBone("teeth2").orElse(null);
        GeoBone rightArm = this.getGeoModel().getBone("rightArm").orElse(null);
        GeoBone footRight = this.getGeoModel().getBone("foot_right").orElse(null);
        GeoBone leftWing4 = this.getGeoModel().getBone("left_wing4").orElse(null);
        GeoBone rightHipWingA = this.getGeoModel().getBone("rightHipWingA").orElse(null);
        GeoBone rightPupil5 = this.getGeoModel().getBone("rightpupil5").orElse(null);
        GeoBone leftWing5 = this.getGeoModel().getBone("left_wing5").orElse(null);
        GeoBone saddle = this.getGeoModel().getBone("Saddle").orElse(null);
        GeoBone eyelidEmoteHappy = this.getGeoModel().getBone("eyelid_emote_happy").orElse(null);
        GeoBone leftFArm2 = this.getGeoModel().getBone("leftFArm2").orElse(null);
        GeoBone body = this.getGeoModel().getBone("body").orElse(null);
        GeoBone footLeft = this.getGeoModel().getBone("foot_left").orElse(null);
        GeoBone sideEarR2 = this.getGeoModel().getBone("SideEarR2").orElse(null);
        GeoBone sideEarR3 = this.getGeoModel().getBone("SideEarR3").orElse(null);
        GeoBone legRight3 = this.getGeoModel().getBone("leg_right3").orElse(null);
        GeoBone head = this.getGeoModel().getBone("head").orElse(null);
        GeoBone eyebrowRight = this.getGeoModel().getBone("eyebrow_right").orElse(null);
        GeoBone upperJaw = this.getGeoModel().getBone("Upperjaw").orElse(null);
        GeoBone claw16 = this.getGeoModel().getBone("claw16").orElse(null);
        GeoBone claw17 = this.getGeoModel().getBone("claw17").orElse(null);
        GeoBone eyes = this.getGeoModel().getBone("eyes").orElse(null);
        GeoBone eyeRight = this.getGeoModel().getBone("eye_right").orElse(null);
        GeoBone claw13 = this.getGeoModel().getBone("claw13").orElse(null);
        GeoBone claw15 = this.getGeoModel().getBone("claw15").orElse(null);
        GeoBone eyebrowAngry2 = this.getGeoModel().getBone("eyebrow_angry2").orElse(null);
        GeoBone rightTWing4 = this.getGeoModel().getBone("rightTWIng4").orElse(null);
        GeoBone claw14 = this.getGeoModel().getBone("claw14").orElse(null);
        GeoBone claw4 = this.getGeoModel().getBone("Claw4").orElse(null);
        GeoBone rightWing6 = this.getGeoModel().getBone("right_wing6").orElse(null);
        GeoBone legRight = this.getGeoModel().getBone("leg_right").orElse(null);
        GeoBone fArmDetR3 = this.getGeoModel().getBone("FArmDetR3").orElse(null);
        GeoBone rightHipWingC = this.getGeoModel().getBone("rightHipWingC").orElse(null);
        GeoBone allClaw2 = this.getGeoModel().getBone("allclaw2").orElse(null);
        GeoBone topEarR3 = this.getGeoModel().getBone("TopEarR3").orElse(null);
        GeoBone legRight2 = this.getGeoModel().getBone("leg_right2").orElse(null);
        GeoBone chests = this.getGeoModel().getBone("Chests").orElse(null);
        GeoBone tail4 = this.getGeoModel().getBone("tail4").orElse(null);
        GeoBone eyebrowLeft = this.getGeoModel().getBone("eyebrow_left").orElse(null);
        GeoBone tail2 = this.getGeoModel().getBone("tail2").orElse(null);
        GeoBone claw6 = this.getGeoModel().getBone("Claw6").orElse(null);
        GeoBone pupilRight = this.getGeoModel().getBone("pupil_right").orElse(null);
        GeoBone claw2 = this.getGeoModel().getBone("Claw2").orElse(null);
        GeoBone rightWing4 = this.getGeoModel().getBone("right_wing4").orElse(null);
        GeoBone tongue8 = this.getGeoModel().getBone("tongue8").orElse(null);
        GeoBone leftWing3 = this.getGeoModel().getBone("left_wing3").orElse(null);
        GeoBone rightFArm2 = this.getGeoModel().getBone("rightFArm2").orElse(null);
        GeoBone rightWing = this.getGeoModel().getBone("right_wing").orElse(null);
        GeoBone eyebrowAngry = this.getGeoModel().getBone("eyebrow_angry").orElse(null);

        if (leftWing != null) {
            long currentTime = Minecraft.getInstance().getFrameTimeNs();
            float rotationSpeed = (float) Math.toRadians(36);  // Rotation speed in radians per second.

            // Calculate elapsed time since last update (in seconds)
            float elapsedTime = (currentTime - lastUpdateTime) / 1_000_000_000.0f;  // Convert to seconds.

            // Update the rotation angle based on the elapsed time
            if (entity.isShouldMove()) {
                BaseLogger.log(BaseLogLevel.INFO, "Fury will now walk!");
                lastRotationAngle += rotationSpeed * elapsedTime; // Clockwise rotation when moving
            } else {
                BaseLogger.log(BaseLogLevel.INFO, "Fury will now stand still!");
                lastRotationAngle -= rotationSpeed * elapsedTime; // Counter-clockwise rotation when standing still
            }

            // Normalize the angle to stay within 0 and 2π (full rotation)
            lastRotationAngle = lastRotationAngle % (float) (2 * Math.PI);
            if (lastRotationAngle < 0) {
                lastRotationAngle += (float) (2 * Math.PI); // Ensure the angle stays positive.
            }

            BaseLogger.log(BaseLogLevel.INFO, "Setting rotation angle to: " + lastRotationAngle);

            // Apply the rotation to the bone
            leftWing.setRotY(lastRotationAngle);

            BaseLogger.log(BaseLogLevel.INFO, "Rotation applied to leftWing. Checking current rotation: " + leftWing.getRotY());

            // Update last update time
            lastUpdateTime = currentTime;
        }


    @Override
    public void preRender(PoseStack poseStack, FuryEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        entity = animatable;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, renderColor);
    }

    public void logAllBones() {
        for (GeoBone bone : this.getGeoModel().getAnimationProcessor().getRegisteredBones()) {
            BaseLogger.log(BaseLogLevel.INFO, "Bone: " + bone.getName());
        }
    }


}
