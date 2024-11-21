// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.entity.fury;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
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

        GeoBone bone = this.getGeoModel().getBone("left_wing").orElse(null);
        if (bone != null) {
            long currentTime = System.currentTimeMillis();
            float rotationAngle;

            if (entity.isShouldMove()) {
                //BaseLogger.log(BaseLogLevel.INFO, "Fury is moving!");
                rotationAngle = (float) Math.toRadians((currentTime % 3600) / 10.0);
            } else {
                //BaseLogger.log(BaseLogLevel.INFO, "not!");
                rotationAngle = (float) Math.toRadians(-(currentTime % 3600) / 10.0);
            }

            bone.setRotY(rotationAngle);
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
