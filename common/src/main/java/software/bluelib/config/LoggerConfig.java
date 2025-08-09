/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import org.jetbrains.annotations.ApiStatus;

public class LoggerConfig {

	// TODO: BlueLib Logging should remain false by default
	public static boolean isBlueLibLoggingEnabled = true;
	public static boolean isLoggingEnabled = true;
	@ApiStatus.Internal
	public static final boolean isExampleEnabled = false;
}
