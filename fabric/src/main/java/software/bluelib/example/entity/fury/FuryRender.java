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

    protected GeoBone legLeft2, leftHipWingC, leftHipWingB, rightWing2, pupilLeft, claw9, leftWing, leftHipWingA,
            legLeft3, claw8, claw7, claw10, bigEarR3, leftEyelid, rider, sideEarR7, eyelidEmoteHappy2, torso,
            leftLeg, claw11, claw12, leftTWing4, jaw, bigEarR2, neck, tailPattern, sideEarR4, rightWing5, eyeLeft,
            claw3, claw5, leftPupil, leftArm, tail1, footLeft3, rightWingEnd, rightEyelid, tail3, leftWing2, rightHipWingB,
            leftWing6, body2, allClaw3, topEarR2, legLeft, rightWing3, teeth, fArmDetR2, rightLeg, neckBase, leftWingEnd,
            nightFury, nubs, footRight3, teeth2, rightArm, footRight, leftWing4, rightHipWingA, rightPupil5, leftWing5,
            saddle, eyelidEmoteHappy, leftFArm2, body, footLeft, sideEarR2, sideEarR3, legRight3, head, eyebrowRight,
            upperJaw, claw16, claw17, eyes, eyeRight, claw13, claw15, eyebrowAngry2, rightTWing4, claw14, claw4, rightWing6,
            legRight, fArmDetR3, rightHipWingC, allClaw2, topEarR3, legRight2, chests, tail4, eyebrowLeft, tail2, claw6,
            pupilRight, claw2, rightWing4, tongue8, leftWing3, rightFArm2, rightWing, eyebrowAngry;

    private float lastRotationAngle = 0;
    private long lastUpdateTime = System.currentTimeMillis();
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

    private void initializeBones() {
        legLeft2 = this.getGeoModel().getBone("leg_left2").orElse(null);
        leftHipWingC = this.getGeoModel().getBone("leftHipWingC").orElse(null);
        leftHipWingB = this.getGeoModel().getBone("leftHipWingB").orElse(null);
        rightWing2 = this.getGeoModel().getBone("right_wing2").orElse(null);
        pupilLeft = this.getGeoModel().getBone("pupil_left").orElse(null);
        claw9 = this.getGeoModel().getBone("Claw9").orElse(null);
        leftWing = this.getGeoModel().getBone("left_wing").orElse(null);
        leftHipWingA = this.getGeoModel().getBone("leftHipWingA").orElse(null);
        legLeft3 = this.getGeoModel().getBone("leg_left3").orElse(null);
        claw8 = this.getGeoModel().getBone("Claw8").orElse(null);
        claw7 = this.getGeoModel().getBone("Claw7").orElse(null);
        claw10 = this.getGeoModel().getBone("claw10").orElse(null);
        bigEarR3 = this.getGeoModel().getBone("BigEarR3").orElse(null);
        leftEyelid = this.getGeoModel().getBone("left_eyelid").orElse(null);
        rider = this.getGeoModel().getBone("rider").orElse(null);
        sideEarR7 = this.getGeoModel().getBone("SideEarR7").orElse(null);
        eyelidEmoteHappy2 = this.getGeoModel().getBone("eyelid_emote_happy2").orElse(null);
        torso = this.getGeoModel().getBone("torso").orElse(null);
        leftLeg = this.getGeoModel().getBone("leftleg").orElse(null);
        claw11 = this.getGeoModel().getBone("claw11").orElse(null);
        claw12 = this.getGeoModel().getBone("claw12").orElse(null);
        leftTWing4 = this.getGeoModel().getBone("leftTWIng4").orElse(null);
        jaw = this.getGeoModel().getBone("jaw").orElse(null);
        bigEarR2 = this.getGeoModel().getBone("BigEarR2").orElse(null);
        neck = this.getGeoModel().getBone("neck").orElse(null);
        tailPattern = this.getGeoModel().getBone("tailPattern").orElse(null);
        sideEarR4 = this.getGeoModel().getBone("SideEarR4").orElse(null);
        rightWing5 = this.getGeoModel().getBone("right_wing5").orElse(null);
        eyeLeft = this.getGeoModel().getBone("eye_left").orElse(null);
        claw3 = this.getGeoModel().getBone("Claw3").orElse(null);
        claw5 = this.getGeoModel().getBone("Claw5").orElse(null);
        leftPupil = this.getGeoModel().getBone("leftpupil").orElse(null);
        leftArm = this.getGeoModel().getBone("leftArm").orElse(null);
        tail1 = this.getGeoModel().getBone("tail1").orElse(null);
        footLeft3 = this.getGeoModel().getBone("foot_left3").orElse(null);
        rightWingEnd = this.getGeoModel().getBone("right_wing_end").orElse(null);
        rightEyelid = this.getGeoModel().getBone("right_eyelid").orElse(null);
        tail3 = this.getGeoModel().getBone("tail3").orElse(null);
        leftWing2 = this.getGeoModel().getBone("left_wing2").orElse(null);
        rightHipWingB = this.getGeoModel().getBone("rightHipWingB").orElse(null);
        leftWing6 = this.getGeoModel().getBone("left_wing6").orElse(null);
        body2 = this.getGeoModel().getBone("body2").orElse(null);
        allClaw3 = this.getGeoModel().getBone("allclaw3").orElse(null);
        topEarR2 = this.getGeoModel().getBone("TopEarR2").orElse(null);
        legLeft = this.getGeoModel().getBone("leg_left").orElse(null);
        rightWing3 = this.getGeoModel().getBone("right_wing3").orElse(null);
        teeth = this.getGeoModel().getBone("teeth").orElse(null);
        fArmDetR2 = this.getGeoModel().getBone("FArmDetR2").orElse(null);
        rightLeg = this.getGeoModel().getBone("rightleg").orElse(null);
        neckBase = this.getGeoModel().getBone("neckbase").orElse(null);
        leftWingEnd = this.getGeoModel().getBone("left_wing_end").orElse(null);
        nightFury = this.getGeoModel().getBone("nightfury").orElse(null);
        nubs = this.getGeoModel().getBone("nubs").orElse(null);
        footRight3 = this.getGeoModel().getBone("foot_right3").orElse(null);
        teeth2 = this.getGeoModel().getBone("teeth2").orElse(null);
        rightArm = this.getGeoModel().getBone("rightArm").orElse(null);
        footRight = this.getGeoModel().getBone("foot_right").orElse(null);
        leftWing4 = this.getGeoModel().getBone("left_wing4").orElse(null);
        rightHipWingA = this.getGeoModel().getBone("rightHipWingA").orElse(null);
        rightPupil5 = this.getGeoModel().getBone("rightPupil5").orElse(null);
        leftWing5 = this.getGeoModel().getBone("left_wing5").orElse(null);
        saddle = this.getGeoModel().getBone("saddle").orElse(null);
        eyelidEmoteHappy = this.getGeoModel().getBone("eyelid_emote_happy").orElse(null);
        leftFArm2 = this.getGeoModel().getBone("leftFArm2").orElse(null);
        body = this.getGeoModel().getBone("body").orElse(null);
        footLeft = this.getGeoModel().getBone("foot_left").orElse(null);
        sideEarR2 = this.getGeoModel().getBone("SideEarR2").orElse(null);
        sideEarR3 = this.getGeoModel().getBone("SideEarR3").orElse(null);
        legRight3 = this.getGeoModel().getBone("leg_right3").orElse(null);
        head = this.getGeoModel().getBone("head").orElse(null);
        eyebrowRight = this.getGeoModel().getBone("eyebrowRight").orElse(null);
        upperJaw = this.getGeoModel().getBone("upperJaw").orElse(null);
        claw16 = this.getGeoModel().getBone("Claw16").orElse(null);
        claw17 = this.getGeoModel().getBone("Claw17").orElse(null);
        eyes = this.getGeoModel().getBone("eyes").orElse(null);
        eyeRight = this.getGeoModel().getBone("eye_right").orElse(null);
        claw13 = this.getGeoModel().getBone("Claw13").orElse(null);
        claw15 = this.getGeoModel().getBone("Claw15").orElse(null);
        eyebrowAngry2 = this.getGeoModel().getBone("eyebrowAngry2").orElse(null);
        rightTWing4 = this.getGeoModel().getBone("rightTWing4").orElse(null);
        claw14 = this.getGeoModel().getBone("Claw14").orElse(null);
        claw4 = this.getGeoModel().getBone("Claw4").orElse(null);
        rightWing6 = this.getGeoModel().getBone("right_wing6").orElse(null);
        legRight = this.getGeoModel().getBone("leg_right").orElse(null);
        fArmDetR3 = this.getGeoModel().getBone("FArmDetR3").orElse(null);
        rightHipWingC = this.getGeoModel().getBone("rightHipWingC").orElse(null);
        allClaw2 = this.getGeoModel().getBone("allClaw2").orElse(null);
        topEarR3 = this.getGeoModel().getBone("TopEarR3").orElse(null);
        legRight2 = this.getGeoModel().getBone("leg_right2").orElse(null);
        chests = this.getGeoModel().getBone("chests").orElse(null);
        tail4 = this.getGeoModel().getBone("tail4").orElse(null);
        eyebrowLeft = this.getGeoModel().getBone("eyebrowLeft").orElse(null);
        tail2 = this.getGeoModel().getBone("tail2").orElse(null);
        claw6 = this.getGeoModel().getBone("Claw6").orElse(null);
        pupilRight = this.getGeoModel().getBone("pupilRight").orElse(null);
        claw2 = this.getGeoModel().getBone("Claw2").orElse(null);
        rightWing4 = this.getGeoModel().getBone("right_wing4").orElse(null);
        tongue8 = this.getGeoModel().getBone("tongue8").orElse(null);
        leftWing3 = this.getGeoModel().getBone("left_wing3").orElse(null);
        rightFArm2 = this.getGeoModel().getBone("rightFArm2").orElse(null);
        rightWing = this.getGeoModel().getBone("right_wing").orElse(null);
        eyebrowAngry = this.getGeoModel().getBone("eyebrowAngry").orElse(null);
    }

    @Override
    public void render(EntityRenderState pAnimatable, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        super.render(pAnimatable, pPoseStack, pBufferSource, pPackedLight);
        initializeBones();

        if (Minecraft.getInstance().isPaused()) {
            return; // Eventually Return to Idle/Sleep/Fly Still etc etc
        }

        if (leftWing != null) {
            long currentTime = System.currentTimeMillis();
            float rotationSpeed = (float) Math.toRadians(36);

            float elapsedTime = (currentTime - lastUpdateTime) / 1000.0f;

            if (entity.isShouldMove()) {
                BaseLogger.log(BaseLogLevel.INFO, "Fury will now walk!");
                lastRotationAngle += rotationSpeed * elapsedTime;
            } else {
                BaseLogger.log(BaseLogLevel.INFO, "Fury will now stand still!");
                lastRotationAngle -= rotationSpeed * elapsedTime;
            }

            lastRotationAngle %= (float) (2 * Math.PI);
            if (lastRotationAngle < 0) {
                lastRotationAngle += (float) (2 * Math.PI);
            }

            BaseLogger.log(BaseLogLevel.INFO, "Setting rotation angle to: " + lastRotationAngle);

            leftWing.setRotY(lastRotationAngle);
            BaseLogger.log(BaseLogLevel.INFO, "Rotation applied to leftWing. Checking current rotation: " + leftWing.getRotY());

            lastUpdateTime = currentTime;
        }
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
