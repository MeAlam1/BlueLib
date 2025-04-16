package software.bluelib.config;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class BlueLibConfig {

    public static void bakeMarkdown(software.bluelib.config.bluelib.MarkdownConfig config) {
        try {
            MarkdownConfig.isMarkdownEnabled = config.isMarkdownEnabled;
            MarkdownConfig.boldPrefix = config.boldPrefix;
            MarkdownConfig.boldSuffix = config.boldSuffix;
            MarkdownConfig.isBoldEnabled = config.isBoldEnabled;
            MarkdownConfig.italicPrefix = config.italicPrefix;
            MarkdownConfig.italicSuffix = config.italicSuffix;
            MarkdownConfig.isItalicEnabled = config.isItalicEnabled;
            MarkdownConfig.underlinePrefix = config.underlinePrefix;
            MarkdownConfig.underlineSuffix = config.underlineSuffix;
            MarkdownConfig.isUnderlineEnabled = config.isUnderlineEnabled;
            MarkdownConfig.strikethroughPrefix = config.strikethroughPrefix;
            MarkdownConfig.strikethroughSuffix = config.strikethroughSuffix;
            MarkdownConfig.isStrikethroughEnabled = config.isStrikethroughEnabled;
            MarkdownConfig.spoilerPrefix = config.spoilerPrefix;
            MarkdownConfig.spoilerSuffix = config.spoilerSuffix;
            MarkdownConfig.isSpoilerEnabled = config.isSpoilerEnabled;
            MarkdownConfig.hyperlinkPrefix = config.hyperlinkPrefix;
            MarkdownConfig.hyperlinkSuffix = config.hyperlinkSuffix;
            MarkdownConfig.isHyperlinkEnabled = config.isHyperlinkEnabled;
            MarkdownConfig.colorPrefix = config.colorPrefix;
            MarkdownConfig.colorSuffix = config.colorSuffix;
            MarkdownConfig.isColorEnabled = config.isColorEnabled;
            MarkdownConfig.isCopyToClipboardEnabled = config.isCopyToClipboardEnabled;
            BaseLogger.log(BaseLogLevel.INFO, "The Markdown Config of BlueLib has been loaded.", true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.WARNING, "The Markdown Config of BlueLib has not been loaded. " + pException.getMessage(), true);
        }
    }
}
