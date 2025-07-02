/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public class BlueLibConfig {

	public static void bakeMarkdown(@NotNull software.bluelib.config.bluelib.MarkdownConfig pConfig) {
		try {
			MarkdownConfig.isMarkdownEnabled = pConfig.isMarkdownEnabled;
			MarkdownConfig.boldPrefix = pConfig.boldPrefix;
			MarkdownConfig.boldSuffix = pConfig.boldSuffix;
			MarkdownConfig.isBoldEnabled = pConfig.isBoldEnabled;
			MarkdownConfig.italicPrefix = pConfig.italicPrefix;
			MarkdownConfig.italicSuffix = pConfig.italicSuffix;
			MarkdownConfig.isItalicEnabled = pConfig.isItalicEnabled;
			MarkdownConfig.underlinePrefix = pConfig.underlinePrefix;
			MarkdownConfig.underlineSuffix = pConfig.underlineSuffix;
			MarkdownConfig.isUnderlineEnabled = pConfig.isUnderlineEnabled;
			MarkdownConfig.strikethroughPrefix = pConfig.strikethroughPrefix;
			MarkdownConfig.strikethroughSuffix = pConfig.strikethroughSuffix;
			MarkdownConfig.isStrikethroughEnabled = pConfig.isStrikethroughEnabled;
			MarkdownConfig.spoilerPrefix = pConfig.spoilerPrefix;
			MarkdownConfig.spoilerSuffix = pConfig.spoilerSuffix;
			MarkdownConfig.isSpoilerEnabled = pConfig.isSpoilerEnabled;
			MarkdownConfig.hyperlinkPrefix = pConfig.hyperlinkPrefix;
			MarkdownConfig.hyperlinkSuffix = pConfig.hyperlinkSuffix;
			MarkdownConfig.isHyperlinkEnabled = pConfig.isHyperlinkEnabled;
			MarkdownConfig.colorPrefix = pConfig.colorPrefix;
			MarkdownConfig.colorSuffix = pConfig.colorSuffix;
			MarkdownConfig.isColorEnabled = pConfig.isColorEnabled;
			MarkdownConfig.isCopyToClipboardEnabled = pConfig.isCopyToClipboardEnabled;
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.config("markdown.loaded"));
		} catch (Exception pException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.config("markdown.failed", pException.getMessage()));
		}
	}

	public static void bakeLogger(@NotNull software.bluelib.config.bluelib.LoggerConfig pConfig) {
		try {
			LoggerConfig.isLoggingEnabled = pConfig.isLoggingEnabled;
			LoggerConfig.isBlueLibLoggingEnabled = pConfig.isBlueLibLoggingEnabled;
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.config("markdown.loaded"));
		} catch (Exception pException) {
			BaseLogger.log(true, BaseLogLevel.ERROR, BlueTranslation.config("markdown.failed", pException.getMessage()));
		}
	}
}
