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
import software.bluelib.loader.geckolib.animations.LoopType;

public final class Animation {

	@NotNull
	private final List<Stage> animationList = new ObjectArrayList<>();

	// Private constructor to force usage of factory for logical operations
	private Animation() {}

	@NotNull
	public static Animation begin() {
		return new Animation();
	}

	@NotNull
	public Animation thenPlay(@NotNull String pAnimationName) {
		return then(pAnimationName, LoopType.DEFAULT);
	}

	@NotNull
	public Animation thenLoop(@NotNull String pAnimationName) {
		return then(pAnimationName, LoopType.LOOP);
	}

	@NotNull
	public Animation thenWait(int pTicks) {
		this.animationList.add(new Stage(Stage.WAIT, LoopType.PLAY_ONCE, pTicks));

		return this;
	}

	@NotNull
	public Animation thenPlayAndHold(@NotNull String pAnimation) {
		return then(pAnimation, LoopType.HOLD_ON_LAST_FRAME);
	}

	@NotNull
	public Animation thenPlayXTimes(@NotNull String pAnimationName, int pPlayCount) {
		for (int i = 0; i < pPlayCount; i++) {
			then(pAnimationName, i == pPlayCount - 1 ? LoopType.DEFAULT : LoopType.PLAY_ONCE);
		}

		return this;
	}

	@NotNull
	public Animation then(@NotNull String pAnimationName, @NotNull LoopType pLoopType) {
		this.animationList.add(new Stage(pAnimationName, pLoopType));

		return this;
	}

	@NotNull
	public List<Stage> getAnimationStages() {
		return this.animationList;
	}

	@NotNull
	public static Animation copyOf(@NotNull Animation pOther) {
		Animation newInstance = Animation.begin();

		newInstance.animationList.addAll(pOther.animationList);

		return newInstance;
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
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

	public record Stage(@NotNull String animationName, @NotNull LoopType loopType, int additionalTicks) {

		@NotNull
		public static final String WAIT = "internal.wait";

		public Stage(@NotNull String pAnimationName, @NotNull LoopType pLoopType) {
			this(pAnimationName, pLoopType, 0);
		}

		@Override
		public boolean equals(@Nullable Object pObj) {
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
