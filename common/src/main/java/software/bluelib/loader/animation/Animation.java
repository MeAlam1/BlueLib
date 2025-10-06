/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.animation.LoopTypeCache;

public final class Animation {

	@NotNull
	private final List<Stage> animationList = new ObjectArrayList<>();

	private Animation() {}

	@NotNull
	public static Animation begin() {
		return new Animation();
	}

	@NotNull
	public Animation thenPlay(@NotNull String pAnimationName) {
		return then(pAnimationName, LoopTypeCache.DEFAULT);
	}

	@NotNull
	public Animation thenLoop(@NotNull String pAnimationName) {
		return then(pAnimationName, LoopTypeCache.LOOP);
	}

	@NotNull
	public Animation thenWait(int pTicks) {
		if (pTicks < 0)
			throw new IllegalArgumentException("Wait time cannot be negative.");
		return addStage(Stage.WAIT, LoopTypeCache.PLAY_ONCE, pTicks);
	}

	@NotNull
	public Animation thenPlayAndHold(@NotNull String pAnimation) {
		return then(pAnimation, LoopTypeCache.HOLD_ON_LAST_FRAME);
	}

	@NotNull
	public Animation thenPlayXTimes(@NotNull String pAnimationName, int pPlayCount) {
		for (int i = 0; i < pPlayCount; i++) {
			then(pAnimationName, i == pPlayCount - 1 ? LoopTypeCache.DEFAULT : LoopTypeCache.PLAY_ONCE);
		}

		return this;
	}

	@NotNull
	public List<Stage> getAnimationStages() {
		return List.copyOf(this.animationList);
	}

	@NotNull
	public static Animation copyOf(@NotNull Animation pOther) {
		Animation newInstance = Animation.begin();

		newInstance.animationList.addAll(pOther.animationList);

		return newInstance;
	}

	@NotNull
	public Animation build() {
		return this;
	}

	@NotNull
	public Animation then(@NotNull String pName, @NotNull LoopTypeCache pLoopTypeCache) {
		return addStage(pName, pLoopTypeCache, 0);
	}

	private Animation addStage(@NotNull String pName, @NotNull LoopTypeCache pLoopTypeCache, int pTicks) {
		this.animationList.add(new Stage(pName, pLoopTypeCache, pTicks));
		return this;
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj)
			return true;
		if (!(pObj instanceof Animation other))
			return false;
		return Objects.equals(this.animationList, other.animationList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.animationList);
	}

	@Override
	public String toString() {
		return "Animation: " + animationList;
	}

	public record Stage(@NotNull String animationName, @NotNull LoopTypeCache loopTypeCache, int additionalTicks) {

		@NotNull
		public static final String WAIT = "internal.wait";

		public Stage(@NotNull String pAnimationName, @NotNull LoopTypeCache pLoopTypeCache) {
			this(pAnimationName, pLoopTypeCache, 0);
		}

		@Override
		public boolean equals(@Nullable Object pObj) {
			if (this == pObj)
				return true;
			if (!(pObj instanceof Stage(String pName, LoopTypeCache pLoopTypeCache, int pTicks)))
				return false;
			return Objects.equals(this.animationName, pName)
					&& this.loopTypeCache == pLoopTypeCache
					&& this.additionalTicks == pTicks;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.animationName, this.loopTypeCache, this.additionalTicks);
		}
	}
}
