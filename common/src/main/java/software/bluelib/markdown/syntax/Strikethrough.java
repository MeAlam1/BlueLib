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
public class Strikethrough extends MarkdownFeature {

	public Strikethrough() {
		prefix = MarkdownConfig.strikethroughPrefix;
		suffix = MarkdownConfig.strikethroughSuffix;
	}

	@Override
	protected void appendFormattedText(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
		MutableComponent strikethroughText = Component.literal(pText)
				.setStyle(pOriginalStyle.withStrikethrough(true));
		pResult.append(strikethroughText);
	}

	@Override
	protected @NotNull Boolean isFeatureEnabled() {
		return MarkdownConfig.isStrikethroughEnabled;
	}

	@Override
	protected @NotNull String getFeatureName() {
		return "Strikethrough";
	}

	public static boolean isStrikethroughEnabled() {
		return MarkdownConfig.isStrikethroughEnabled;
	}
}
