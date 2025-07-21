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

public record BehaviourCache(
		@NotNull Map<String, List<StateCache>> states) {

	@NotNull
	public List<StateCache> getStates(@NotNull String pName) {
		return states.get(pName);
	}

	@Nullable
	public StateCache getMainState(@NotNull String pName) {
		List<StateCache> stateList = states.get(pName);
		if (stateList == null || stateList.isEmpty()) {
			return null;
		}
		return stateList.getFirst();
	}
}
