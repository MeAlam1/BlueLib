/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animatable.base;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.AnimationController;
import software.bluelib.loader.animation.keyframe.BoneFrame;
import software.bluelib.loader.cache.controller.ControllerCache;
import software.bluelib.loader.controller.ControllerManager;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;

public class AnimatableManager<T extends BlueAnimatable> {

	@NotNull
	private final Map<String, BoneFrame> boneSnapshotCollection = new Object2ObjectOpenHashMap<>();
	@NotNull
	private final Map<String, AnimationController<T>> animationControllers;
	@Nullable
	private Map<DataTicket<?>, Object> extraData;

	private double lastUpdateTime;
	private boolean isFirstTick = true;
	private double firstTickTime = -1;

	public AnimatableManager(@NotNull BlueAnimatable pAnimatable) {
		ControllerRegistrar<T> registrar = new ControllerRegistrar<>(new ObjectArrayList<>(2));

		ControllerCache controllerCache = ControllerManager.getBakedController(pAnimatable.getControllerResource());
		new ControllerManager<T>().registerControllers(pAnimatable, controllerCache, registrar);

		pAnimatable.registerControllers(registrar);

		this.animationControllers = registrar.build();
	}

	public void addController(@NotNull AnimationController<T> pController) {
		getAnimationControllers().put(pController.getName(), pController);
	}

	public void removeController(@NotNull String pName) {
		getAnimationControllers().remove(pName);
	}

	@NotNull
	public Map<String, AnimationController<T>> getAnimationControllers() {
		return this.animationControllers;
	}

	@NotNull
	public Map<String, BoneFrame> getBoneSnapshotCollection() {
		return this.boneSnapshotCollection;
	}

	public void clearSnapshotCache() {
		getBoneSnapshotCollection().clear();
	}

	@NotNull
	public Double getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void updatedAt(@NotNull Double pUpdateTime) {
		this.lastUpdateTime = pUpdateTime;
	}

	@NotNull
	public Double getFirstTickTime() {
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

	@Nullable
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

	public void stopTriggeredAnimation(@Nullable String pControllerName, @Nullable String pAnimName) {
		AnimationController<?> controller = getAnimationControllers().get(pControllerName);

		if (controller != null && (pAnimName == null || controller.triggerableAnimations.get(pAnimName) == controller.getTriggeredAnimation()))
			controller.stopTriggeredAnimation();
	}

	public record ControllerRegistrar<T extends BlueAnimatable>(@NotNull List<AnimationController<T>> controllers) {

		@SafeVarargs
		@NotNull
		public final ControllerRegistrar<T> add(@NotNull AnimationController<T>... pControllers) {
			controllers().addAll(Arrays.asList(pControllers));

			return this;
		}

		@NotNull
		public ControllerRegistrar<T> add(@NotNull AnimationController<T> pController) {
			controllers().add(pController);

			return this;
		}

		@NotNull
		public ControllerRegistrar<T> remove(@NotNull String pName) {
			controllers().removeIf(controller -> controller.getName().equals(pName));

			return this;
		}

		@ApiStatus.Internal
		@NotNull
		private Object2ObjectArrayMap<String, AnimationController<T>> build() {
			Object2ObjectArrayMap<String, AnimationController<T>> map = new Object2ObjectArrayMap<>(controllers().size());

			controllers().forEach(controller -> map.put(controller.getName(), controller));

			return map;
		}
	}
}
