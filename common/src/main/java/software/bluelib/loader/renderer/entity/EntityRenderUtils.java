package software.bluelib.loader.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityRenderUtils {

	private EntityRenderUtils() {}

	public static float getLerpBodyRot(@Nullable LivingEntity pEntity, float pPartialTick) {
		return pEntity == null ? 0 : Mth.rotLerp(pPartialTick, pEntity.yBodyRotO, pEntity.yBodyRot);
	}

	public static float getLerpHeadRot(@Nullable LivingEntity pEntity, float pPartialTick) {
		return pEntity == null ? 0 : Mth.rotLerp(pPartialTick, pEntity.yHeadRotO, pEntity.yHeadRot);
	}

	public static float getNetHeadYaw(float lerpHeadRot, float lerpBodyRot) {
		return lerpHeadRot - lerpBodyRot;
	}

	public static float[] adjustSittingRotations(
			boolean pShouldSit,
			@NotNull Entity pAnimatable,
			float pLerpHeadRot,
			float pLerpBodyRot,
			float pPartialTick) {
		float netHeadYaw = pLerpHeadRot - pLerpBodyRot;

		if (pShouldSit && pAnimatable.getVehicle() instanceof LivingEntity livingentity) {
			pLerpBodyRot = Mth.rotLerp(pPartialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
			netHeadYaw = pLerpHeadRot - pLerpBodyRot;
			float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
			pLerpBodyRot = pLerpHeadRot - clampedHeadYaw;

			if (clampedHeadYaw * clampedHeadYaw > 2500f)
				pLerpBodyRot += clampedHeadYaw * 0.2f;

			netHeadYaw = pLerpHeadRot - pLerpBodyRot;
		}

		return new float[] { pLerpBodyRot, netHeadYaw };
	}

	public static void applySleepingTranslation(@NotNull PoseStack pPoseStack, @NotNull LivingEntity pLivingEntity) {
		if (pLivingEntity.getPose() == Pose.SLEEPING) {
			Direction bedDirection = pLivingEntity.getBedOrientation();
			if (bedDirection != null) {
				float eyePosOffset = pLivingEntity.getEyeHeight(Pose.STANDING) - 0.1F;
				pPoseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
			}
		}
	}

	public static float[] computeLimbSwing(@NotNull LivingEntity pLivingEntity, boolean pShouldSit, float pPartialTick) {
		float limbSwingAmount = 0;
		float limbSwing = 0;

		if (!pShouldSit && pLivingEntity.isAlive()) {
			limbSwingAmount = pLivingEntity.walkAnimation.speed(pPartialTick);
			limbSwing = pLivingEntity.walkAnimation.position(pPartialTick);

			if (pLivingEntity.isBaby())
				limbSwing *= 3f;

			if (limbSwingAmount > 1f)
				limbSwingAmount = 1f;
		}
		return new float[] { limbSwing, limbSwingAmount };
	}

	public static boolean isEntityMoving(@NotNull LivingEntity pLivingEntity, float pMotionThreshold, float pLimbSwingAmount) {
		Vec3 velocity = pLivingEntity.getDeltaMovement();
		float avgVelocity = (float) ((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);
		return avgVelocity >= pMotionThreshold && pLimbSwingAmount != 0;
	}
}
