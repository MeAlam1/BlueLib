package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animation.AnimationController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record LoopTypeCache(@NotNull String name,
                            @NotNull Behavior behavior) {

	private static final @NotNull Map<String, LoopTypeCache> ALL_LOOP_TYPES = new ConcurrentHashMap<>(4);

	@FunctionalInterface
	public interface Behavior {
		boolean shouldPlayAgain(@NotNull BlueAnimatable pAnimatable,
		                        @NotNull AnimationController<? extends BlueAnimatable> pController,
		                        @NotNull AnimationCache pAnimationCache);
	}

	public LoopTypeCache register() {
		ALL_LOOP_TYPES.put(name, this);
		return this;
	}

	public static @NotNull LoopTypeCache fromString(@NotNull String pName) {
		return ALL_LOOP_TYPES.getOrDefault(pName, PLAY_ONCE);
	}

	public static final LoopTypeCache PLAY_ONCE = new LoopTypeCache("play_once",
			(anim, controller, cache) -> false).register();
	public static final LoopTypeCache LOOP = new LoopTypeCache("loop",
			(anim, controller, cache) -> true).register();
	public static final LoopTypeCache HOLD_ON_LAST_FRAME = new LoopTypeCache("hold_on_last_frame",
			(anim, controller, cache) -> {
				controller.animationState = AnimationController.State.PAUSED;
				return true;
			}).register();
}
