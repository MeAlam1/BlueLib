/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.constant.dataticket.DataTicket;

public class AnimationState<T extends BlueAnimatable> {

	private final T animatable;
	private final float limbSwing;
	private final float limbSwingAmount;
	private final float partialTick;
	private final boolean isMoving;
	private final Map<DataTicket<?>, Object> extraData = new Object2ObjectOpenHashMap<>();

	protected AnimationController<T> controller;
	public double animationTick;

	public AnimationState(T animatable, float limbSwing, float limbSwingAmount, float pPartialTick, boolean isMoving) {
		this.animatable = animatable;
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.partialTick = pPartialTick;
		this.isMoving = isMoving;
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

	public AnimationState<T> withController(AnimationController<T> controller) {
		this.controller = controller;

		return this;
	}

	public Map<DataTicket<?>, ?> getExtraData() {
		return this.extraData;
	}

	@Nullable
	public <D> D getData(DataTicket<D> dataTicket) {
		return dataTicket.getData(this.extraData);
	}

	public <D> void setData(DataTicket<D> dataTicket, D data) {
		this.extraData.put(dataTicket, data);
	}

	public void setAnimation(RawAnimation animation) {
		getController().setAnimation(animation);
	}

	public PlayState setAndContinue(RawAnimation animation) {
		getController().setAnimation(animation);

		return PlayState.CONTINUE;
	}

	public boolean isCurrentAnimation(RawAnimation animation) {
		return Objects.equals(getController().currentRawAnimation, animation);
	}

	public boolean isCurrentAnimationStage(String name) {
		return getController().getCurrentAnimation() != null && getController().getCurrentAnimation().animationCache().name().equals(name);
	}

	public void resetCurrentAnimation() {
		getController().forceAnimationReset();
	}

	public void setControllerSpeed(float speed) {
		getController().setAnimationSpeed(speed);
	}
}
