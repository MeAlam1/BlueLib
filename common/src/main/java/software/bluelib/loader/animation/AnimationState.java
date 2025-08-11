/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.state.PlayState;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;

public class AnimationState<T extends BlueAnimatable> {

	@NotNull
	private final T animatable;
	private final float limbSwing;
	private final float limbSwingAmount;
	private final float partialTick;
	private final boolean isMoving;
	@NotNull
	private final Map<DataTicket<?>, Object> extraData = new Object2ObjectOpenHashMap<>();

	@Nullable
	protected AnimationController<T> controller;
	public double animationTick;

	public AnimationState(@NotNull T pAnimatable, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, boolean pIsMoving) {
		this.animatable = pAnimatable;
		this.limbSwing = pLimbSwing;
		this.limbSwingAmount = pLimbSwingAmount;
		this.partialTick = pPartialTick;
		this.isMoving = pIsMoving;
	}

	public double getAnimationTick() {
		return this.animationTick;
	}

	@NotNull
	public T getAnimatable() {
		return this.animatable;
	}

	public float getLimbSwing() {
		return this.limbSwing;
	}

	public float getLimbSwingAmount() {
		return this.limbSwingAmount;
	}

	public float getPartialTick() {
		return this.partialTick;
	}

	public boolean isMoving() {
		return this.isMoving;
	}

	public @Nullable AnimationController<T> getController() {
		return this.controller;
	}

	@NotNull
	public AnimationState<T> withController(@NotNull AnimationController<T> pController) {
		this.controller = pController;

		return this;
	}

	@NotNull
	public Map<DataTicket<?>, ?> getExtraData() {
		return this.extraData;
	}

	@Nullable
	public <D> D getData(@NotNull DataTicket<D> pDataTicket) {
		return pDataTicket.getData(this.extraData);
	}

	public <D> void setData(@NotNull DataTicket<D> pDataTicket, D pData) {
		this.extraData.put(pDataTicket, pData);
	}

	public void setAnimation(@NotNull Animation pAnimation) {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		getController().setAnimation(pAnimation);
	}

	@NotNull
	public PlayState setAndContinue(@NotNull Animation pAnimation) {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		getController().setAnimation(pAnimation);

		return PlayState.PLAY;
	}

	public boolean isCurrentAnimation(@NotNull Animation pAnimation) {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		return Objects.equals(getController().currentRawAnimation, pAnimation);
	}

	public boolean isCurrentAnimationStage(@NotNull String pName) {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		return getController().getCurrentAnimation() != null && getController().getCurrentAnimation().animationCache().name().equals(pName);
	}

	public void resetCurrentAnimation() {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		getController().forceAnimationReset();
	}

	public void setControllerSpeed(@NotNull Double pSpeed) {
		if (getController() == null) {
			throw new IllegalStateException("AnimationController is not set for this AnimationState.");
		}
		getController().setAnimationSpeed(pSpeed);
	}
}
