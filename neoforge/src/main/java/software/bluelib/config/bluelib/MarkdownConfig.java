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

public class MarkdownConfig {

	@NotNull
	public final ModConfigSpec.BooleanValue isMarkdownEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> boldPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> boldSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isBoldEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> italicPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> italicSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isItalicEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> underlinePrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> underlineSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isUnderlineEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> strikethroughPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> strikethroughSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isStrikethroughEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> spoilerPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> spoilerSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isSpoilerEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> hyperlinkPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> hyperlinkSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isHyperlinkEnabled;

	@NotNull
	public final ModConfigSpec.ConfigValue<String> colorPrefix;
	@NotNull
	public final ModConfigSpec.ConfigValue<String> colorSuffix;
	@NotNull
	public final ModConfigSpec.BooleanValue isColorEnabled;

	@NotNull
	public final ModConfigSpec.BooleanValue isCopyToClipboardEnabled;

	public MarkdownConfig(@NotNull final ModConfigSpec.Builder pBuilder) {
		pBuilder.push("Markdown");
		isMarkdownEnabled = ConfigBuilder.buildBoolean(pBuilder, "markdownEnabled", true, "Default is 'true/on'");
		pBuilder.push("Bold");
		boldPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "**", "Default is '**'");
		boldSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "**", "Default is '**'");
		isBoldEnabled = ConfigBuilder.buildBoolean(pBuilder, "boldEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Italic");
		italicPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "*", "Default is '*'");
		italicSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "*", "Default is '*'");
		isItalicEnabled = ConfigBuilder.buildBoolean(pBuilder, "italicEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Underline");
		underlinePrefix = ConfigBuilder.buildString(pBuilder, "prefix", "__", "Default is '__'");
		underlineSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "__", "Default is '__'");
		isUnderlineEnabled = ConfigBuilder.buildBoolean(pBuilder, "underlineEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Strikethrough");
		strikethroughPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "~~", "Default is '~~'");
		strikethroughSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "~~", "Default is '~~'");
		isStrikethroughEnabled = ConfigBuilder.buildBoolean(pBuilder, "strikethroughEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Spoiler");
		spoilerPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "||", "Default is '||'");
		spoilerSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "||", "Default is '||'");
		isSpoilerEnabled = ConfigBuilder.buildBoolean(pBuilder, "spoilerEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Hyperlink");
		hyperlinkPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "[", "Default is '['");
		hyperlinkSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "]", "Default is ']'");
		isHyperlinkEnabled = ConfigBuilder.buildBoolean(pBuilder, "hyperlinkEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("Color");
		colorPrefix = ConfigBuilder.buildString(pBuilder, "prefix", "-", "Default is '-'");
		colorSuffix = ConfigBuilder.buildString(pBuilder, "suffix", "-", "Default is '-'");
		isColorEnabled = ConfigBuilder.buildBoolean(pBuilder, "colorEnabled", true, "Default is 'true/on'");
		pBuilder.pop(1);
		pBuilder.push("CopyToClipboard");
		isCopyToClipboardEnabled = ConfigBuilder.buildBoolean(pBuilder, "copyToClipboardEnabled", true, "Default is 'true/on'");
		pBuilder.pop(2);
	}
}
