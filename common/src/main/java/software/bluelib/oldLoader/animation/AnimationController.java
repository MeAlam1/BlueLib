/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.*;
import java.util.function.Function;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.animations.keyframe.BoneAnimationCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeStackCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.keyframe.AnimationPoint;
import software.bluelib.oldLoader.animation.keyframe.BoneAnimationQueue;
import software.bluelib.oldLoader.animation.keyframe.KeyframeLocation;
import software.bluelib.oldLoader.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bluelib.oldLoader.animation.keyframe.event.ParticleKeyframeEvent;
import software.bluelib.oldLoader.animation.keyframe.event.SoundKeyframeEvent;
import software.bluelib.oldLoader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.KeyFrameData;
import software.bluelib.oldLoader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.SoundKeyframeData;
import software.bluelib.oldLoader.animation.state.BoneSnapshot;
import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.value.Constant;

public class AnimationController<T extends BlueAnimatable> {

	protected final T animatable;
	protected final String name;
	protected final AnimationStateHandler<T> stateHandler;
	protected final Map<String, BoneAnimationQueue> boneAnimationQueues = new Object2ObjectOpenHashMap<>();
	protected final Map<String, BoneSnapshot> boneSnapshots = new Object2ObjectOpenHashMap<>();
	protected Queue<AnimationProcessor.QueuedAnimation> animationQueue = new LinkedList<>();

	protected boolean isJustStarting = false;
	protected boolean needsAnimationReload = false;
	protected boolean shouldResetTick = false;
	private boolean justStopped = true;
	protected boolean justStartedTransition = false;

	protected SoundKeyframeHandler<T> soundKeyframeHandler = null;
	protected ParticleKeyframeHandler<T> particleKeyframeHandler = null;
	protected CustomKeyframeHandler<T> customKeyframeHandler = null;

	protected final Map<String, RawAnimation> triggerableAnimations = new Object2ObjectOpenHashMap<>(0);
	protected RawAnimation triggeredAnimation = null;
	protected boolean handlingTriggeredAnimations = false;

	protected double transitionLength;
	protected RawAnimation currentRawAnimation;
	protected AnimationProcessor.QueuedAnimation currentAnimation;
	public State animationState = State.STOPPED;
	protected double tickOffset;
	protected double lastPollTime = -1;
	protected Function<T, Double> animationSpeedModifier = animatable -> 1d;
	protected Function<T, EasingType> overrideEasingTypeFunction = animatable -> null;
	private final Set<KeyFrameData> executedKeyFrames = new ObjectOpenHashSet<>();
	protected BlueModel<T> lastModel;

	private double lastAdjustedTick = 0;

	public AnimationController(T animatable, AnimationStateHandler<T> animationHandler) {
		this(animatable, "base_controller", 0, animationHandler);
	}

	public AnimationController(T animatable, String name, AnimationStateHandler<T> animationHandler) {
		this(animatable, name, 0, animationHandler);
	}

	public AnimationController(T animatable, int transitionTickTime, AnimationStateHandler<T> animationHandler) {
		this(animatable, "base_controller", transitionTickTime, animationHandler);
	}

	public AnimationController(T animatable, String name, int transitionTickTime, AnimationStateHandler<T> animationHandler) {
		this.animatable = animatable;
		this.name = name;
		this.transitionLength = transitionTickTime;
		this.stateHandler = animationHandler;
	}

	public AnimationController<T> setSoundKeyframeHandler(SoundKeyframeHandler<T> soundHandler) {
		this.soundKeyframeHandler = soundHandler;

		return this;
	}

	public AnimationController<T> setParticleKeyframeHandler(ParticleKeyframeHandler<T> particleHandler) {
		this.particleKeyframeHandler = particleHandler;

		return this;
	}

	public AnimationController<T> setCustomInstructionKeyframeHandler(CustomKeyframeHandler<T> customInstructionHandler) {
		this.customKeyframeHandler = customInstructionHandler;

		return this;
	}

	public AnimationController<T> setAnimationSpeedHandler(Function<T, Double> speedModFunction) {
		this.animationSpeedModifier = speedModFunction;

		return this;
	}

	public AnimationController<T> setAnimationSpeed(double speed) {
		return setAnimationSpeedHandler(animatable -> speed);
	}

	public AnimationController<T> setOverrideEasingType(EasingType easingTypeFunction) {
		return setOverrideEasingTypeFunction(animatable -> easingTypeFunction);
	}

	public AnimationController<T> setOverrideEasingTypeFunction(Function<T, EasingType> easingType) {
		this.overrideEasingTypeFunction = easingType;

		return this;
	}

	public AnimationController<T> triggerableAnim(String name, RawAnimation animation) {
		this.triggerableAnimations.put(name, animation);

		return this;
	}

	public AnimationController<T> receiveTriggeredAnimations() {
		this.handlingTriggeredAnimations = true;

		return this;
	}

	public String getName() {
		return this.name;
	}

	@Nullable
	public AnimationProcessor.QueuedAnimation getCurrentAnimation() {
		return this.currentAnimation;
	}

	@Nullable
	public RawAnimation getTriggeredAnimation() {
		return this.triggeredAnimation;
	}

	public State getAnimationState() {
		return this.animationState;
	}

	public Map<String, BoneAnimationQueue> getBoneAnimationQueues() {
		return this.boneAnimationQueues;
	}

	public double getAnimationSpeed() {
		return this.animationSpeedModifier.apply(this.animatable);
	}

	public void forceAnimationReset() {
		this.needsAnimationReload = true;
	}

	public void stop() {
		this.animationState = State.STOPPED;
	}

	public AnimationController<T> transitionLength(int ticks) {
		this.transitionLength = ticks;

		return this;
	}

	public boolean hasAnimationFinished() {
		return this.currentRawAnimation != null && this.animationState == State.STOPPED;
	}

	public RawAnimation getCurrentRawAnimation() {
		return this.currentRawAnimation;
	}

	public boolean isPlayingTriggeredAnimation() {
		return this.triggeredAnimation != null && !hasAnimationFinished();
	}

	public void setAnimation(RawAnimation rawAnimation) {
		if (rawAnimation == null || rawAnimation.getAnimationStages().isEmpty()) {
			stop();

			return;
		}

		if (this.needsAnimationReload || !rawAnimation.equals(this.currentRawAnimation)) {
			if (this.lastModel != null) {
				Queue<AnimationProcessor.QueuedAnimation> animations = this.lastModel.getAnimationProcessor().buildAnimationQueue(this.animatable, rawAnimation);

				if (animations != null) {
					this.animationQueue = animations;
					this.currentRawAnimation = rawAnimation;
					this.shouldResetTick = true;
					this.animationState = State.TRANSITIONING;
					this.justStartedTransition = true;
					this.needsAnimationReload = false;

					return;
				}
			}

			stop();
		}
	}

	public boolean tryTriggerAnimation(String animName) {
		RawAnimation anim = this.triggerableAnimations.get(animName);

		if (anim == null)
			return false;

		this.triggeredAnimation = anim;

		if (this.animationState == State.STOPPED) {
			this.animationState = State.TRANSITIONING;
			this.shouldResetTick = true;
			this.justStartedTransition = true;
		}

		return true;
	}

	protected boolean stopTriggeredAnimation() {
		if (this.triggeredAnimation == null)
			return false;

		if (this.currentRawAnimation == this.triggeredAnimation) {
			this.currentAnimation = null;
			this.currentRawAnimation = null;
		}

		this.triggeredAnimation = null;
		this.needsAnimationReload = true;

		return true;
	}

	protected PlayState handleAnimationState(AnimationState<T> state) {
		if (this.triggeredAnimation != null) {
			if (this.currentRawAnimation != this.triggeredAnimation)
				this.currentAnimation = null;

			setAnimation(this.triggeredAnimation);

			if (!hasAnimationFinished() && (!this.handlingTriggeredAnimations || this.stateHandler.handle(state) == PlayState.CONTINUE))
				return PlayState.CONTINUE;

			this.triggeredAnimation = null;
			this.needsAnimationReload = true;
		}

		return this.stateHandler.handle(state);
	}

	public void process(BlueModel<T> model, AnimationState<T> state, Map<String, BoneCache> bones, Map<String, BoneSnapshot> snapshots, final double seekTime, boolean crashWhenCantFindBone) {
		double adjustedTick = adjustTick(seekTime);
		this.lastModel = model;

		if (animationState == State.TRANSITIONING && adjustedTick >= this.transitionLength) {
			this.shouldResetTick = true;
			this.animationState = State.RUNNING;
			adjustedTick = adjustTick(seekTime);
		}

		PlayState playState = handleAnimationState(state);

		if (playState == PlayState.STOP || (this.currentAnimation == null && this.animationQueue.isEmpty())) {
			this.animationState = State.STOPPED;
			this.justStopped = true;

			return;
		}

		createInitialQueues(bones.values());

		if (this.justStartedTransition && (this.shouldResetTick || this.justStopped)) {
			this.justStopped = false;
			adjustedTick = adjustTick(seekTime);

			if (this.currentAnimation == null)
				this.animationState = State.TRANSITIONING;
		} else if (this.currentAnimation == null) {
			this.shouldResetTick = true;
			this.animationState = State.TRANSITIONING;
			this.justStartedTransition = true;
			this.needsAnimationReload = false;
			adjustedTick = adjustTick(seekTime);
		} else if (this.animationState != State.TRANSITIONING) {
			this.animationState = State.RUNNING;
		}

		if (getAnimationState() == State.RUNNING) {
			processCurrentAnimation(adjustedTick, seekTime, crashWhenCantFindBone);
		} else if (this.animationState == State.TRANSITIONING) {
			if (this.lastPollTime != seekTime && (adjustedTick == 0 || this.isJustStarting)) {
				this.justStartedTransition = false;
				this.lastPollTime = seekTime;
				this.currentAnimation = this.animationQueue.poll();

				resetEventKeyFrames();

				if (this.currentAnimation == null)
					return;

				saveSnapshotsForAnimation(this.currentAnimation, snapshots);
			}

			if (this.currentAnimation != null) {

				for (BoneAnimationCache boneAnimationCache : this.currentAnimation.animationCache().boneAnimationCaches()) {
					BoneAnimationQueue boneAnimationQueue = this.boneAnimationQueues.get(boneAnimationCache.boneName());
					BoneSnapshot boneSnapshot = this.boneSnapshots.get(boneAnimationCache.boneName());
					BoneCache bone = bones.get(boneAnimationCache.boneName());

					if (boneSnapshot == null)
						continue;

					if (bone == null) {
						if (crashWhenCantFindBone)
							throw new RuntimeException("Could not find bone: " + boneAnimationCache.boneName());

						continue;
					}

					KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
					KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
					KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

					if (!rotationKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationQueue.addNextRotation(null, adjustedTick, this.transitionLength, boneSnapshot, bone.getInitialSnapshot(),
								getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), 0, true, Axis.X),
								getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), 0, true, Axis.Y),
								getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), 0, true, Axis.Z));
					}

					if (!positionKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationQueue.addNextPosition(null, adjustedTick, this.transitionLength, boneSnapshot,
								getAnimationPointAtTick(positionKeyFrames.xKeyframes(), 0, false, Axis.X),
								getAnimationPointAtTick(positionKeyFrames.yKeyframes(), 0, false, Axis.Y),
								getAnimationPointAtTick(positionKeyFrames.zKeyframes(), 0, false, Axis.Z));
					}

					if (!scaleKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationQueue.addNextScale(null, adjustedTick, this.transitionLength, boneSnapshot,
								getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), 0, false, Axis.X),
								getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), 0, false, Axis.Y),
								getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), 0, false, Axis.Z));
					}
				}
			}
		}
	}

	private void processCurrentAnimation(double adjustedTick, double seekTime, boolean crashWhenCantFindBone) {
		if (adjustedTick >= this.currentAnimation.animationCache().length()) {
			if (this.currentAnimation.loopType().shouldPlayAgain(this.animatable, this, this.currentAnimation.animationCache())) {
				if (this.animationState != State.PAUSED) {
					this.shouldResetTick = true;

					adjustedTick = adjustTick(seekTime);
					resetEventKeyFrames();
				}
			} else {
				AnimationProcessor.QueuedAnimation nextAnimation = this.animationQueue.peek();

				resetEventKeyFrames();

				if (nextAnimation == null) {
					this.animationState = State.STOPPED;

					return;
				} else {
					this.animationState = State.TRANSITIONING;
					this.shouldResetTick = true;
					adjustedTick = adjustTick(seekTime);
					this.currentAnimation = this.animationQueue.poll();
				}
			}
		}

		this.lastAdjustedTick = adjustedTick;

		//TODO: System.out.println("BLUELIB: " + MoLang.animatableMoLang("q.anim_time"));

		for (BoneAnimationCache boneAnimationCache : this.currentAnimation.animationCache().boneAnimationCaches()) {
			BoneAnimationQueue boneAnimationQueue = this.boneAnimationQueues.get(boneAnimationCache.boneName());

			if (boneAnimationQueue == null) {
				if (crashWhenCantFindBone)
					throw new RuntimeException("Could not find bone: " + boneAnimationCache.boneName());

				continue;
			}

			KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
			KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
			KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

			if (!rotationKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationQueue.addRotations(
						getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), adjustedTick, true, Axis.X),
						getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), adjustedTick, true, Axis.Y),
						getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), adjustedTick, true, Axis.Z));
			}

			if (!positionKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationQueue.addPositions(
						getAnimationPointAtTick(positionKeyFrames.xKeyframes(), adjustedTick, false, Axis.X),
						getAnimationPointAtTick(positionKeyFrames.yKeyframes(), adjustedTick, false, Axis.Y),
						getAnimationPointAtTick(positionKeyFrames.zKeyframes(), adjustedTick, false, Axis.Z));
			}

			if (!scaleKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationQueue.addScales(
						getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), adjustedTick, false, Axis.X),
						getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), adjustedTick, false, Axis.Y),
						getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), adjustedTick, false, Axis.Z));
			}
		}

		adjustedTick += this.transitionLength;

		for (SoundKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().sounds()) {
			if (adjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.soundKeyframeHandler == null) {
					//BlueLibConstants.LOGGER.log(Level.WARN, "Sound Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");

					break;
				}

				this.soundKeyframeHandler.handle(new SoundKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeData));
			}
		}

		for (ParticleKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().particles()) {
			if (adjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.particleKeyframeHandler == null) {
					//BlueLibConstants.LOGGER.log(Level.WARN, "Particle Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");

					break;
				}

				this.particleKeyframeHandler.handle(new ParticleKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeData));
			}
		}

		for (CustomInstructionKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().customInstructions()) {
			if (adjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.customKeyframeHandler == null) {
					//BlueLibConstants.LOGGER.log(Level.WARN, "Custom Instruction Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");

					break;
				}

				this.customKeyframeHandler.handle(new CustomInstructionKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeData));
			}
		}

		if (this.transitionLength == 0 && this.shouldResetTick && this.animationState == State.TRANSITIONING)
			this.currentAnimation = this.animationQueue.poll();
	}

	public double getAnimTime() {
		return lastAdjustedTick / 20d;
	}

	private void createInitialQueues(Collection<BoneCache> modelRendererList) {
		this.boneAnimationQueues.clear();

		for (BoneCache modelRenderer : modelRendererList) {
			this.boneAnimationQueues.put(modelRenderer.getName(), new BoneAnimationQueue(modelRenderer));
		}
	}

	private void saveSnapshotsForAnimation(AnimationProcessor.QueuedAnimation animation, Map<String, BoneSnapshot> snapshots) {
		for (BoneSnapshot snapshot : snapshots.values()) {
			if (animation.animationCache().boneAnimationCaches() != null) {
				for (BoneAnimationCache boneAnimationCache : animation.animationCache().boneAnimationCaches()) {
					if (boneAnimationCache.boneName().equals(snapshot.getBone().getName())) {
						this.boneSnapshots.put(boneAnimationCache.boneName(), BoneSnapshot.copy(snapshot));

						break;
					}
				}
			}
		}
	}

	protected double adjustTick(double tick) {
		if (!this.shouldResetTick)
			return this.animationSpeedModifier.apply(this.animatable) * Math.max(tick - this.tickOffset, 0);

		if (getAnimationState() != State.STOPPED)
			this.tickOffset = tick;

		this.shouldResetTick = false;

		return 0;
	}

	private AnimationPoint getAnimationPointAtTick(List<KeyframeCache<MathValue>> frames, double tick, boolean isRotation,
			Axis axis) {
		KeyframeLocation<KeyframeCache<MathValue>> location = getCurrentKeyFrameLocation(frames, tick);
		KeyframeCache<MathValue> currentFrame = location.keyframe();
		double startValue = currentFrame.startValue().get();
		double endValue = currentFrame.endValue().get();

		if (isRotation) {
			if (!(currentFrame.startValue() instanceof Constant)) {
				startValue = Math.toRadians(startValue);

				if (axis == Axis.X || axis == Axis.Y)
					startValue *= -1;
			}

			if (!(currentFrame.endValue() instanceof Constant)) {
				endValue = Math.toRadians(endValue);

				if (axis == Axis.X || axis == Axis.Y)
					endValue *= -1;
			}
		}

		return new AnimationPoint(currentFrame, location.startTick(), currentFrame.length(), startValue, endValue);
	}

	private KeyframeLocation<KeyframeCache<MathValue>> getCurrentKeyFrameLocation(List<KeyframeCache<MathValue>> frames,
			double ageInTicks) {
		double totalFrameTime = 0;

		for (KeyframeCache<MathValue> frame : frames) {
			totalFrameTime += frame.length();

			if (totalFrameTime > ageInTicks)
				return new KeyframeLocation<>(frame, (ageInTicks - (totalFrameTime - frame.length())));
		}

		return new KeyframeLocation<>(frames.getLast(), ageInTicks);
	}

	private void resetEventKeyFrames() {
		this.executedKeyFrames.clear();
	}

	@FunctionalInterface
	public interface AnimationStateHandler<A extends BlueAnimatable> {

		PlayState handle(AnimationState<A> state);
	}

	@FunctionalInterface
	public interface SoundKeyframeHandler<A extends BlueAnimatable> {

		void handle(SoundKeyframeEvent<A> event);
	}

	@FunctionalInterface
	public interface ParticleKeyframeHandler<A extends BlueAnimatable> {

		void handle(ParticleKeyframeEvent<A> event);
	}

	@FunctionalInterface
	public interface CustomKeyframeHandler<A extends BlueAnimatable> {

		void handle(CustomInstructionKeyframeEvent<A> event);
	}

	public enum State {
		RUNNING,
		TRANSITIONING,
		PAUSED,
		STOPPED
	}
}
