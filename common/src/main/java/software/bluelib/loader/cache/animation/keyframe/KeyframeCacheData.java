package software.bluelib.loader.cache.animation.keyframe;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.EasingCache;

public record KeyframeCacheData(
		@NotNull List<MoLangValue> pre,
		@NotNull List<MoLangValue> post,
		@Nullable EasingCache easing,
		@Nullable List<MoLangValue> easingArgs) {

	public static KeyframeCacheData fromArray(@NotNull List<MoLangValue> pValues) {
		return new KeyframeCacheData(pValues, pValues, null, null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.pre, this.post, this.easing, this.easingArgs);
	}

	@Override
	public boolean equals(@Nullable Object pObj) {
		if (this == pObj) return true;
		if (pObj == null || getClass() != pObj.getClass()) return false;
		return hashCode() == pObj.hashCode();
	}
}