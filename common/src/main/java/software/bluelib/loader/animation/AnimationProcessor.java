package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.Level;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.keyframe.AnimationPoint;
import software.bluelib.loader.animation.keyframe.BoneAnimationQueue;
import software.bluelib.loader.animation.state.BoneSnapshot;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.loading.math.MolangQueries;
import software.bluelib.loader.model.GeoModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AnimationProcessor<T extends GeoAnimatable> {
	private final Map<String, GeoBone> bones = new Object2ObjectOpenHashMap<>();
	private final GeoModel<T> model;

	public boolean reloadAnimations = false;

	public AnimationProcessor(GeoModel<T> model) {
		this.model = model;
	}

	
	public Queue<QueuedAnimation> buildAnimationQueue(T animatable, RawAnimation rawAnimation) {
		LinkedList<QueuedAnimation> animations = new LinkedList<>();
		boolean error = false;

		for (RawAnimation.Stage stage : rawAnimation.getAnimationStages()) {
			Animation animation = null;

			if (stage.animationName() == RawAnimation.Stage.WAIT) { // This is intentional. Do not change this or Tslat will be unhappy
				animation = Animation.generateWaitAnimation(stage.additionalTicks());
			}
			else {
				try {
					animation = this.model.getAnimation(animatable, stage.animationName());
				}
				catch (RuntimeException ex) {
					GeckoLibConstants.LOGGER.log(Level.ERROR, "Unable to find animation: " + stage.animationName() + " for " + animatable.getClass().getSimpleName());

					error = true;
					ex.printStackTrace();
				}
			}

			if (animation != null)
				animations.add(new QueuedAnimation(animation, stage.loopType()));
		}

		return error ? null : animations;
	}

	
	public void tickAnimation(T animatable, GeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> state, boolean crashWhenCantFindBone) {
		Map<String, BoneSnapshot> boneSnapshots = updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());

		for (AnimationController<T> controller : animatableManager.getAnimationControllers().values()) {
			if (this.reloadAnimations) {
				controller.forceAnimationReset();
				controller.getBoneAnimationQueues().clear();
			}

			controller.isJustStarting = animatableManager.isFirstTick();

			state.withController(controller);
			controller.process(model, state, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);

			for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
				GeoBone bone = boneAnimation.bone();
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
					bone.setRotX((float)EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
					bone.setRotY((float)EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
					bone.setRotZ((float)EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
					snapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
					snapshot.startRotAnim();
					bone.markRotationAsChanged();
				}

				if (posXPoint != null && posYPoint != null && posZPoint != null) {
					bone.setPosX((float)EasingType.lerpWithOverride(posXPoint, easingType));
					bone.setPosY((float)EasingType.lerpWithOverride(posYPoint, easingType));
					bone.setPosZ((float)EasingType.lerpWithOverride(posZPoint, easingType));
					snapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
					snapshot.startPosAnim();
					bone.markPositionAsChanged();
				}

				if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
					bone.setScaleX((float)EasingType.lerpWithOverride(scaleXPoint, easingType));
					bone.setScaleY((float)EasingType.lerpWithOverride(scaleYPoint, easingType));
					bone.setScaleZ((float)EasingType.lerpWithOverride(scaleZPoint, easingType));
					snapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
					snapshot.startScaleAnim();
					bone.markScaleAsChanged();
				}
			}
		}

		this.reloadAnimations = false;
		double resetTickLength = animatable.getBoneResetTime();

		for (GeoBone bone : getRegisteredBones()) {
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

				bone.setRotX((float)Mth.lerp(percentageReset, lastXRot, initialRotX));
				bone.setRotY((float)Mth.lerp(percentageReset, lastYRot, initialRotY));
				bone.setRotZ((float)Mth.lerp(percentageReset, lastZRot, initialRotZ));

				if (percentageReset >= 1)
					saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
			}

			if (!bone.hasPositionChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isPosAnimInProgress())
					saveSnapshot.stopPosAnim(animTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((animTime - saveSnapshot.getLastResetPositionTick()) / resetTickLength, 1);

				bone.setPosX((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetX(), initialSnapshot.getOffsetX()));
				bone.setPosY((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetY(), initialSnapshot.getOffsetY()));
				bone.setPosZ((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetZ(), initialSnapshot.getOffsetZ()));

				if (percentageReset >= 1)
					saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
			}

			if (!bone.hasScaleChanged()) {
				BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
				BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

				if (saveSnapshot.isScaleAnimInProgress())
					saveSnapshot.stopScaleAnim(animTime);

				double percentageReset = resetTickLength == 0 ? 1 : Math.min((animTime - saveSnapshot.getLastResetScaleTick()) / resetTickLength, 1);

				bone.setScaleX((float)Mth.lerp(percentageReset, saveSnapshot.getScaleX(), initialSnapshot.getScaleX()));
				bone.setScaleY((float)Mth.lerp(percentageReset, saveSnapshot.getScaleY(), initialSnapshot.getScaleY()));
				bone.setScaleZ((float)Mth.lerp(percentageReset, saveSnapshot.getScaleZ(), initialSnapshot.getScaleZ()));

				if (percentageReset >= 1)
					saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
			}
		}

		resetBoneTransformationMarkers();
		animatableManager.finishFirstTick();
	}

	
	private boolean isSuspectedCompletedRotation(float lastRotation) {
		float rotations = Mth.abs(lastRotation / (360f * Mth.DEG_TO_RAD));
		float partialRotation = 1 - (rotations - (int)rotations);

		return partialRotation == 1 || partialRotation < 0.026 * rotations;
	}

	
	private void resetBoneTransformationMarkers() {
		getRegisteredBones().forEach(GeoBone::resetStateChanges);
	}

	
	private Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> snapshots) {
		for (GeoBone bone : getRegisteredBones()) {
			if (!snapshots.containsKey(bone.getName()))
				snapshots.put(bone.getName(), BoneSnapshot.copy(bone.getInitialSnapshot()));
		}

		return snapshots;
	}

	
	public GeoBone getBone(String boneName) {
		return this.bones.get(boneName);
	}

	
	public void registerGeoBone(GeoBone bone) {
		bone.saveInitialSnapshot();
		this.bones.put(bone.getName(), bone);

		for (GeoBone child : bone.getChildBones()) {
			registerGeoBone(child);
		}
	}

	
	public void setActiveModel(BakedGeoModel model) {
		this.bones.clear();

		for (GeoBone bone : model.topLevelBones()) {
			registerGeoBone(bone);
		}
	}

	
	public Collection<GeoBone> getRegisteredBones() {
		return this.bones.values();
	}

	
	public void preAnimationSetup(AnimationState<T> animationState, double animTime) {
		MolangQueries.updateActor(animationState, animTime);
		this.model.applyMolangQueries(animationState, animTime);
	}

	
	public record QueuedAnimation(Animation animation, Animation.LoopType loopType) {}
}
