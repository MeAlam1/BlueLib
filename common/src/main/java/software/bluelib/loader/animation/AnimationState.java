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
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;
import software.bluelib.oldLoader.animation.PlayState;
import software.bluelib.oldLoader.animation.RawAnimation;

public class AnimationState<T extends BlueAnimatable> {

	private final T animatable;
	private final float limbSwing;
	private final float limbSwingAmount;
	private final float partialTick;
	private final boolean isMoving;
	private final Map<DataTicket<?>, Object> extraData = new Object2ObjectOpenHashMap<>();

	protected AnimationController<T> controller;
	public double animationTick;

	public AnimationState(T pAnimatable, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, boolean pIsMoving) {
		this.animatable = pAnimatable;
		this.limbSwing = pLimbSwing;
		this.limbSwingAmount = pLimbSwingAmount;
		this.partialTick = pPartialTick;
		this.isMoving = pIsMoving;
	}

	public double getAnimationTick() {
		return this.animationTick;
	}

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

	public AnimationController<T> getController() {
		return this.controller;
	}

	public AnimationState<T> withController(AnimationController<T> pController) {
		this.controller = pController;

		return this;
	}

	public Map<DataTicket<?>, ?> getExtraData() {
		return this.extraData;
	}

	@Nullable
	public <D> D getData(DataTicket<D> pDataTicket) {
		return pDataTicket.getData(this.extraData);
	}

	public <D> void setData(DataTicket<D> pDataTicket, D pData) {
		this.extraData.put(pDataTicket, pData);
	}

	public void setAnimation(RawAnimation pAnimation) {
		getController().setAnimation(pAnimation);
	}

	public PlayState setAndContinue(RawAnimation pAnimation) {
		getController().setAnimation(pAnimation);

		return PlayState.PLAY;
	}

	public boolean isCurrentAnimation(RawAnimation pAnimation) {
		return Objects.equals(getController().currentRawAnimation, pAnimation);
	}

	public boolean isCurrentAnimationStage(String pName) {
		return getController().getCurrentAnimation() != null && getController().getCurrentAnimation().animationCache().name().equals(pName);
	}

	public void resetCurrentAnimation() {
		getController().forceAnimationReset();
	}

	public void setControllerSpeed(Double pSpeed) {
		getController().setAnimationSpeed(pSpeed);
	}
}
