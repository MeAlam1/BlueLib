/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config.bluelib;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.config.ConfigBuilder;

public class LoggerConfig {

	@NotNull
	public final ModConfigSpec.BooleanValue isBlueLibLoggingEnabled;
	@NotNull
	public final ModConfigSpec.BooleanValue isLoggingEnabled;

	public LoggerConfig(@NotNull final ModConfigSpec.Builder pBuilder) {
		pBuilder.push("Logging");
		isBlueLibLoggingEnabled = ConfigBuilder.buildBoolean(pBuilder, "BlueLibLogging", true, "Default is 'false/off'");
		isLoggingEnabled = ConfigBuilder.buildBoolean(pBuilder, "modLogging", true, "Default is 'false/off'");
	}
}
