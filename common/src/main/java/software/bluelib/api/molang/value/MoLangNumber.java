package software.bluelib.api.molang.value;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.MoLangRuntime;

public record MoLangNumber(@NotNull Float value) implements MoLangValue {
	@Override
	public @NotNull Object evaluate(@NotNull MoLangRuntime pRuntime) {
		return value;
	}
}
