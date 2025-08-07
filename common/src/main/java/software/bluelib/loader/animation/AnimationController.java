/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.*;
import java.util.function.Function;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.animation.keyframe.BoneAnimationFrame;
import software.bluelib.loader.animation.keyframe.KeyframeLocation;
import software.bluelib.loader.animation.keyframe.data.CustomInstructionKeyframeData;
import software.bluelib.loader.animation.keyframe.data.KeyFrameData;
import software.bluelib.loader.animation.keyframe.data.ParticleKeyframeData;
import software.bluelib.loader.animation.keyframe.data.SoundKeyframeData;
import software.bluelib.loader.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bluelib.loader.animation.keyframe.event.ParticleKeyframeEvent;
import software.bluelib.loader.animation.keyframe.event.SoundKeyframeEvent;
import software.bluelib.loader.animation.math.Easing;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.cache.animations.keyframe.BoneAnimationCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.loader.cache.animations.keyframe.KeyframeStackCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.geckolib.math.MathParser;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.MoLangQueries;
import software.bluelib.loader.geckolib.math.value.Constant;
import software.bluelib.loader.model.BlueModel;

public class AnimationController<T extends BlueAnimatable> {

	protected final T animatable;
	protected final String name;
	protected final AnimationStateHandler<T> stateHandler;
	protected final Map<String, BoneAnimationFrame> boneAnimationQueues = new Object2ObjectOpenHashMap<>();
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

	public final Map<String, Animation> triggerableAnimations = new Object2ObjectOpenHashMap<>(0);
	protected Animation triggeredAnimation = null;
	protected boolean handlingTriggeredAnimations = false;

	protected double transitionLength;
	protected Animation currentRawAnimation;
	protected AnimationProcessor.QueuedAnimation currentAnimation;
	public State animationState = State.STOPPED;
	protected double tickOffset;
	protected double lastPollTime = -1;
	protected Function<T, Double> animationSpeedModifier = animatable -> 1d;
	protected Function<T, Easing> overrideEasingTypeFunction = animatable -> null;
	private final Set<KeyFrameData> executedKeyFrames = new ObjectOpenHashSet<>();
	protected BlueModel<T> lastModel;

	private double lastAdjustedTick = 0;

	public AnimationController(T pAnimatable, AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, "base_controller", 0, pAnimationHandler);
	}

	public AnimationController(T pAnimatable, String pName, AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, pName, 0, pAnimationHandler);
	}

	public AnimationController(T pAnimatable, int pTransitionTickTime, AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, "base_controller", pTransitionTickTime, pAnimationHandler);
	}

	public AnimationController(T pAnimatable, String pName, int pTransitionTickTime, AnimationStateHandler<T> pAnimationHandler) {
		this.animatable = pAnimatable;
		this.name = pName;
		this.transitionLength = pTransitionTickTime;
		this.stateHandler = pAnimationHandler;
	}

	public AnimationController<T> setSoundKeyframeHandler(SoundKeyframeHandler<T> pSoundHandler) {
		this.soundKeyframeHandler = pSoundHandler;

		return this;
	}

	public AnimationController<T> setParticleKeyframeHandler(ParticleKeyframeHandler<T> pParticleHandler) {
		this.particleKeyframeHandler = pParticleHandler;

		return this;
	}

	public AnimationController<T> setCustomInstructionKeyframeHandler(CustomKeyframeHandler<T> pCustomInstructionHandler) {
		this.customKeyframeHandler = pCustomInstructionHandler;

		return this;
	}

	public AnimationController<T> setAnimationSpeedHandler(Function<T, Double> pSpeedModFunction) {
		this.animationSpeedModifier = pSpeedModFunction;

		return this;
	}

	public AnimationController<T> setAnimationSpeed(double pSpeed) {
		return setAnimationSpeedHandler(animatable -> pSpeed);
	}

	public AnimationController<T> setOverrideEasingType(Easing pEasingFunction) {
		return setOverrideEasingTypeFunction(animatable -> pEasingFunction);
	}

	public AnimationController<T> setOverrideEasingTypeFunction(Function<T, Easing> pEasingType) {
		this.overrideEasingTypeFunction = pEasingType;

		return this;
	}

	public AnimationController<T> triggerableAnim(String pName, Animation pAnimation) {
		this.triggerableAnimations.put(pName, pAnimation);

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
	public Animation getTriggeredAnimation() {
		return this.triggeredAnimation;
	}

	public State getAnimationState() {
		return this.animationState;
	}

	public Map<String, BoneAnimationFrame> getBoneAnimationQueues() {
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

	public AnimationController<T> transitionLength(int pTicks) {
		this.transitionLength = pTicks;

		return this;
	}

	public boolean hasAnimationFinished() {
		return this.currentAnimation != null && this.animationState == State.STOPPED;
	}

	public Animation getCurrentRawAnimation() {
		return this.currentRawAnimation;
	}

	public boolean isPlayingTriggeredAnimation() {
		return this.triggeredAnimation != null && !hasAnimationFinished();
	}

	public void setAnimation(Animation pAnimation) {
		if (pAnimation == null || pAnimation.getAnimationStages().isEmpty()) {
			stop();

			return;
		}

		if (this.needsAnimationReload || !pAnimation.equals(this.currentAnimation)) {
			if (this.lastModel != null) {
				Queue<AnimationProcessor.QueuedAnimation> animations = this.lastModel.getAnimationProcessor().buildAnimationQueue(this.animatable, pAnimation);

				if (animations != null) {
					this.animationQueue = animations;
					this.currentRawAnimation = pAnimation;
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

	public boolean tryTriggerAnimation(String pAnimName) {
		Animation anim = this.triggerableAnimations.get(pAnimName);

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

	public boolean stopTriggeredAnimation() {
		if (this.triggeredAnimation == null)
			return false;

		if (this.currentRawAnimation == this.triggeredAnimation) {
			this.currentRawAnimation = null;
			this.currentAnimation = null;
		}

		this.triggeredAnimation = null;
		this.needsAnimationReload = true;

		return true;
	}

	protected PlayState handleAnimationState(AnimationState<T> pState) {
		if (this.triggeredAnimation != null) {
			if (this.currentRawAnimation != this.triggeredAnimation)
				this.currentAnimation = null;

			setAnimation(this.triggeredAnimation);

			if (!hasAnimationFinished() && (!this.handlingTriggeredAnimations || this.stateHandler.handle(pState) == PlayState.PLAY))
				return PlayState.PLAY;

			this.triggeredAnimation = null;
			this.needsAnimationReload = true;
		}

		return this.stateHandler.handle(pState);
	}

	public void process(BlueModel<T> pModel, AnimationState<T> pState, Map<String, BoneCache> pBones, Map<String, BoneSnapshot> pSnapshots, final double pSeekTime, boolean pCrashWhenCantFindBone) {
		double adjustedTick = adjustTick(pSeekTime);
		this.lastModel = pModel;

		if (animationState == State.TRANSITIONING && adjustedTick >= this.transitionLength) {
			this.shouldResetTick = true;
			this.animationState = State.RUNNING;
			adjustedTick = adjustTick(pSeekTime);
		}

		PlayState playState = handleAnimationState(pState);

		if (playState == PlayState.STOP || (this.currentAnimation == null && this.animationQueue.isEmpty())) {
			this.animationState = State.STOPPED;
			this.justStopped = true;

			return;
		}

		createInitialQueues(pBones.values());

		if (this.justStartedTransition && (this.shouldResetTick || this.justStopped)) {
			this.justStopped = false;
			adjustedTick = adjustTick(pSeekTime);

			if (this.currentAnimation == null)
				this.animationState = State.TRANSITIONING;
		} else if (this.currentAnimation == null) {
			this.shouldResetTick = true;
			this.animationState = State.TRANSITIONING;
			this.justStartedTransition = true;
			this.needsAnimationReload = false;
			adjustedTick = adjustTick(pSeekTime);
		} else if (this.animationState != State.TRANSITIONING) {
			this.animationState = State.RUNNING;
		}

		if (getAnimationState() == State.RUNNING) {
			processCurrentAnimation(adjustedTick, pSeekTime, pCrashWhenCantFindBone);
		} else if (this.animationState == State.TRANSITIONING) {
			if (this.lastPollTime != pSeekTime && (adjustedTick == 0 || this.isJustStarting)) {
				this.justStartedTransition = false;
				this.lastPollTime = pSeekTime;
				this.currentAnimation = this.animationQueue.poll();

				resetEventKeyFrames();

				if (this.currentAnimation == null)
					return;

				saveSnapshotsForAnimation(this.currentAnimation, pSnapshots);
			}

			if (this.currentAnimation != null) {
				// TODO: REMOVE!!!!! 
				MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> 0);

				for (BoneAnimationCache boneAnimationCache : this.currentAnimation.animationCache().boneAnimationCaches()) {
					BoneAnimationFrame boneAnimationFrame = this.boneAnimationQueues.get(boneAnimationCache.boneName());
					BoneSnapshot boneSnapshot = this.boneSnapshots.get(boneAnimationCache.boneName());
					BoneCache bone = pBones.get(boneAnimationCache.boneName());

					if (boneSnapshot == null)
						continue;

					if (bone == null) {
						if (pCrashWhenCantFindBone)
							throw new RuntimeException("Could not find bone: " + boneAnimationCache.boneName());

						continue;
					}

					KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
					KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
					KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

					if (!rotationKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationFrame.addNextRotation(null, adjustedTick, this.transitionLength, boneSnapshot, bone.getInitialSnapshot(),
								getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), 0, true, Axis.X),
								getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), 0, true, Axis.Y),
								getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), 0, true, Axis.Z));
					}

					if (!positionKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationFrame.addNextPosition(null, adjustedTick, this.transitionLength, boneSnapshot,
								getAnimationPointAtTick(positionKeyFrames.xKeyframes(), 0, false, Axis.X),
								getAnimationPointAtTick(positionKeyFrames.yKeyframes(), 0, false, Axis.Y),
								getAnimationPointAtTick(positionKeyFrames.zKeyframes(), 0, false, Axis.Z));
					}

					if (!scaleKeyFrames.xKeyframes().isEmpty()) {
						boneAnimationFrame.addNextScale(null, adjustedTick, this.transitionLength, boneSnapshot,
								getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), 0, false, Axis.X),
								getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), 0, false, Axis.Y),
								getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), 0, false, Axis.Z));
					}
				}
			}
		}
	}

	private void processCurrentAnimation(double pAdjustedTick, double pSeekTime, boolean pCrashWhenCantFindBone) {
		if (pAdjustedTick >= this.currentAnimation.animationCache().length()) {
			if (this.currentAnimation.loopType().shouldPlayAgain(this.animatable, this, this.currentAnimation.animationCache())) {
				if (this.animationState != State.PAUSED) {
					this.shouldResetTick = true;

					pAdjustedTick = adjustTick(pSeekTime);
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
					pAdjustedTick = adjustTick(pSeekTime);
					this.currentAnimation = this.animationQueue.poll();
				}
			}
		}

		final double finalAdjustedTick = pAdjustedTick;

		// TODO: REMOVE!!!!!
		MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> finalAdjustedTick / 20d);

		for (BoneAnimationCache boneAnimationCache : this.currentAnimation.animationCache().boneAnimationCaches()) {
			BoneAnimationFrame boneAnimationFrame = this.boneAnimationQueues.get(boneAnimationCache.boneName());

			if (boneAnimationFrame == null) {
				if (pCrashWhenCantFindBone)
					throw new RuntimeException("Could not find bone: " + boneAnimationCache.boneName());

				continue;
			}

			KeyframeStackCache<KeyframeCache<MathValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
			KeyframeStackCache<KeyframeCache<MathValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
			KeyframeStackCache<KeyframeCache<MathValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

			if (!rotationKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationFrame.addRotations(
						getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), pAdjustedTick, true, Axis.X),
						getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), pAdjustedTick, true, Axis.Y),
						getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), pAdjustedTick, true, Axis.Z));
			}

			if (!positionKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationFrame.addPositions(
						getAnimationPointAtTick(positionKeyFrames.xKeyframes(), pAdjustedTick, false, Axis.X),
						getAnimationPointAtTick(positionKeyFrames.yKeyframes(), pAdjustedTick, false, Axis.Y),
						getAnimationPointAtTick(positionKeyFrames.zKeyframes(), pAdjustedTick, false, Axis.Z));
			}

			if (!scaleKeyFrames.xKeyframes().isEmpty()) {
				boneAnimationFrame.addScales(
						getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), pAdjustedTick, false, Axis.X),
						getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), pAdjustedTick, false, Axis.Y),
						getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), pAdjustedTick, false, Axis.Z));
			}
		}

		pAdjustedTick += this.transitionLength;

		for (SoundKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().sounds()) {
			if (pAdjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.soundKeyframeHandler == null) {
					BaseLogger.log(BaseLogLevel.WARNING, "Sound Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");
					break;
				}

				this.soundKeyframeHandler.handle(new SoundKeyframeEvent<>(this.animatable, pAdjustedTick, this, keyframeData));
			}
		}

		for (ParticleKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().particles()) {
			if (pAdjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.particleKeyframeHandler == null) {
					BaseLogger.log(BaseLogLevel.WARNING, "Particle Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");
					break;
				}

				this.particleKeyframeHandler.handle(new ParticleKeyframeEvent<>(this.animatable, pAdjustedTick, this, keyframeData));
			}
		}

		for (CustomInstructionKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().customInstructions()) {
			if (pAdjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
				if (this.customKeyframeHandler == null) {
					BaseLogger.log(BaseLogLevel.WARNING, "Custom Instruction Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + getName() + ", but no keyframe handler registered");
					break;
				}

				this.customKeyframeHandler.handle(new CustomInstructionKeyframeEvent<>(this.animatable, pAdjustedTick, this, keyframeData));
			}
		}

		if (this.transitionLength == 0 && this.shouldResetTick && this.animationState == State.TRANSITIONING)
			this.currentAnimation = this.animationQueue.poll();
	}

	public double getAnimTime() {
		return lastAdjustedTick / 20d;
	}

	private void createInitialQueues(Collection<BoneCache> pModelRendererList) {
		this.boneAnimationQueues.clear();

		for (BoneCache modelRenderer : pModelRendererList) {
			this.boneAnimationQueues.put(modelRenderer.getName(), new BoneAnimationFrame(modelRenderer));
		}
	}

	private void saveSnapshotsForAnimation(AnimationProcessor.QueuedAnimation pAnimation, Map<String, BoneSnapshot> pSnapshots) {
		for (BoneSnapshot snapshot : pSnapshots.values()) {
			if (pAnimation.animationCache().boneAnimationCaches() != null) {
				for (BoneAnimationCache boneAnimationCache : pAnimation.animationCache().boneAnimationCaches()) {
					if (boneAnimationCache.boneName().equals(snapshot.getBone().getName())) {
						this.boneSnapshots.put(boneAnimationCache.boneName(), BoneSnapshot.copy(snapshot));

						break;
					}
				}
			}
		}
	}

	protected double adjustTick(double pTick) {
		if (!this.shouldResetTick)
			return this.animationSpeedModifier.apply(this.animatable) * Math.max(pTick - this.tickOffset, 0);

		if (getAnimationState() != State.STOPPED)
			this.tickOffset = pTick;

		this.shouldResetTick = false;

		return 0;
	}

	private AnimationPoint getAnimationPointAtTick(List<KeyframeCache<MathValue>> pFrames, double pTick, boolean pIsRotation,
			Axis pAxis) {
		KeyframeLocation<KeyframeCache<MathValue>> location = getCurrentKeyFrameLocation(pFrames, pTick);
		KeyframeCache<MathValue> currentFrame = location.keyframe();
		double startValue = currentFrame.startValue().get();
		double endValue = currentFrame.endValue().get();

		if (pIsRotation) {
			if (!(currentFrame.startValue() instanceof Constant)) {
				startValue = Math.toRadians(startValue);

				if (pAxis == Axis.X || pAxis == Axis.Y)
					startValue *= -1;
			}

			if (!(currentFrame.endValue() instanceof Constant)) {
				endValue = Math.toRadians(endValue);

				if (pAxis == Axis.X || pAxis == Axis.Y)
					endValue *= -1;
			}
		}

		return new AnimationPoint(currentFrame, location.startTick(), currentFrame.length(), startValue, endValue);
	}

	private KeyframeLocation<KeyframeCache<MathValue>> getCurrentKeyFrameLocation(List<KeyframeCache<MathValue>> pFrames,
			double pAgeInTicks) {
		double totalFrameTime = 0;

		for (KeyframeCache<MathValue> frame : pFrames) {
			totalFrameTime += frame.length();

			if (totalFrameTime > pAgeInTicks)
				return new KeyframeLocation<>(frame, (pAgeInTicks - (totalFrameTime - frame.length())));
		}

		return new KeyframeLocation<>(pFrames.getLast(), pAgeInTicks);
	}

	private void resetEventKeyFrames() {
		this.executedKeyFrames.clear();
	}

	@FunctionalInterface
	public interface AnimationStateHandler<A extends BlueAnimatable> {

		PlayState handle(AnimationState<A> pState);
	}

	@FunctionalInterface
	public interface SoundKeyframeHandler<A extends BlueAnimatable> {

		void handle(SoundKeyframeEvent<A> pEvent);
	}

	@FunctionalInterface
	public interface ParticleKeyframeHandler<A extends BlueAnimatable> {

		void handle(ParticleKeyframeEvent<A> pEvent);
	}

	@FunctionalInterface
	public interface CustomKeyframeHandler<A extends BlueAnimatable> {

		void handle(CustomInstructionKeyframeEvent<A> pEvent);
	}

	public enum State {
		RUNNING,
		TRANSITIONING,
		PAUSED,
		STOPPED
	}
}
