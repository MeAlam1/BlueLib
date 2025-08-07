/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiMoLangContext extends BaseMoLangContext {

	private final List<BaseMoLangContext> contexts;

	public MultiMoLangContext(List<BaseMoLangContext> pContexts) {
		this.contexts = pContexts;
	}

	@Override
	public @Nullable Object getVariable(@NotNull String pName) {
		for (BaseMoLangContext ctx : contexts) {
			Object value = ctx.getVariable(pName);
			if (value != null) return value;
		}
		return null;
	}
}
