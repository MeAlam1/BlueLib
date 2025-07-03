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
public class Spoiler extends MarkdownFeature {

	public Spoiler() {
		prefix = MarkdownConfig.spoilerPrefix;
		suffix = MarkdownConfig.spoilerSuffix;
	}

	@Override
	protected void appendFormattedText(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
		MutableComponent SpoilerText = Component.literal(pText)
				.setStyle(pOriginalStyle.withObfuscated(true));
		pResult.append(SpoilerText);
	}

	@Override
	protected @NotNull Boolean isFeatureEnabled() {
		return MarkdownConfig.isSpoilerEnabled;
	}

	@Override
	protected @NotNull String getFeatureName() {
		return "Spoiler";
	}

	public static Boolean isSpoilerEnabled() {
		return MarkdownConfig.isSpoilerEnabled;
	}
}
