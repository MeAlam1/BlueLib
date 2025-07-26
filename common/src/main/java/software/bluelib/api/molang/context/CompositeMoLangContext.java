/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.MoLangContext;

public class CompositeMoLangContext implements MoLangContext {

	public final List<MoLangContext> contexts = new ArrayList<>();

	public void addContext(@NotNull MoLangContext pContext) {
		if (!containsContext(pContext)) {
			contexts.add(pContext);
		}
	}

	public boolean containsContext(@NotNull MoLangContext pContext) {
		for (MoLangContext ctx : contexts) {
			if (ctx.getClass().equals(pContext.getClass())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public @Nullable Object getVariable(@NotNull String pName) {
		for (MoLangContext ctx : contexts) {
			Object result = ctx.getVariable(pName);
			if (result != null) return result;
		}
		return null;
	}

	@Override
	public @Nullable Object callFunction(@NotNull String pName, @NotNull List<Object> pArguments) {
		for (MoLangContext ctx : contexts) {
			Object result = ctx.callFunction(pName, pArguments);
			if (result != null) return result;
		}
		return null;
	}
}
