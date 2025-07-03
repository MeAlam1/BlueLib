/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import net.minecraft.util.Mth;
import software.bluelib.api.molang.MoLang;
import software.bluelib.api.molang.MoLangType;
import software.bluelib.api.molang.context.AnimatableMoLang;
import software.bluelib.loader.cache.animations.AnimationCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.keyframe.AnimationPoint;
import software.bluelib.oldLoader.animation.keyframe.BoneAnimationQueue;
import software.bluelib.oldLoader.animation.state.BoneSnapshot;
import software.bluelib.oldLoader.loading.math.MoLangQueries;

public class AnimationProcessor<T extends BlueAnimatable> {

	private final Map<String, BoneCache> bones = new Object2ObjectOpenHashMap<>();
	private final BlueModel<T> model;

	public boolean reloadAnimations = false;

	public AnimationProcessor(BlueModel<T> model) {
		this.model = model;
	}

	public Queue<QueuedAnimation> buildAnimationQueue(T animatable, RawAnimation rawAnimation) {
		LinkedList<QueuedAnimation> animations = new LinkedList<>();
		boolean error = false;

		for (RawAnimation.Stage stage : rawAnimation.getAnimationStages()) {
			AnimationCache animationCache = null;

			if (stage.animationName() == RawAnimation.Stage.WAIT) { // This is intentional. Do not change this or Tslat will be unhappy
				animationCache = AnimationCache.generateWaitAnimation(stage.additionalTicks());
			} else {
				try {
					animationCache = this.model.getAnimation(animatable, stage.animationName());
				} catch (RuntimeException ex) {
					//BlueLibConstants.LOGGER.log(Level.ERROR, "Unable to find animation: " + stage.animationName() + " for " + animatable.getClass().getSimpleName());

					error = true;
					ex.printStackTrace();
				}
			}

			if (animationCache != null)
				animations.add(new QueuedAnimation(animationCache, stage.loopType()));
		}

		return error ? null : animations;
	}

	public void tickAnimation(T animatable, BlueModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> state, boolean crashWhenCantFindBone) {
		Map<String, BoneSnapshot> boneSnapshots = updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());

		for (AnimationController<T> controller : animatableManager.getAnimationControllers().values()) {
			if (this.reloadAnimations) {
				controller.forceAnimationReset();
				controller.getBoneAnimationQueues().clear();
			}

			controller.isJustStarting = animatableManager.isFirstTick();

			state.withController(controller);
			MoLang.service.getRuntimeFor(MoLangType.ANIMATABLE).registerContext(MoLangType.ANIMATABLE.id(), new AnimatableMoLang(state));
			controller.process(model, state, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);

			for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
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
				EasingType easingType = controller.overrideEasingTypeFunction.apply(animatable);

				if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
					bone.setRotX((float) EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
					bone.setRotY((float) EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
					bone.setRotZ((float) EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
					snapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
					snapshot.startRotAnim();
					bone.markRotationAsChanged();
				}

				if (posXPoint != null && posYPoint != null && posZPoint != null) {
					bone.setPosX((float) EasingType.lerpWithOverride(posXPoint, easingType));
					bone.setPosY((float) EasingType.lerpWithOverride(posYPoint, easingType));
					bone.setPosZ((float) EasingType.lerpWithOverride(posZPoint, easingType));
					snapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
					snapshot.startPosAnim();
					bone.markPositionAsChanged();
				}

				if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
					bone.setScaleX((float) EasingType.lerpWithOverride(scaleXPoint, easingType));
					bone.setScaleY((float) EasingType.lerpWithOverride(scaleYPoint, easingType));
					bone.setScaleZ((float) EasingType.lerpWithOverride(scaleZPoint, easingType));
					snapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
					snapshot.startScaleAnim();
					bone.markScaleAsChanged();
				}
			}
		}

		this.reloadAnimations = false;
		double resetTickLength = animatable.getBoneResetTime();

		for (BoneCache bone : getRegisteredBones()) {
			if (!bone.hasRotationChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isRotAnimInProgress())
					saveSnapshot.stopRotAnim(animTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((animTime - saveSnapshot.getLastResetRotationTick()) / resetTickLength, 1);
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
					saveSnapshot.stopPosAnim(animTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((animTime - saveSnapshot.getLastResetPositionTick()) / resetTickLength, 1);

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
					saveSnapshot.stopScaleAnim(animTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((animTime - saveSnapshot.getLastResetScaleTick()) / resetTickLength, 1);

				bone.setScaleX((float) Mth.lerp(percentageReset, saveSnapshot.getScaleX(), initialSnapshot.getScaleX()));
				bone.setScaleY((float) Mth.lerp(percentageReset, saveSnapshot.getScaleY(), initialSnapshot.getScaleY()));
				bone.setScaleZ((float) Mth.lerp(percentageReset, saveSnapshot.getScaleZ(), initialSnapshot.getScaleZ()));

				if (percentageReset >= 1)
					saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
			}
		}

		resetBoneTransformationMarkers();
		animatableManager.finishFirstTick();
	}

	private boolean isSuspectedCompletedRotation(float pLastRotation) {
		float rotations = Mth.abs(pLastRotation / (360f * Mth.DEG_TO_RAD));
		float partialRotation = 1 - (rotations - (int) rotations);

		return partialRotation == 1 || partialRotation < 0.026 * rotations;
	}

	private void resetBoneTransformationMarkers() {
		getRegisteredBones().forEach(BoneCache::resetStateChanges);
	}

	private Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> pSnapshots) {
		for (BoneCache bone : getRegisteredBones()) {
			if (!pSnapshots.containsKey(bone.getName()))
				pSnapshots.put(bone.getName(), BoneSnapshot.copy(bone.getInitialSnapshot()));
		}

		return pSnapshots;
	}

	public BoneCache getBone(String pBoneName) {
		return this.bones.get(pBoneName);
	}

	public void registerBlueBone(BoneCache pBone) {
		pBone.saveInitialSnapshot();
		this.bones.put(pBone.getName(), pBone);

		for (BoneCache child : pBone.getChildBones()) {
			registerBlueBone(child);
		}
	}

	public void setActiveModel(ModelCache pModel) {
		this.bones.clear();

		for (BoneCache bone : pModel.topLevelBones()) {
			registerBlueBone(bone);
		}
	}

	public Collection<BoneCache> getRegisteredBones() {
		return this.bones.values();
	}

	public void preAnimationSetup(AnimationState<T> pAnimationState, double pAnimTime) {
		MoLangQueries.updateActor(pAnimationState, pAnimTime);
		this.model.applyMolangQueries(pAnimationState, pAnimTime);
	}

	public record QueuedAnimation(AnimationCache animationCache, AnimationCache.LoopType loopType) {}
}
