/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "unused" })
public class ConfigBuilder {

	@NotNull
	public static ModConfigSpec.IntValue buildInt(@NotNull ModConfigSpec.Builder pBuilder, @NotNull String pName, @NotNull Integer pDefaultValue, @NotNull Integer pMin, @NotNull Integer pMax, @NotNull String pComment) {
		return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
	}

	@NotNull
	public static ModConfigSpec.DoubleValue buildDouble(@NotNull ModConfigSpec.Builder pBuilder, @NotNull String pName, @NotNull Double pDefaultValue, @NotNull Double pMin, @NotNull Double pMax, @NotNull String pComment) {
		return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
	}

	@NotNull
	public static ModConfigSpec.BooleanValue buildBoolean(@NotNull ModConfigSpec.Builder pBuilder, @NotNull String pName, boolean pDefaultValue, @NotNull String pComment) {
		return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
	}

	@NotNull
	public static ModConfigSpec.LongValue buildLong(@NotNull ModConfigSpec.Builder pBuilder, @NotNull String pName, @NotNull Long pDefaultValue, @NotNull Long pMin, @NotNull Long pMax, @NotNull String pComment) {
		return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
	}

	@NotNull
	public static ModConfigSpec.ConfigValue<String> buildString(@NotNull ModConfigSpec.Builder pBuilder, @NotNull String pName, @NotNull String pDefaultValue, @NotNull String pComment) {
		return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
	}
}
