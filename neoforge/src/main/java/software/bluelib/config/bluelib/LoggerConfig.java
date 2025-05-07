// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.config.bluelib;

import net.neoforged.neoforge.common.ModConfigSpec;
import software.bluelib.api.config.ConfigBuilder;

public class LoggerConfig {

    public final ModConfigSpec.BooleanValue isBlueLibLoggingEnabled;
    public final ModConfigSpec.BooleanValue isLoggingEnabled;

    public LoggerConfig(final ModConfigSpec.Builder pBuilder) {
        pBuilder.push("Logging");
        isBlueLibLoggingEnabled = ConfigBuilder.buildBoolean(pBuilder, "BlueLibLogging", true, "Default is 'false/off'");
        isLoggingEnabled = ConfigBuilder.buildBoolean(pBuilder, "modLogging", true, "Default is 'false/off'");
    }
}
