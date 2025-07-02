/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Italic extends MarkdownFeature {

	public Italic() {
		prefix = MarkdownConfig.italicPrefix;
		suffix = MarkdownConfig.italicSuffix;
	}

	@Override
	protected void appendFormattedText(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
		MutableComponent italicText = Component.literal(pText)
				.setStyle(pOriginalStyle.withItalic(true));
		pResult.append(italicText);
	}

	@Override
	protected @NotNull Boolean isFeatureEnabled() {
		return MarkdownConfig.isItalicEnabled;
	}

	@Override
	protected @NotNull String getFeatureName() {
		return "Italic";
	}

	public static boolean isItalicEnabled() {
		return MarkdownConfig.isItalicEnabled;
	}
}
