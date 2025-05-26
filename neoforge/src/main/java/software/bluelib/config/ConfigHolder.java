/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import software.bluelib.config.bluelib.LoggerConfig;
import software.bluelib.config.bluelib.MarkdownConfig;

public final class ConfigHolder {

    // Type of Configs

    // Markdown
    public static final ModConfigSpec MARKDOWN_SPEC;
    public static final MarkdownConfig MARKDOWN;

    // Logger
    public static final ModConfigSpec LOGGER_SPEC;
    public static final LoggerConfig LOGGER;

    static {
        {
            Pair<MarkdownConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(MarkdownConfig::new);
            MARKDOWN = specPair.getLeft();
            MARKDOWN_SPEC = specPair.getRight();
        }
        {
            Pair<LoggerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(LoggerConfig::new);
            LOGGER = specPair.getLeft();
            LOGGER_SPEC = specPair.getRight();
        }
    }
}
