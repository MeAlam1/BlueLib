package software.bluelib.loader.loading.object;

import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.Animation;

import java.util.Map;


public record BakedAnimations(Map<String, Animation> animations) {
	
	@Nullable
	public Animation getAnimation(String name) {
		return animations.get(name);
	}
}
