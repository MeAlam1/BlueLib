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
import software.bluelib.example.entity.fury.animations.IdleAnims;
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

    protected float lastRotationAngle = 0;
    protected long lastUpdateTime = System.currentTimeMillis();
    protected static Boolean areBonesLogged = false;
    protected static FuryEntity entity;

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


    private void initializeNadderBones() {
        wingLeftS4 = this.getGeoModel().getBone("WingLeftS4").orElse(null);
        snoot = this.getGeoModel().getBone("Snoot").orElse(null);
        tail2Spikes = this.getGeoModel().getBone("Tail2Spikes").orElse(null);
        tongue = this.getGeoModel().getBone("Tongue").orElse(null);
        tail5Spikes = this.getGeoModel().getBone("Tail5Spikes").orElse(null);
        spikeLeft5 = this.getGeoModel().getBone("SpikeLeft5").orElse(null);
        wingRightArm = this.getGeoModel().getBone("WingRightArm").orElse(null);
        head = this.getGeoModel().getBone("Head").orElse(null);
        nostrilRight1 = this.getGeoModel().getBone("NostrilRight1").orElse(null);
        talon4 = this.getGeoModel().getBone("Talon4").orElse(null);
        talon2 = this.getGeoModel().getBone("Talon2").orElse(null);
        spikeRight1 = this.getGeoModel().getBone("SpikeRight1").orElse(null);
        talon3 = this.getGeoModel().getBone("Talon3").orElse(null);
        jaw = this.getGeoModel().getBone("Jaw").orElse(null);
        talon1 = this.getGeoModel().getBone("Talon1").orElse(null);
        nostrilRight = this.getGeoModel().getBone("NostrilRight").orElse(null);
        legRight4 = this.getGeoModel().getBone("LegRight4").orElse(null);
        neck3 = this.getGeoModel().getBone("Neck3").orElse(null);
        tongueSplitRight2 = this.getGeoModel().getBone("TongueSplitRight2").orElse(null);
        neck2 = this.getGeoModel().getBone("Neck2").orElse(null);
        legRight3 = this.getGeoModel().getBone("LegRight3").orElse(null);
        legRight5 = this.getGeoModel().getBone("LegRight5").orElse(null);
        wingRightBase = this.getGeoModel().getBone("WingRightBase").orElse(null);
        backtoe = this.getGeoModel().getBone("Backtoe").orElse(null);
        talons = this.getGeoModel().getBone("Talons").orElse(null);
        s1Arm2 = this.getGeoModel().getBone("S1Arm2").orElse(null);
        s1Arm3 = this.getGeoModel().getBone("S1Arm3").orElse(null);
        eye = this.getGeoModel().getBone("Eye").orElse(null);
        tongueSplitLeft2 = this.getGeoModel().getBone("TongueSplitLeft2").orElse(null);
        spike1 = this.getGeoModel().getBone("Spike1").orElse(null);
        teeth = this.getGeoModel().getBone("Teeth").orElse(null);
        talon10 = this.getGeoModel().getBone("Talon10").orElse(null);
        tongueSplitRight1 = this.getGeoModel().getBone("TongueSplitRight1").orElse(null);
        wingLeftHalf = this.getGeoModel().getBone("WingLeftHalf").orElse(null);
        wingRightS5 = this.getGeoModel().getBone("WingRightS5").orElse(null);
        wingRight = this.getGeoModel().getBone("WingRight").orElse(null);
        eyelid2 = this.getGeoModel().getBone("Eyelid2").orElse(null);
        talon6 = this.getGeoModel().getBone("Talon6").orElse(null);
        tail = this.getGeoModel().getBone("Tail").orElse(null);
        spikeRight4 = this.getGeoModel().getBone("SpikeRight4").orElse(null);
        spikeRight2 = this.getGeoModel().getBone("SpikeRight2").orElse(null);
        wingLeftS6 = this.getGeoModel().getBone("WingLeftS6").orElse(null);
        neck = this.getGeoModel().getBone("Neck").orElse(null);
        wingLeftS2 = this.getGeoModel().getBone("WingLeftS2").orElse(null);
        eyelid = this.getGeoModel().getBone("Eyelid").orElse(null);
        wingRightS7 = this.getGeoModel().getBone("WingRightS7").orElse(null);
        talon8 = this.getGeoModel().getBone("Talon8").orElse(null);
        wingRightS3 = this.getGeoModel().getBone("WingRightS3").orElse(null);
        nose = this.getGeoModel().getBone("Nose").orElse(null);
        nostrilLeft = this.getGeoModel().getBone("NostrilLeft").orElse(null);
        spikeLeft2 = this.getGeoModel().getBone("SpikeLeft2").orElse(null);
        horn2 = this.getGeoModel().getBone("Horn2").orElse(null);
        spikeLeft4 = this.getGeoModel().getBone("SpikeLeft4").orElse(null);
        tail3 = this.getGeoModel().getBone("Tail3").orElse(null);
        tail5 = this.getGeoModel().getBone("Tail5").orElse(null);
        tail7 = this.getGeoModel().getBone("Tail7").orElse(null);
        wingLeftBase = this.getGeoModel().getBone("WingLeftBase").orElse(null);
        legLeft2 = this.getGeoModel().getBone("LegLeft2").orElse(null);
        bags = this.getGeoModel().getBone("Bags").orElse(null);
        wingRightS1 = this.getGeoModel().getBone("WingRightS1").orElse(null);
        spikes2 = this.getGeoModel().getBone("Spikes2").orElse(null);
        talons2 = this.getGeoModel().getBone("Talons2").orElse(null);
        legRightrot = this.getGeoModel().getBone("LegRightrot").orElse(null);
        backtoe2 = this.getGeoModel().getBone("Backtoe2").orElse(null);
        teeth2 = this.getGeoModel().getBone("Teeth2").orElse(null);
        wingRightS2 = this.getGeoModel().getBone("WingRightS2").orElse(null);
        pupil = this.getGeoModel().getBone("Pupil").orElse(null);
        root = this.getGeoModel().getBone("root").orElse(null);
        torso = this.getGeoModel().getBone("Torso").orElse(null);
        saddle = this.getGeoModel().getBone("Saddle").orElse(null);
        legLeft3 = this.getGeoModel().getBone("LegLeft3").orElse(null);
        legLeft = this.getGeoModel().getBone("LegLeft").orElse(null);
        legLeftrot = this.getGeoModel().getBone("LegLeftrot").orElse(null);
        wingRightHalf = this.getGeoModel().getBone("WingRightHalf").orElse(null);
        wingLeftrot = this.getGeoModel().getBone("WingLeftrot").orElse(null);
        legLeft4 = this.getGeoModel().getBone("LegLeft4").orElse(null);
        spikes4 = this.getGeoModel().getBone("Spikes4").orElse(null);
        tail3Spikes = this.getGeoModel().getBone("Tail3Spikes").orElse(null);
        legLeft5 = this.getGeoModel().getBone("LegLeft5").orElse(null);
        nostrilLeft1 = this.getGeoModel().getBone("NostrilLeft1").orElse(null);
        spikes3 = this.getGeoModel().getBone("Spikes3").orElse(null);
        spikes6 = this.getGeoModel().getBone("Spikes6").orElse(null);
        spikes5 = this.getGeoModel().getBone("Spikes5").orElse(null);
        legRight2 = this.getGeoModel().getBone("LegRight2").orElse(null);
        spikes = this.getGeoModel().getBone("Spikes").orElse(null);
        legRight = this.getGeoModel().getBone("LegRight").orElse(null);
        eye2 = this.getGeoModel().getBone("Eye2").orElse(null);
        talon7 = this.getGeoModel().getBone("Talon7").orElse(null);
        wingRightS6 = this.getGeoModel().getBone("WingRightS6").orElse(null);
        wingLeftArm = this.getGeoModel().getBone("WingLeftArm").orElse(null);
        tail6Spikes = this.getGeoModel().getBone("Tail6Spikes").orElse(null);
        tongueSplitLeft1 = this.getGeoModel().getBone("TongueSplitLeft1").orElse(null);
        wingLeft = this.getGeoModel().getBone("WingLeft").orElse(null);
        tail4Spikes = this.getGeoModel().getBone("Tail4Spikes").orElse(null);
        talon5 = this.getGeoModel().getBone("Talon5").orElse(null);
        spikeRight5 = this.getGeoModel().getBone("SpikeRight5").orElse(null);
        spikeRight3 = this.getGeoModel().getBone("SpikeRight3").orElse(null);
        spikeLeft3 = this.getGeoModel().getBone("SpikeLeft3").orElse(null);
        horn = this.getGeoModel().getBone("Horn").orElse(null);
        tongue2 = this.getGeoModel().getBone("Tongue2").orElse(null);
        horn3 = this.getGeoModel().getBone("Horn3").orElse(null);
        wingRightrot = this.getGeoModel().getBone("WingRightrot").orElse(null);
        talon9 = this.getGeoModel().getBone("Talon9").orElse(null);
        wingRightS4 = this.getGeoModel().getBone("WingRightS4").orElse(null);
        rotation = this.getGeoModel().getBone("Rotation").orElse(null);
        wingLeftS1 = this.getGeoModel().getBone("WingLeftS1").orElse(null);
        wingLeftS5 = this.getGeoModel().getBone("WingLeftS5").orElse(null);
        wingLeftS3 = this.getGeoModel().getBone("WingLeftS3").orElse(null);
        pupil2 = this.getGeoModel().getBone("Pupil2").orElse(null);
        wingLeftS7 = this.getGeoModel().getBone("WingLeftS7").orElse(null);
        tailBottom = this.getGeoModel().getBone("TailBottom").orElse(null);
        tail1Spikes = this.getGeoModel().getBone("Tail1Spikes").orElse(null);
        tail2 = this.getGeoModel().getBone("Tail2").orElse(null);
        tail6 = this.getGeoModel().getBone("Tail6").orElse(null);
        spikeLeft1 = this.getGeoModel().getBone("SpikeLeft1").orElse(null);
        tail4 = this.getGeoModel().getBone("Tail4").orElse(null);
    }


    @Override
    public void render(EntityRenderState pAnimatable, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        super.render(pAnimatable, pPoseStack, pBufferSource, pPackedLight);
        initializeNadderBones();

        /*if (!areBonesLogged) {
            logAllBones();
            areBonesLogged = true;
        }*/

        if (Minecraft.getInstance().isPaused()) {
            return; // Eventually Return to Idle/Sleep/Fly Still etc etc
        }

        long currentTime = System.currentTimeMillis();
        float rotationSpeed = (float) Math.toRadians(36);

        float elapsedTime = (currentTime - lastUpdateTime) / 1000.0f;

        if (entity.isShouldMove()) {
            BaseLogger.log(BaseLogLevel.INFO, "Fury will now walk!");
            lastRotationAngle += rotationSpeed * elapsedTime;
        } else {
            IdleAnims.playIdleAnims();
            //BaseLogger.log(BaseLogLevel.INFO, "Fury will now stand still!");
            //lastRotationAngle -= rotationSpeed * elapsedTime;
        }

        lastRotationAngle %= (float) (2 * Math.PI);
        if (lastRotationAngle < 0) {
            lastRotationAngle += (float) (2 * Math.PI);
        }

        //BaseLogger.log(BaseLogLevel.INFO, "Setting rotation angle to: " + lastRotationAngle);

        //wingLeftrot.setRotY(lastRotationAngle);
        //BaseLogger.log(BaseLogLevel.INFO, "Rotation applied to leftWing. Checking current rotation: " + leftWing.getRotY());

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


    public static GeoBone wingLeftS4, snoot, tail2Spikes, tongue, tail5Spikes, spikeLeft5, wingRightArm, head,
            nostrilRight1, talon4, talon2, spikeRight1, talon3, jaw, talon1, nostrilRight,
            legRight4, neck3, tongueSplitRight2, neck2, legRight3, legRight5, wingRightBase, backtoe,
            talons, s1Arm2, s1Arm3, eye, tongueSplitLeft2, spike1, teeth, talon10, tongueSplitRight1,
            wingLeftHalf, wingRightS5, wingRight, eyelid2, talon6, tail, spikeRight4, spikeRight2,
            wingLeftS6, neck, wingLeftS2, eyelid, wingRightS7, talon8, wingRightS3, nose, nostrilLeft,
            spikeLeft2, horn2, spikeLeft4, tail3, tail5, tail7, wingLeftBase, legLeft2, bags,
            wingRightS1, spikes2, talons2, legRightrot, backtoe2, teeth2, wingRightS2, pupil,
            root, torso, saddle, legLeft3, legLeft, legLeftrot, wingRightHalf, wingLeftrot, legLeft4,
            spikes4, tail3Spikes, legLeft5, nostrilLeft1, spikes3, spikes6, spikes5, legRight2,
            spikes, legRight, eye2, talon7, wingRightS6, wingLeftArm, tail6Spikes, tongueSplitLeft1,
            wingLeft, tail4Spikes, talon5, spikeRight5, spikeRight3, spikeLeft3, horn, tongue2, horn3,
            wingRightrot, talon9, wingRightS4, rotation, wingLeftS1, wingLeftS5, wingLeftS3,
            pupil2, wingLeftS7, tailBottom, tail1Spikes, tail2, tail6, spikeLeft1, tail4;

    // Fury

    /*public static GeoBone legLeft2, leftHipWingC, leftHipWingB, rightWing2, pupilLeft, claw9, leftWing, leftHipWingA,
            legLeft3, claw8, claw7, claw10, bigEarR3, leftEyelid, rider, sideEarR7, eyelidEmoteHappy2, torso,
            leftLeg, claw11, claw12, leftTWing4, jaw, bigEarR2, neck, tailPattern, sideEarR4, rightWing5, eyeLeft,
            claw3, claw5, leftPupil, leftArm, tail1, footLeft3, rightWingEnd, rightEyelid, tail3, leftWing2, rightHipWingB,
            leftWing6, body2, allClaw3, topEarR2, legLeft, rightWing3, teeth, fArmDetR2, rightLeg, neckBase, leftWingEnd,
            nightFury, nubs, footRight3, teeth2, rightArm, footRight, leftWing4, rightHipWingA, rightPupil5, leftWing5,
            saddle, eyelidEmoteHappy, leftFArm2, body, footLeft, sideEarR2, sideEarR3, legRight3, head, eyebrowRight,
            upperJaw, claw16, claw17, eyes, eyeRight, claw13, claw15, eyebrowAngry2, rightTWing4, claw14, claw4, rightWing6,
            legRight, fArmDetR3, rightHipWingC, allClaw2, topEarR3, legRight2, chests, tail4, eyebrowLeft, tail2, claw6,
            pupilRight, claw2, rightWing4, tongue8, leftWing3, rightFArm2, rightWing, eyebrowAngry;*/

    /*private void initializeFuryBones() {
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
    }*/
}
