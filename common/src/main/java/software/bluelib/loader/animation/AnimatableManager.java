package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.state.BoneSnapshot;
import software.bluelib.loader.constant.dataticket.DataTicket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AnimatableManager<T extends GeoAnimatable> {
	private final Map<String, BoneSnapshot> boneSnapshotCollection = new Object2ObjectOpenHashMap<>();
	private final Map<String, AnimationController<T>> animationControllers;
	private Map<DataTicket<?>, Object> extraData;

	private double lastUpdateTime;
	private boolean isFirstTick = true;
	private double firstTickTime = -1;

	
	public AnimatableManager(GeoAnimatable animatable) {
		ControllerRegistrar registrar = new ControllerRegistrar(new ObjectArrayList<>(2));

		animatable.registerControllers(registrar);

		this.animationControllers = registrar.build();
	}

	
	public void addController(AnimationController controller) {
		getAnimationControllers().put(controller.getName(), controller);
	}

	
	public void removeController(String name) {
		getAnimationControllers().remove(name);
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

	public void updatedAt(double updateTime) {
		this.lastUpdateTime = updateTime;
	}

	public double getFirstTickTime() {
		return this.firstTickTime;
	}

	public void startedAt(double time) {
		this.firstTickTime = time;
	}

	public boolean isFirstTick() {
		return this.isFirstTick;
	}

	protected void finishFirstTick() {
		this.isFirstTick = false;
	}

	
	public <D> void setData(DataTicket<D> dataTicket, D data) {
		if (this.extraData == null)
			this.extraData = new Object2ObjectOpenHashMap<>();

		this.extraData.put(dataTicket, data);
	}

	
	public <D> D getData(DataTicket<D> dataTicket) {
		return this.extraData != null ? dataTicket.getData(this.extraData) : null;
	}

	
	public void tryTriggerAnimation(String animName) {
		for (AnimationController<?> controller : getAnimationControllers().values()) {
			if (controller.tryTriggerAnimation(animName))
				return;
		}
	}

	
	public void tryTriggerAnimation(String controllerName, String animName) {
		AnimationController<?> controller = getAnimationControllers().get(controllerName);

		if (controller != null)
			controller.tryTriggerAnimation(animName);
	}

	
	public void stopTriggeredAnimation(@Nullable String animName) {
		for (AnimationController<?> controller : getAnimationControllers().values()) {
			if ((animName == null || controller.triggerableAnimations.get(animName) == controller.getTriggeredAnimation()) && controller.stopTriggeredAnimation())
				return;
		}
	}

	
	public void stopTriggeredAnimation(String controllerName, @Nullable String animName) {
		AnimationController<?> controller = getAnimationControllers().get(controllerName);

		if (controller != null && (animName == null || controller.triggerableAnimations.get(animName) == controller.getTriggeredAnimation()))
			controller.stopTriggeredAnimation();
	}

	
	public record ControllerRegistrar(List<AnimationController<? extends GeoAnimatable>> controllers) {
		
		public ControllerRegistrar add(AnimationController<?>... controllers) {
			controllers().addAll(Arrays.asList(controllers));

			return this;
		}

		
		public ControllerRegistrar add(AnimationController<?> controller) {
			controllers().add(controller);

			return this;
		}

		
		public ControllerRegistrar remove(String name) {
			controllers().removeIf(controller -> controller.getName().equals(name));

			return this;
		}

		@ApiStatus.Internal
		private <T extends GeoAnimatable> Object2ObjectArrayMap<String, AnimationController<T>> build() {
			Object2ObjectArrayMap<String, AnimationController<?>> map = new Object2ObjectArrayMap<>(controllers().size());

			controllers().forEach(controller -> map.put(controller.getName(), controller));

			return (Object2ObjectArrayMap)map;
		}
	}
}
