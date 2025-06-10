/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import net.neoforged.fml.config.ModConfig;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public class BlueLibConfig {

    public static void bakeMarkdown(final ModConfig pConfig) {
        try {
            MarkdownConfig.isMarkdownEnabled = ConfigHolder.MARKDOWN.isMarkdownEnabled.get();
            MarkdownConfig.boldPrefix = ConfigHolder.MARKDOWN.boldPrefix.get();
            MarkdownConfig.boldSuffix = ConfigHolder.MARKDOWN.boldSuffix.get();
            MarkdownConfig.isBoldEnabled = ConfigHolder.MARKDOWN.isBoldEnabled.get();
            MarkdownConfig.italicPrefix = ConfigHolder.MARKDOWN.italicPrefix.get();
            MarkdownConfig.italicSuffix = ConfigHolder.MARKDOWN.italicSuffix.get();
            MarkdownConfig.isItalicEnabled = ConfigHolder.MARKDOWN.isItalicEnabled.get();
            MarkdownConfig.underlinePrefix = ConfigHolder.MARKDOWN.underlinePrefix.get();
            MarkdownConfig.underlineSuffix = ConfigHolder.MARKDOWN.underlineSuffix.get();
            MarkdownConfig.isUnderlineEnabled = ConfigHolder.MARKDOWN.isUnderlineEnabled.get();
            MarkdownConfig.strikethroughPrefix = ConfigHolder.MARKDOWN.strikethroughPrefix.get();
            MarkdownConfig.strikethroughSuffix = ConfigHolder.MARKDOWN.strikethroughSuffix.get();
            MarkdownConfig.isStrikethroughEnabled = ConfigHolder.MARKDOWN.isStrikethroughEnabled.get();
            MarkdownConfig.spoilerPrefix = ConfigHolder.MARKDOWN.spoilerPrefix.get();
            MarkdownConfig.spoilerSuffix = ConfigHolder.MARKDOWN.spoilerSuffix.get();
            MarkdownConfig.isSpoilerEnabled = ConfigHolder.MARKDOWN.isSpoilerEnabled.get();
            MarkdownConfig.hyperlinkPrefix = ConfigHolder.MARKDOWN.hyperlinkPrefix.get();
            MarkdownConfig.hyperlinkSuffix = ConfigHolder.MARKDOWN.hyperlinkSuffix.get();
            MarkdownConfig.isHyperlinkEnabled = ConfigHolder.MARKDOWN.isHyperlinkEnabled.get();
            MarkdownConfig.colorPrefix = ConfigHolder.MARKDOWN.colorPrefix.get();
            MarkdownConfig.colorSuffix = ConfigHolder.MARKDOWN.colorSuffix.get();
            MarkdownConfig.isColorEnabled = ConfigHolder.MARKDOWN.isColorEnabled.get();
            MarkdownConfig.isCopyToClipboardEnabled = ConfigHolder.MARKDOWN.isCopyToClipboardEnabled.get();
            BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.config("markdown.loaded"));
        } catch (Exception pException) {
            BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.config("markdown.failed", pException.getMessage()));
        }
    }

    public static void bakeLogger(final ModConfig pConfig) {
        try {
            LoggerConfig.isBlueLibLoggingEnabled = ConfigHolder.LOGGER.isBlueLibLoggingEnabled.get();
            LoggerConfig.isLoggingEnabled = ConfigHolder.LOGGER.isLoggingEnabled.get();
            BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.config("markdown.loaded"));
        } catch (Exception pException) {
            BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.config("markdown.failed", pException.getMessage()));
        }
    }
}
