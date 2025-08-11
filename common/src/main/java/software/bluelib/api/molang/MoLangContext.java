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

public interface MoLangContext {

	@Nullable
	Object getVariable(@NotNull String pName);

	@Nullable
	Object callFunction(@NotNull String pName, @NotNull List<Object> pArguments);
}
