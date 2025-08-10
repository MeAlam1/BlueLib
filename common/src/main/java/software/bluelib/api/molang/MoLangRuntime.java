/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.context.BaseMoLangContext;

@SuppressWarnings({ "unused" })
public record MoLangRuntime(@NotNull List<BaseMoLangContext> contexts) {

	public MoLangRuntime(@NotNull List<BaseMoLangContext> contexts) {
		this.contexts = contexts;
	}

	public @Nullable Object getVariable(@NotNull String pName) {
		for (BaseMoLangContext ctx : contexts) {
			Object result = ctx.getVariable(pName);
			if (result != null) return result;
		}
		return null;
	}

	public @Nullable Object callFunction(@NotNull String pName, @NotNull List<Object> pArguments) {
		for (BaseMoLangContext ctx : contexts) {
			Object result = ctx.callFunction(pName, pArguments);
			if (result != null) return result;
		}
		return null;
	}

	@Override
	public @NotNull List<BaseMoLangContext> contexts() {
		return contexts;
	}
}
