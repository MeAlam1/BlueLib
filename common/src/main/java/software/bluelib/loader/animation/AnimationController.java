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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.animation.keyframe.AnimationPoint;
import software.bluelib.loader.animation.keyframe.BoneAnimationFrame;
import software.bluelib.loader.animation.keyframe.KeyframeLocation;
import software.bluelib.loader.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bluelib.loader.animation.keyframe.event.ParticleKeyframeEvent;
import software.bluelib.loader.animation.keyframe.event.SoundKeyframeEvent;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.geckolib.animations.BoneAnimationCache;
import software.bluelib.loader.geckolib.animations.Easing;
import software.bluelib.loader.geckolib.animations.KeyFrameData;
import software.bluelib.loader.geckolib.animations.KeyframeCache;
import software.bluelib.loader.geckolib.animations.KeyframeStackCache;
import software.bluelib.loader.geckolib.math.MathParser;
import software.bluelib.loader.geckolib.math.MoLangQueries;
import software.bluelib.loader.model.BlueModel;

public class AnimationController<T extends BlueAnimatable> {

	@NotNull
	protected final T animatable;
	@NotNull
	protected final String name;
	@NotNull
	protected final AnimationStateHandler<T> stateHandler;
	@NotNull
	protected final Map<String, BoneAnimationFrame> boneAnimationQueues = new Object2ObjectOpenHashMap<>();
	@NotNull
	protected final Map<String, BoneSnapshot> boneSnapshots = new Object2ObjectOpenHashMap<>();
	@NotNull
	protected Queue<AnimationProcessor.QueuedAnimation> animationQueue = new LinkedList<>();

	protected boolean isJustStarting = false;
	protected boolean needsAnimationReload = false;
	protected boolean shouldResetTick = false;
	private boolean justStopped = true;
	protected boolean justStartedTransition = false;

	@Nullable
	protected SoundKeyframeHandler<T> soundKeyframeHandler = null;
	@Nullable
	protected ParticleKeyframeHandler<T> particleKeyframeHandler = null;
	@Nullable
	protected CustomKeyframeHandler<T> customKeyframeHandler = null;

	@NotNull
	public final Map<String, Animation> triggerableAnimations = new Object2ObjectOpenHashMap<>(0);
	@Nullable
	protected Animation triggeredAnimation = null;
	protected boolean handlingTriggeredAnimations = false;

	protected double transitionLength;
	@Nullable
	protected Animation currentRawAnimation;
	@Nullable
	protected AnimationProcessor.QueuedAnimation currentAnimation;
	@NotNull
	public State animationState = State.STOPPED;
	protected double tickOffset;
	protected double lastPollTime = -1;
	@NotNull
	protected Function<T, Double> animationSpeedModifier = animatable -> 1d;
	@NotNull
	protected Function<T, Easing> overrideEasingTypeFunction = animatable -> null;
	@NotNull
	private final Set<KeyFrameData> executedKeyFrames = new ObjectOpenHashSet<>();
	@Nullable
	protected BlueModel<T> lastModel;

	private double lastAdjustedTick = 0;

	public AnimationController(@NotNull T pAnimatable, @NotNull AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, "base_controller", 0, pAnimationHandler);
	}

	public AnimationController(@NotNull T pAnimatable, @NotNull String pName, @NotNull AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, pName, 0, pAnimationHandler);
	}

	public AnimationController(@NotNull T pAnimatable, int pTransitionTickTime, @NotNull AnimationStateHandler<T> pAnimationHandler) {
		this(pAnimatable, "base_controller", pTransitionTickTime, pAnimationHandler);
	}

	public AnimationController(@NotNull T pAnimatable, @NotNull String pName, int pTransitionTickTime, @NotNull AnimationStateHandler<T> pAnimationHandler) {
		this.animatable = pAnimatable;
		this.name = pName;
		this.transitionLength = pTransitionTickTime;
		this.stateHandler = pAnimationHandler;
	}

	@NotNull
	public AnimationController<T> setSoundKeyframeHandler(@NotNull SoundKeyframeHandler<T> pSoundHandler) {
		this.soundKeyframeHandler = pSoundHandler;

		return this;
	}

	@NotNull
	public AnimationController<T> setParticleKeyframeHandler(@NotNull ParticleKeyframeHandler<T> pParticleHandler) {
		this.particleKeyframeHandler = pParticleHandler;

		return this;
	}

	@NotNull
	public AnimationController<T> setCustomInstructionKeyframeHandler(@NotNull CustomKeyframeHandler<T> pCustomInstructionHandler) {
		this.customKeyframeHandler = pCustomInstructionHandler;

		return this;
	}

	@NotNull
	public AnimationController<T> setAnimationSpeedHandler(@NotNull Function<T, Double> pSpeedModFunction) {
		this.animationSpeedModifier = pSpeedModFunction;

		return this;
	}

	@NotNull
	public AnimationController<T> setAnimationSpeed(@NotNull Double pSpeed) {
		return setAnimationSpeedHandler(animatable -> pSpeed);
	}

	@NotNull
	public AnimationController<T> setOverrideEasingType(@NotNull Easing pEasingFunction) {
		return setOverrideEasingTypeFunction(animatable -> pEasingFunction);
	}

	@NotNull
	public AnimationController<T> setOverrideEasingTypeFunction(@NotNull Function<T, Easing> pEasingType) {
		this.overrideEasingTypeFunction = pEasingType;

		return this;
	}

	@NotNull
	public AnimationController<T> triggerableAnim(@NotNull String pName, @NotNull Animation pAnimation) {
		this.triggerableAnimations.put(pName, pAnimation);

		return this;
	}

	@NotNull
	public AnimationController<T> receiveTriggeredAnimations() {
		this.handlingTriggeredAnimations = true;

		return this;
	}

	@NotNull
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

	@NotNull
	public State getAnimationState() {
		return this.animationState;
	}

	@NotNull
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

	@NotNull
	public AnimationController<T> transitionLength(int pTicks) {
		this.transitionLength = pTicks;

		return this;
	}

	public boolean hasAnimationFinished() {
		return this.currentRawAnimation != null && this.animationState == State.STOPPED;
	}

	@NotNull
	public Animation getCurrentRawAnimation() {
		return this.currentRawAnimation;
	}

	public boolean isPlayingTriggeredAnimation() {
		return this.triggeredAnimation != null && !hasAnimationFinished();
	}

	public void setAnimation(@NotNull Animation pAnimation) {
		if (pAnimation.getAnimationStages().isEmpty()) {
			stop();

			return;
		}

		if (this.needsAnimationReload || !pAnimation.equals(this.currentRawAnimation)) {
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

	public boolean tryTriggerAnimation(@NotNull String pAnimName) {
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
			this.currentAnimation = null;
			this.currentRawAnimation = null;
		}

		this.triggeredAnimation = null;
		this.needsAnimationReload = true;

		return true;
	}

	@NotNull
	protected PlayState handleAnimationState(@NotNull AnimationState<T> pState) {
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

	public void process(@NotNull BlueModel<T> pModel, @NotNull AnimationState<T> pState, @NotNull Map<String, BoneCache> pBones, @NotNull Map<String, BoneSnapshot> pSnapshots, final double pSeekTime, boolean pCrashWhenCantFindBone) {
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
				/* TODO: REMOVE!!!!! 
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

					KeyframeStackCache<KeyframeCache<MoLangValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
					KeyframeStackCache<KeyframeCache<MoLangValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
					KeyframeStackCache<KeyframeCache<MoLangValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

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
				}*/
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

		/* TODO: REMOVE!!!!!
		MathParser.setVariable(MoLangQueries.ANIM_TIME, () -> finalAdjustedTick / 20d);

		for (BoneAnimationCache boneAnimationCache : this.currentAnimation.animationCache().boneAnimationCaches()) {
			BoneAnimationFrame boneAnimationFrame = this.boneAnimationQueues.get(boneAnimationCache.boneName());

			if (boneAnimationFrame == null) {
				if (pCrashWhenCantFindBone)
					throw new RuntimeException("Could not find bone: " + boneAnimationCache.boneName());

				continue;
			}

			KeyframeStackCache<KeyframeCache<MoLangValue>> rotationKeyFrames = boneAnimationCache.rotationKeyFrames();
			KeyframeStackCache<KeyframeCache<MoLangValue>> positionKeyFrames = boneAnimationCache.positionKeyFrames();
			KeyframeStackCache<KeyframeCache<MoLangValue>> scaleKeyFrames = boneAnimationCache.scaleKeyFrames();

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
		}*/

		pAdjustedTick += this.transitionLength;

		/*for (SoundKeyframeData keyframeData : this.currentAnimation.animationCache().keyFrames().sounds()) {
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
		}*/

		if (this.transitionLength == 0 && this.shouldResetTick && this.animationState == State.TRANSITIONING)
			this.currentAnimation = this.animationQueue.poll();
	}

	public double getAnimTime() {
		return lastAdjustedTick / 20d;
	}

	private void createInitialQueues(@NotNull Collection<BoneCache> pModelRendererList) {
		this.boneAnimationQueues.clear();

		for (BoneCache modelRenderer : pModelRendererList) {
			this.boneAnimationQueues.put(modelRenderer.getName(), new BoneAnimationFrame(modelRenderer));
		}
	}

	private void saveSnapshotsForAnimation(@NotNull AnimationProcessor.QueuedAnimation pAnimation, @NotNull Map<String, BoneSnapshot> pSnapshots) {
		for (BoneSnapshot snapshot : pSnapshots.values()) {
			/*if (pAnimation.animationCache().boneAnimationCaches() != null) {
				for (BoneAnimationCache boneAnimationCache : pAnimation.animationCache().boneAnimationCaches()) {
					if (boneAnimationCache.boneName().equals(snapshot.getBone().getName())) {
						this.boneSnapshots.put(boneAnimationCache.boneName(), BoneSnapshot.copy(snapshot));

						break;
					}
				}
			}*/
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

	@NotNull
	private AnimationPoint getAnimationPointAtTick(@NotNull List<KeyframeCache<MoLangValue>> pFrames, double pTick, boolean pIsRotation,
			@NotNull Axis pAxis) {
		KeyframeLocation<KeyframeCache<MoLangValue>> location = getCurrentKeyFrameLocation(pFrames, pTick);
		KeyframeCache<MoLangValue> currentFrame = location.keyframe();
		double startValue = 1;
		double endValue = 1;

		/*if (pIsRotation) {
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
		}*/

		return new AnimationPoint(currentFrame, location.startTick(), currentFrame.length(), startValue, endValue);
	}

	@NotNull
	private KeyframeLocation<KeyframeCache<MoLangValue>> getCurrentKeyFrameLocation(@NotNull List<KeyframeCache<MoLangValue>> pFrames,
			double pAgeInTicks) {
		double totalFrameTime = 0;

		for (KeyframeCache<MoLangValue> frame : pFrames) {
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

		@NotNull
		PlayState handle(@NotNull AnimationState<A> pState);
	}

	@FunctionalInterface
	public interface SoundKeyframeHandler<A extends BlueAnimatable> {

		void handle(@NotNull SoundKeyframeEvent<A> pEvent);
	}

	@FunctionalInterface
	public interface ParticleKeyframeHandler<A extends BlueAnimatable> {

		void handle(@NotNull ParticleKeyframeEvent<A> pEvent);
	}

	@FunctionalInterface
	public interface CustomKeyframeHandler<A extends BlueAnimatable> {

		void handle(@NotNull CustomInstructionKeyframeEvent<A> pEvent);
	}

	public enum State {
		RUNNING,
		TRANSITIONING,
		PAUSED,
		STOPPED
	}
}
