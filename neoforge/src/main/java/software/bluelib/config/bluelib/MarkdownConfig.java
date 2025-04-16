package software.bluelib.config.bluelib;

import net.neoforged.neoforge.common.ModConfigSpec;
import software.bluelib.config.ConfigBuilder;

public class MarkdownConfig {


    // List of Config Options
    public final ModConfigSpec.BooleanValue isMarkdownEnabled;

    public final ModConfigSpec.ConfigValue<String> boldPrefix;
    public final ModConfigSpec.ConfigValue<String> boldSuffix;
    public final ModConfigSpec.BooleanValue isBoldEnabled;

    public final ModConfigSpec.ConfigValue<String> italicPrefix;
    public final ModConfigSpec.ConfigValue<String> italicSuffix;
    public final ModConfigSpec.BooleanValue isItalicEnabled;

    public final ModConfigSpec.ConfigValue<String> underlinePrefix;
    public final ModConfigSpec.ConfigValue<String> underlineSuffix;
    public final ModConfigSpec.BooleanValue isUnderlineEnabled;

    public final ModConfigSpec.ConfigValue<String> strikethroughPrefix;
    public final ModConfigSpec.ConfigValue<String> strikethroughSuffix;
    public final ModConfigSpec.BooleanValue isStrikethroughEnabled;

    public final ModConfigSpec.ConfigValue<String> spoilerPrefix;
    public final ModConfigSpec.ConfigValue<String> spoilerSuffix;
    public final ModConfigSpec.BooleanValue isSpoilerEnabled;

    public final ModConfigSpec.ConfigValue<String> hyperlinkPrefix;
    public final ModConfigSpec.ConfigValue<String> hyperlinkSuffix;
    public final ModConfigSpec.BooleanValue isHyperlinkEnabled;

    public final ModConfigSpec.ConfigValue<String> colorPrefix;
    public final ModConfigSpec.ConfigValue<String> colorSuffix;
    public final ModConfigSpec.BooleanValue isColorEnabled;

    public final ModConfigSpec.BooleanValue isCopyToClipboardEnabled;

    // List of Config Options
    public MarkdownConfig(final ModConfigSpec.Builder pBuilder) {
        pBuilder.push("Markdown");
        isMarkdownEnabled = ConfigBuilder.buildBoolean(pBuilder, "markdownEnabled", true, "Default is 'true'");
        pBuilder.push("Bold");
        boldPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "**", "Default is '**'");
        boldSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "**", "Default is '**'");
        isBoldEnabled = ConfigBuilder.buildBoolean(pBuilder, "boldEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Italic");
        italicPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "*", "Default is '*'");
        italicSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "*", "Default is '*'");
        isItalicEnabled = ConfigBuilder.buildBoolean(pBuilder, "italicEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Underline");
        underlinePrefix = ConfigBuilder.buildString(pBuilder, "prefix", "__", "Default is '__'");
        underlineSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "__", "Default is '__'");
        isUnderlineEnabled = ConfigBuilder.buildBoolean(pBuilder, "underlineEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Strikethrough");
        strikethroughPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "~~", "Default is '~~'");
        strikethroughSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "~~", "Default is '~~'");
        isStrikethroughEnabled = ConfigBuilder.buildBoolean(pBuilder, "strikethroughEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Spoiler");
        spoilerPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "||", "Default is '||'");
        spoilerSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "||", "Default is '||'");
        isSpoilerEnabled = ConfigBuilder.buildBoolean(pBuilder, "spoilerEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Hyperlink");
        hyperlinkPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "[", "Default is '['");
        hyperlinkSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "]", "Default is ']'");
        isHyperlinkEnabled = ConfigBuilder.buildBoolean(pBuilder, "hyperlinkEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("Color");
        colorPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "-", "Default is '-'");
        colorSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "-", "Default is '-'");
        isColorEnabled = ConfigBuilder.buildBoolean(pBuilder, "colorEnabled", true, "Default is 'true'");
        pBuilder.pop(1);
        pBuilder.push("CopyToClipboard");
        isCopyToClipboardEnabled = ConfigBuilder.buildBoolean(pBuilder, "copyToClipboardEnabled", true, "Default is 'true'");
        pBuilder.pop(2);
    }

}
