package software.bluelib.config;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class BlueLibConfig {

    public static void bakeMarkdown(software.bluelib.config.bluelib.MarkdownConfig pConfig) {
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
            BaseLogger.log(BaseLogLevel.INFO, "The Markdown Config of BlueLib has been loaded.", true);
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.WARNING, "The Markdown Config of BlueLib has not been loaded. " + pException.getMessage(), true);
        }
    }
}
