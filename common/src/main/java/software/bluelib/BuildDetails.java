/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import org.jetbrains.annotations.NotNull;

public interface BuildDetails {

	@NotNull
	String getModId();

	@NotNull
	String getVersion();

	boolean displayWarning();
}
