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
import software.bluelib.loader.cache.animations.AnimationCache;

public final class Animation {

	private final List<Stage> animationList = new ObjectArrayList<>();

	// Private constructor to force usage of factory for logical operations
	private Animation() {}

	public static Animation begin() {
		return new Animation();
	}

	public Animation thenPlay(String pAnimationName) {
		return then(pAnimationName, AnimationCache.LoopType.DEFAULT);
	}

	public Animation thenLoop(String pAnimationName) {
		return then(pAnimationName, AnimationCache.LoopType.LOOP);
	}

	public Animation thenWait(int pTicks) {
		this.animationList.add(new Stage(Stage.WAIT, AnimationCache.LoopType.PLAY_ONCE, pTicks));

		return this;
	}

	public Animation thenPlayAndHold(String pAnimation) {
		return then(pAnimation, AnimationCache.LoopType.HOLD_ON_LAST_FRAME);
	}

	public Animation thenPlayXTimes(String pAnimationName, int pPlayCount) {
		for (int i = 0; i < pPlayCount; i++) {
			then(pAnimationName, i == pPlayCount - 1 ? AnimationCache.LoopType.DEFAULT : AnimationCache.LoopType.PLAY_ONCE);
		}

		return this;
	}

	public Animation then(String pAnimationName, AnimationCache.LoopType pLoopType) {
		this.animationList.add(new Stage(pAnimationName, pLoopType));

		return this;
	}

	public List<Stage> getAnimationStages() {
		return this.animationList;
	}

	public static Animation copyOf(Animation pOther) {
		Animation newInstance = Animation.begin();

		newInstance.animationList.addAll(pOther.animationList);

		return newInstance;
	}

	@Override
	public boolean equals(Object pObj) {
		if (this == pObj)
			return true;

		if (pObj == null || getClass() != pObj.getClass())
			return false;

		return hashCode() == pObj.hashCode();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.animationList);
	}

	public record Stage(String animationName, AnimationCache.LoopType loopType, int additionalTicks) {

		public static final String WAIT = "internal.wait";

		public Stage(String pAnimationName, AnimationCache.LoopType pLoopType) {
			this(pAnimationName, pLoopType, 0);
		}

		@Override
		public boolean equals(Object pObj) {
			if (this == pObj)
				return true;

			if (pObj == null || getClass() != pObj.getClass())
				return false;

			return hashCode() == pObj.hashCode();
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.animationName, this.loopType);
		}
	}
}
