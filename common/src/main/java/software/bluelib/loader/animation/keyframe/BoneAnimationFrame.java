package software.bluelib.loader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.animation.keyframe.frame.AnimationFrameVector;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.loader.cache.model.BoneCache;

public record BoneAnimationFrame(@NotNull BoneCache bone,
                                 @NotNull AnimationFrameVector rotation,
                                 @NotNull AnimationFrameVector position,
                                 @NotNull AnimationFrameVector scale) {

	public BoneAnimationFrame(@NotNull BoneCache pBone) {
		this(pBone, new AnimationFrameVector(), new AnimationFrameVector(), new AnimationFrameVector());
	}

	public void addNextPosition(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
	                            @NotNull BoneSnapshot pStartSnapshot,
	                            @NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		position.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getOffsetX(), pNextX.startValue(),
				pStartSnapshot.getOffsetY(), pNextY.startValue(),
				pStartSnapshot.getOffsetZ(), pNextZ.startValue());
	}

	public void addNextScale(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
	                         @NotNull BoneSnapshot pStartSnapshot,
	                         @NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		scale.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getScaleX(), pNextX.startValue(),
				pStartSnapshot.getScaleY(), pNextY.startValue(),
				pStartSnapshot.getScaleZ(), pNextZ.startValue());
	}

	public void addNextRotation(@Nullable KeyframeCache<?> pKeyFrame, double pLerpedTick, double pTransitionLength,
	                            @NotNull BoneSnapshot pStartSnapshot, @NotNull BoneSnapshot pInitialSnapshot,
	                            @NotNull InterpolationData pNextX, @NotNull InterpolationData pNextY, @NotNull InterpolationData pNextZ) {
		rotation.addPoint(pKeyFrame, pLerpedTick, pTransitionLength,
				pStartSnapshot.getRotX() - pInitialSnapshot.getRotX(), pNextX.startValue(),
				pStartSnapshot.getRotY() - pInitialSnapshot.getRotY(), pNextY.startValue(),
				pStartSnapshot.getRotZ() - pInitialSnapshot.getRotZ(), pNextZ.startValue());
	}

	public void addNextRotation(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		rotation.add(pX, pY, pZ);
	}

	public void addNextPosition(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		position.add(pX, pY, pZ);
	}

	public void addNextScale(@NotNull InterpolationData pX, @NotNull InterpolationData pY, @NotNull InterpolationData pZ) {
		scale.add(pX, pY, pZ);
	}
}