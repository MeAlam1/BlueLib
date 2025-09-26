/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.animation.keyframe.AnimationPoint;
import software.bluelib.loader.animation.keyframe.BoneAnimationFrame;
import software.bluelib.loader.animation.math.Easing;
import software.bluelib.loader.cache.animation.AnimationCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.geckolib.animations.LoopType;
import software.bluelib.loader.geckolib.math.MathParser;
import software.bluelib.loader.geckolib.math.MoLangQueries;
import software.bluelib.loader.model.BlueModel;

public class AnimationProcessor<T extends BlueAnimatable> {

	@NotNull
	private final Map<String, BoneCache> bones = new Object2ObjectOpenHashMap<>();
	@NotNull
	private final BlueModel<T> model;

	public boolean reloadAnimations = false;

	public AnimationProcessor(@NotNull BlueModel<T> pModel) {
		this.model = pModel;
	}

	@Nullable
	public Queue<QueuedAnimation> buildAnimationQueue(@NotNull T pAnimatable, @NotNull Animation pAnimation) {
		LinkedList<QueuedAnimation> animations = new LinkedList<>();
		boolean error = false;

		for (Animation.Stage stage : pAnimation.getAnimationStages()) {
			AnimationCache animationCache = null;

			if (stage.animationName() == Animation.Stage.WAIT) {
				animationCache = AnimationCache.generateWaitAnimation(stage.additionalTicks());
			} else {
				try {
					animationCache = this.model.getAnimation(pAnimatable, stage.animationName());
				} catch (RuntimeException ex) {
					BaseLogger.log(BaseLogLevel.ERROR, "Unable to find animation: " + stage.animationName() + " for " + pAnimatable.getClass().getSimpleName(), ex);
					error = true;
				}
			}

			if (animationCache != null)
				animations.add(new QueuedAnimation(animationCache, stage.loopType()));
		}

		return error ? null : animations;
	}

	public void tickAnimation(@NotNull T pAnimatable, @NotNull BlueModel<T> pModel, @NotNull AnimatableManager<T> pAnimatableManager, double pAnimTime, @NotNull AnimationState<T> pState, boolean pCrashWhenCantFindBone) {
		Map<String, BoneSnapshot> boneSnapshots = updateBoneSnapshots(pAnimatableManager.getBoneSnapshotCollection());

		for (AnimationController<T> controller : pAnimatableManager.getAnimationControllers().values()) {
			if (this.reloadAnimations) {
				controller.forceAnimationReset();
				controller.getBoneAnimationQueues().clear();
			}

			controller.isJustStarting = pAnimatableManager.isFirstTick();

			pState.withController(controller);
			// TODO: REMOVE!!!!
			MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> pState.getController() != null ? pState.getController().getAnimTime() : 0d);
			controller.process(pModel, pState, this.bones, boneSnapshots, pAnimTime, pCrashWhenCantFindBone);

			for (BoneAnimationFrame boneAnimation : controller.getBoneAnimationQueues().values()) {
				BoneCache bone = boneAnimation.bone();
				BoneSnapshot snapshot = boneSnapshots.get(bone.getName());
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();

				AnimationPoint rotXPoint = boneAnimation.rotationXQueue().poll();
				AnimationPoint rotYPoint = boneAnimation.rotationYQueue().poll();
				AnimationPoint rotZPoint = boneAnimation.rotationZQueue().poll();
				AnimationPoint posXPoint = boneAnimation.positionXQueue().poll();
				AnimationPoint posYPoint = boneAnimation.positionYQueue().poll();
				AnimationPoint posZPoint = boneAnimation.positionZQueue().poll();
				AnimationPoint scaleXPoint = boneAnimation.scaleXQueue().poll();
				AnimationPoint scaleYPoint = boneAnimation.scaleYQueue().poll();
				AnimationPoint scaleZPoint = boneAnimation.scaleZQueue().poll();
				Easing easing = controller.overrideEasingTypeFunction.apply(pAnimatable);

				if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
					bone.setRotX((float) Easing.lerpWithOverride(rotXPoint, easing) + initialSnapshot.getRotX());
					bone.setRotY((float) Easing.lerpWithOverride(rotYPoint, easing) + initialSnapshot.getRotY());
					bone.setRotZ((float) Easing.lerpWithOverride(rotZPoint, easing) + initialSnapshot.getRotZ());
					snapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
					snapshot.startRotAnim();
					bone.markRotationAsChanged();
				}

				if (posXPoint != null && posYPoint != null && posZPoint != null) {
					bone.setPosX((float) Easing.lerpWithOverride(posXPoint, easing));
					bone.setPosY((float) Easing.lerpWithOverride(posYPoint, easing));
					bone.setPosZ((float) Easing.lerpWithOverride(posZPoint, easing));
					snapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
					snapshot.startPosAnim();
					bone.markPositionAsChanged();
				}

				if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
					bone.setScaleX((float) Easing.lerpWithOverride(scaleXPoint, easing));
					bone.setScaleY((float) Easing.lerpWithOverride(scaleYPoint, easing));
					bone.setScaleZ((float) Easing.lerpWithOverride(scaleZPoint, easing));
					snapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
					snapshot.startScaleAnim();
					bone.markScaleAsChanged();
				}
			}
		}

		this.reloadAnimations = false;
		double resetTickLength = pAnimatable.boneResetTime();

		for (BoneCache bone : getRegisteredBones()) {
			if (!bone.hasRotationChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isRotAnimInProgress())
					saveSnapshot.stopRotAnim(pAnimTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((pAnimTime - saveSnapshot.getLastResetRotationTick()) / resetTickLength, 1);
				float initialRotX = initialSnapshot.getRotX();
				float initialRotY = initialSnapshot.getRotY();
				float initialRotZ = initialSnapshot.getRotZ();
				float lastXRot = saveSnapshot.getRotX();
				float lastYRot = saveSnapshot.getRotY();
				float lastZRot = saveSnapshot.getRotZ();

				// Let's capture suspected full-rotations and prevent them from back-lerping
				// Far from perfect, but the best I can think of until I redo the system itself
				if (percentageReset == 0) {
					if (lastXRot != initialRotX && isSuspectedCompletedRotation(lastXRot)) {
						lastXRot = initialRotX;
						percentageReset = 1;
					}

					if (lastYRot != initialRotY && isSuspectedCompletedRotation(lastYRot)) {
						lastYRot = initialRotY;
						percentageReset = 1;
					}

					if (lastZRot != initialRotZ && isSuspectedCompletedRotation(lastZRot)) {
						lastZRot = initialRotZ;
						percentageReset = 1;
					}
				}

				bone.setRotX((float) Mth.lerp(percentageReset, lastXRot, initialRotX));
				bone.setRotY((float) Mth.lerp(percentageReset, lastYRot, initialRotY));
				bone.setRotZ((float) Mth.lerp(percentageReset, lastZRot, initialRotZ));

				if (percentageReset >= 1)
					saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
			}

			if (!bone.hasPositionChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isPosAnimInProgress())
					saveSnapshot.stopPosAnim(pAnimTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((pAnimTime - saveSnapshot.getLastResetPositionTick()) / resetTickLength, 1);

				bone.setPosX((float) Mth.lerp(percentageReset, saveSnapshot.getOffsetX(), initialSnapshot.getOffsetX()));
				bone.setPosY((float) Mth.lerp(percentageReset, saveSnapshot.getOffsetY(), initialSnapshot.getOffsetY()));
				bone.setPosZ((float) Mth.lerp(percentageReset, saveSnapshot.getOffsetZ(), initialSnapshot.getOffsetZ()));

				if (percentageReset >= 1)
					saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
			}

			if (!bone.hasScaleChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isScaleAnimInProgress())
					saveSnapshot.stopScaleAnim(pAnimTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((pAnimTime - saveSnapshot.getLastResetScaleTick()) / resetTickLength, 1);

				bone.setScaleX((float) Mth.lerp(percentageReset, saveSnapshot.getScaleX(), initialSnapshot.getScaleX()));
				bone.setScaleY((float) Mth.lerp(percentageReset, saveSnapshot.getScaleY(), initialSnapshot.getScaleY()));
				bone.setScaleZ((float) Mth.lerp(percentageReset, saveSnapshot.getScaleZ(), initialSnapshot.getScaleZ()));

				if (percentageReset >= 1)
					saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
			}
		}

		resetBoneTransformationMarkers();
		pAnimatableManager.finishFirstTick();
	}

	private boolean isSuspectedCompletedRotation(float pLastRotation) {
		float rotations = Mth.abs(pLastRotation / (360f * Mth.DEG_TO_RAD));
		float partialRotation = 1 - (rotations - (int) rotations);

		return partialRotation == 1 || partialRotation < 0.026 * rotations;
	}

	private void resetBoneTransformationMarkers() {
		getRegisteredBones().forEach(BoneCache::resetStateChanges);
	}

	@NotNull
	private Map<String, BoneSnapshot> updateBoneSnapshots(@NotNull Map<String, BoneSnapshot> pSnapshots) {
		for (BoneCache bone : getRegisteredBones()) {
			if (!pSnapshots.containsKey(bone.getName()))
				pSnapshots.put(bone.getName(), BoneSnapshot.copy(bone.getInitialSnapshot()));
		}

		return pSnapshots;
	}

	@NotNull
	public BoneCache getBone(@NotNull String pBoneName) {
		return this.bones.get(pBoneName);
	}

	public void registerBlueBone(@NotNull BoneCache pBone) {
		pBone.saveInitialSnapshot();
		this.bones.put(pBone.getName(), pBone);

		for (BoneCache child : pBone.getChildBones()) {
			registerBlueBone(child);
		}
	}

	public void setActiveModel(@NotNull ModelCache pModel) {
		this.bones.clear();

		for (BoneCache bone : pModel.topLevelBones()) {
			registerBlueBone(bone);
		}
	}

	@NotNull
	public Collection<BoneCache> getRegisteredBones() {
		return this.bones.values();
	}

	public void preAnimationSetup(@NotNull AnimationState<T> pAnimationState, double pAnimTime) {
		MoLangQueries.updateActor(pAnimationState, pAnimTime);
		this.model.applyMolangQueries(pAnimationState, pAnimTime);
	}

	public record QueuedAnimation(@NotNull AnimationCache animationCache, @NotNull LoopType loopType) {}
}
