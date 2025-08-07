/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.base;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.bone.BoneSnapshot;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;

public abstract class ContextAwareAnimatableManager<T extends BlueAnimatable, C> extends AnimatableManager<T> {

	private final Map<C, AnimatableManager<T>> managers;

	public ContextAwareAnimatableManager(@NotNull BlueAnimatable pAnimatable) {
		super(pAnimatable);

		this.managers = buildContextOptions(pAnimatable);
	}

	protected abstract Map<C, AnimatableManager<T>> buildContextOptions(@NotNull BlueAnimatable pAnimatable);

	public abstract C getCurrentContext();

	public AnimatableManager<T> getManagerForContext(@NotNull C pContext) {
		return this.managers.get(pContext);
	}

	public void addController(@NotNull AnimationController<T> pController) {
		getManagerForContext(getCurrentContext()).addController(pController);
	}

	public void removeController(@NotNull String pName) {
		getManagerForContext(getCurrentContext()).removeController(pName);
	}

	public Map<String, AnimationController<T>> getAnimationControllers() {
		return getManagerForContext(getCurrentContext()).getAnimationControllers();
	}

	public Map<String, BoneSnapshot> getBoneSnapshotCollection() {
		return getManagerForContext(getCurrentContext()).getBoneSnapshotCollection();
	}

	public void clearSnapshotCache() {
		getManagerForContext(getCurrentContext()).clearSnapshotCache();
	}

	public double getLastUpdateTime() {
		return getManagerForContext(getCurrentContext()).getLastUpdateTime();
	}

	public void updatedAt(@NotNull Double pUpdateTime) {
		getManagerForContext(getCurrentContext()).updatedAt(pUpdateTime);
	}

	public double getFirstTickTime() {
		return getManagerForContext(getCurrentContext()).getFirstTickTime();
	}

	public void startedAt(@NotNull Double pTime) {
		getManagerForContext(getCurrentContext()).startedAt(pTime);
	}

	public boolean isFirstTick() {
		return getManagerForContext(getCurrentContext()).isFirstTick();
	}

	public void finishFirstTick() {
		getManagerForContext(getCurrentContext()).finishFirstTick();
	}

	public void tryTriggerAnimation(@NotNull String pAnimName) {
		for (AnimatableManager<T> manager : this.managers.values()) {
			manager.tryTriggerAnimation(pAnimName);
		}
	}

	public void tryTriggerAnimation(@NotNull String pControllerName, @NotNull String pAnimName) {
		for (AnimatableManager<T> manager : this.managers.values()) {
			manager.tryTriggerAnimation(pControllerName, pAnimName);
		}
	}

	public void stopTriggeredAnimation(@Nullable String pAnimName) {
		for (AnimatableManager<T> manager : this.managers.values()) {
			manager.stopTriggeredAnimation(pAnimName);
		}
	}

	public void stopTriggeredAnimation(@NotNull String pControllerName, @Nullable String pAnimName) {
		for (AnimatableManager<T> manager : this.managers.values()) {
			manager.stopTriggeredAnimation(pControllerName, pAnimName);
		}
	}

	public <D> void setData(@NotNull DataTicket<D> pDataTicket, @NotNull D pData) {
		super.setData(pDataTicket, pData);
	}

	public <D> D getData(@NotNull DataTicket<D> pDataTicket) {
		return super.getData(pDataTicket);
	}
}
