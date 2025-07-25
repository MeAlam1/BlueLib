/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.controller.ControllerManager;
import software.bluelib.oldLoader.animation.AnimationController;
import software.bluelib.oldLoader.animation.state.BoneSnapshot;
import software.bluelib.oldLoader.constant.dataticket.DataTicket;

public class AnimatableManager<T extends BlueAnimatable> {

	private final Map<String, BoneSnapshot> boneSnapshotCollection = new Object2ObjectOpenHashMap<>();
	private final Map<String, AnimationController<T>> animationControllers;
	private Map<DataTicket<?>, Object> extraData;

	private double lastUpdateTime;
	private boolean isFirstTick = true;
	private double firstTickTime = -1;

	public AnimatableManager(@NotNull BlueAnimatable pAnimatable) {
		ControllerRegistrar registrar = new ControllerRegistrar(new ObjectArrayList<>(2));

		ControllerCache controllerCache = ControllerManager.getBakedController(pAnimatable.getControllerResource());
		ControllerManager.registerControllers(pAnimatable, controllerCache, registrar, null);

		this.animationControllers = registrar.build();
	}

	public void addController(@NotNull AnimationController pController) {
		getAnimationControllers().put(pController.getName(), pController);
	}

	public void removeController(@NotNull String pName) {
		getAnimationControllers().remove(pName);
	}

	public Map<String, AnimationController<T>> getAnimationControllers() {
		return this.animationControllers;
	}

	public Map<String, BoneSnapshot> getBoneSnapshotCollection() {
		return this.boneSnapshotCollection;
	}

	public void clearSnapshotCache() {
		getBoneSnapshotCollection().clear();
	}

	public double getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void updatedAt(@NotNull Double pUpdateTime) {
		this.lastUpdateTime = pUpdateTime;
	}

	public double getFirstTickTime() {
		return this.firstTickTime;
	}

	public void startedAt(@NotNull Double pTime) {
		this.firstTickTime = pTime;
	}

	public boolean isFirstTick() {
		return this.isFirstTick;
	}

	public void finishFirstTick() {
		this.isFirstTick = false;
	}

	public <D> void setData(@NotNull DataTicket<D> pDataTicket, @NotNull D pData) {
		if (this.extraData == null)
			this.extraData = new Object2ObjectOpenHashMap<>();

		this.extraData.put(pDataTicket, pData);
	}

	public <D> D getData(@NotNull DataTicket<D> pDataTicket) {
		return this.extraData != null ? pDataTicket.getData(this.extraData) : null;
	}

	public void tryTriggerAnimation(@NotNull String pAnimName) {
		for (AnimationController<?> controller : getAnimationControllers().values()) {
			if (controller.tryTriggerAnimation(pAnimName))
				return;
		}
	}

	public void tryTriggerAnimation(@NotNull String pControllerName, @NotNull String pAnimName) {
		AnimationController<?> controller = getAnimationControllers().get(pControllerName);

		if (controller != null)
			controller.tryTriggerAnimation(pAnimName);
	}

	public void stopTriggeredAnimation(@Nullable String pAnimName) {
		for (AnimationController<?> controller : getAnimationControllers().values()) {
			if ((pAnimName == null || controller.triggerableAnimations.get(pAnimName) == controller.getTriggeredAnimation()) && controller.stopTriggeredAnimation())
				return;
		}
	}

	public void stopTriggeredAnimation(@NotNull String pControllerName, @Nullable String pAnimName) {
		AnimationController<?> controller = getAnimationControllers().get(pControllerName);

		if (controller != null && (pAnimName == null || controller.triggerableAnimations.get(pAnimName) == controller.getTriggeredAnimation()))
			controller.stopTriggeredAnimation();
	}

	public record ControllerRegistrar(List<AnimationController<? extends BlueAnimatable>> controllers) {

		public ControllerRegistrar add(@NotNull AnimationController<?>... pControllers) {
			controllers().addAll(Arrays.asList(pControllers));

			return this;
		}

		public ControllerRegistrar add(@NotNull AnimationController<?> pController) {
			controllers().add(pController);

			return this;
		}

		public ControllerRegistrar remove(@NotNull String pName) {
			controllers().removeIf(controller -> controller.getName().equals(pName));

			return this;
		}

		@ApiStatus.Internal
		private <T extends BlueAnimatable> Object2ObjectArrayMap<String, AnimationController<T>> build() {
			Object2ObjectArrayMap<String, AnimationController<?>> map = new Object2ObjectArrayMap<>(controllers().size());

			controllers().forEach(controller -> map.put(controller.getName(), controller));

			return (Object2ObjectArrayMap) map;
		}
	}
}
