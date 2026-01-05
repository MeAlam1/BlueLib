/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the current state of an animation for an animatable entity.
 * Combines immutable snapshot data with mutable controller interaction.
 *
 * @param <T> the type of animatable entity
 */
public final class AnimationState<T extends BlueAnimatable> {

	@NotNull
	private final T animatable;
	@NotNull
	private final AnimationSnapshot snapshot;
	@NotNull
	private final AnimationExtraData extraData;

	@Nullable
	private AnimationController<T> controller;
	private double animationTick;

	public AnimationState(
			@NotNull T pAnimatable,
			float pLimbSwing,
			float pLimbSwingAmount,
			float pPartialTick,
			boolean pIsMoving
	) {
		this.animatable = pAnimatable;
		this.snapshot = new AnimationSnapshot(pLimbSwing, pLimbSwingAmount, pPartialTick, pIsMoving);
		this.extraData = new AnimationExtraData();
	}

	@NotNull
	public T getAnimatable() {
		return this.animatable;
	}

	public float getLimbSwing() {
		return this.snapshot.limbSwing();
	}

	public float getLimbSwingAmount() {
		return this.snapshot.limbSwingAmount();
	}

	public float getPartialTick() {
		return this.snapshot.partialTick();
	}

	public boolean isMoving() {
		return this.snapshot.isMoving();
	}

	@NotNull
	public AnimationSnapshot getSnapshot() {
		return this.snapshot;
	}

	public double getAnimationTick() {
		return this.animationTick;
	}

	public void setAnimationTick(double pTick) {
		if (pTick < 0) {
			throw new IllegalArgumentException("Animation tick cannot be negative");
		}
		this.animationTick = pTick;
	}

	@NotNull
	public AnimationController<T> getController() {
		if (this.controller == null) {
			throw new IllegalStateException("AnimationController has not been set");
		}
		return this.controller;
	}

	public boolean hasController() {
		return this.controller != null;
	}

	@NotNull
	public AnimationState<T> withController(@NotNull AnimationController<T> pController) {
		this.controller = Objects.requireNonNull(pController, "Controller cannot be null");
		return this;
	}

	@NotNull
	public Map<DataTicket<?>, ?> getExtraData() {
		return this.extraData.asUnmodifiableMap();
	}

	@Nullable
	public <D> D getData(@NotNull DataTicket<D> pTicket) {
		return this.extraData.get(pTicket);
	}

	public <D> void setData(@NotNull DataTicket<D> pTicket, @Nullable D pData) {
		this.extraData.set(pTicket, pData);
	}

	public void setAnimation(@NotNull Animation pAnimation) {
		getController().setAnimation(pAnimation);
	}

	@NotNull
	public PlayState setAndContinue(@NotNull Animation pAnimation) {
		setAnimation(pAnimation);
		return PlayState.PLAY;
	}

	public boolean isCurrentAnimation(@NotNull Animation pAnimation) {
		return Objects.equals(getController().currentRawAnimation, pAnimation);
	}

	public boolean isCurrentAnimationStage(@NotNull String pName) {
		var currentAnim = getController().getCurrentAnimation();
		return currentAnim != null && pName.equals(currentAnim.animationCache().name());
	}

	public void resetCurrentAnimation() {
		getController().forceAnimationReset();
	}

	public void setControllerSpeed(double pSpeed) {
		if (pSpeed < 0) {
			throw new IllegalArgumentException("Speed cannot be negative");
		}
		getController().setAnimationSpeed(pSpeed);
	}

	@Override
	public String toString() {
		return "AnimationState{" +
				"animatable=" + this.animatable.getClass().getSimpleName() +
				", tick=" + this.animationTick +
				", moving=" + this.snapshot.isMoving() +
				", hasController=" + (this.controller != null) +
				'}';
	}
}