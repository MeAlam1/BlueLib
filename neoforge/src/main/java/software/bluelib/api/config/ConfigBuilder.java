/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigBuilder {

    public static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder pBuilder, String pName, int pDefaultValue, int pMin, int pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.DoubleValue buildDouble(ModConfigSpec.Builder pBuilder, String pName, double pDefaultValue, double pMin, double pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder pBuilder, String pName, boolean pDefaultValue, String pComment) {
        return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
    }

    public static ModConfigSpec.LongValue buildLong(ModConfigSpec.Builder pBuilder, String pName, long pDefaultValue, long pMin, long pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.ConfigValue<String> buildString(ModConfigSpec.Builder pBuilder, String pName, String pDefaultValue, String pComment) {
        return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
    }
}
