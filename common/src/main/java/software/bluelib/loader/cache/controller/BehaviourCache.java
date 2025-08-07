/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.controller;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public record BehaviourCache(
		@NotNull List<String> conditions,
		@Nullable Integer priority,
		@NotNull Map<String, StateCache> states) {

	@Nullable
	public StateCache getState(@NotNull String pName) {
		StateCache state = states.get(pName);
		if (state == null) {
			BaseLogger.log(BaseLogLevel.WARNING, "State not found: " + pName);
		}
		return state;
	}

	@NotNull
	public StateCache getMainState() {
		return states.values().iterator().next();
	}
}
