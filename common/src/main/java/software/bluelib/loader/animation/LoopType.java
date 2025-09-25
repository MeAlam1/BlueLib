package software.bluelib.loader.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.animation.AnimationCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: WHY GECKOLIB, WHY
@FunctionalInterface
public interface LoopType {

	@NotNull
	Map<String, LoopType> LOOP_TYPES = new ConcurrentHashMap<>(4);

	@NotNull
	LoopType DEFAULT = (animatable, controller, currentAnimation) -> currentAnimation.loopType().shouldPlayAgain(animatable, controller, currentAnimation);
	@NotNull
	LoopType PLAY_ONCE = register("play_once", register("false", (animatable, controller, currentAnimation) -> false));
	@NotNull
	LoopType HOLD_ON_LAST_FRAME = register("hold_on_last_frame", (animatable, controller, currentAnimation) -> {
		controller.animationState = AnimationController.State.PAUSED;

		return true;
	});
	@NotNull
	LoopType LOOP = register("loop", register("true", (animatable, controller, currentAnimation) -> true));

	boolean shouldPlayAgain(@NotNull BlueAnimatable pAnimatable, @NotNull AnimationController<? extends BlueAnimatable> pController, @NotNull AnimationCache pCurrentAnimationCache);

	@NotNull
	static LoopType fromJson(@Nullable JsonElement pJson) {
		if (pJson == null || !pJson.isJsonPrimitive())
			return PLAY_ONCE;

		JsonPrimitive primitive = pJson.getAsJsonPrimitive();

		if (primitive.isBoolean())
			return primitive.getAsBoolean() ? LOOP : PLAY_ONCE;

		if (primitive.isString())
			return fromString(primitive.getAsString());

		return PLAY_ONCE;
	}

	@NotNull
	static LoopType fromString(@NotNull String pName) {
		return LOOP_TYPES.getOrDefault(pName, PLAY_ONCE);
	}

	@NotNull
	static LoopType register(@NotNull String pName, @NotNull LoopType pLoopType) {
		LOOP_TYPES.put(pName, pLoopType);

		return pLoopType;
	}
}
