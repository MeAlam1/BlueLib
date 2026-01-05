/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.animations.AnimationCache;

public final class Animation {

	@NotNull
	private final List<Frame> animationList = new ObjectArrayList<>();

	private Animation() {
	}

	@NotNull
	public static Animation begin() {
		return new Animation();
	}

	@NotNull
	public Animation thenPlay(@NotNull String pAnimationName) {
		return then(pAnimationName, AnimationCache.LoopType.DEFAULT);
	}

	@NotNull
	public Animation thenLoop(@NotNull String pAnimationName) {
		return then(pAnimationName, AnimationCache.LoopType.LOOP);
	}

	@NotNull
	public Animation thenWait(int pTicks) {
		if (pTicks < 0) {
			throw new IllegalArgumentException("Ticks cannot be negative");
		}
		this.animationList.add(new Frame(Frame.WAIT, AnimationCache.LoopType.PLAY_ONCE, pTicks));
		return this;
	}

	@NotNull
	public Animation thenPlayAndHold(@NotNull String pAnimationName) {
		return then(pAnimationName, AnimationCache.LoopType.HOLD_ON_LAST_FRAME);
	}

	@NotNull
	public Animation thenPlayXTimes(@NotNull String pAnimationName, int pPlayCount) {
		if (pPlayCount <= 0) {
			throw new IllegalArgumentException("Play count must be positive");
		}
		for (int i = 0; i < pPlayCount; i++) {
			then(pAnimationName, i == pPlayCount - 1 ? AnimationCache.LoopType.DEFAULT : AnimationCache.LoopType.PLAY_ONCE);
		}
		return this;
	}

	@NotNull
	public Animation then(@NotNull String pAnimationName, @NotNull AnimationCache.LoopType pLoopType) {
		this.animationList.add(new Frame(pAnimationName, pLoopType));
		return this;
	}

	@NotNull
	public List<Frame> getAnimationFrames() {
		return Collections.unmodifiableList(this.animationList);
	}

	@NotNull
	public static Animation copyOf(@NotNull Animation pOther) {
		Animation newInstance = Animation.begin();
		newInstance.animationList.addAll(pOther.animationList);
		return newInstance;
	}

	public boolean isEmpty() {
		return this.animationList.isEmpty();
	}

	public int size() {
		return this.animationList.size();
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj) return true;
		if (pObj == null || getClass() != pObj.getClass()) return false;
		Animation animation = (Animation) pObj;
		return Objects.equals(this.animationList, animation.animationList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.animationList);
	}

	@Override
	public String toString() {
		return "Animation{stages=" + this.animationList + "}";
	}

	public record Frame(@NotNull String animationName, @NotNull AnimationCache.LoopType loopType, int additionalTicks) {

		@NotNull
		public static final String WAIT = "internal.wait";

		public Frame {
			if (additionalTicks < 0) {
				throw new IllegalArgumentException("Additional ticks cannot be negative");
			}
		}

		public Frame(@NotNull String pAnimationName, @NotNull AnimationCache.LoopType pLoopType) {
			this(pAnimationName, pLoopType, 0);
		}

		public boolean isWait() {
			return WAIT.equals(this.animationName);
		}

		@Override
		public boolean equals(@Nullable Object pObj) {
			if (this == pObj) return true;
			if (pObj == null || getClass() != pObj.getClass()) return false;
			Frame frame = (Frame) pObj;
			return this.additionalTicks == frame.additionalTicks
					&& Objects.equals(this.animationName, frame.animationName)
					&& this.loopType == frame.loopType;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.animationName, this.loopType, this.additionalTicks);
		}
	}
}