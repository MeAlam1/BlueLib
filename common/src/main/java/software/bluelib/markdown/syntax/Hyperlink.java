/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import java.util.regex.Pattern;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.IsValidUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Hyperlink extends MarkdownFeature {

	public @NotNull MutableComponent apply(@NotNull MutableComponent pComponent) {
		if (!MarkdownConfig.isHyperlinkEnabled) {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("markdown.hyperlink.disabled"));
			return pComponent;
		}

		Pattern pattern = Pattern.compile(Pattern.quote(MarkdownConfig.hyperlinkPrefix) + "(.*?)" + Pattern.quote(MarkdownConfig.hyperlinkSuffix) + "\\((.*?)\\)");

		MutableComponent result = Component.empty();

		if (pComponent.getSiblings().isEmpty()) {
			processComponentTextWithHyperlinks(pComponent.getString(), pComponent.getStyle(), result, pattern);
		} else {
			result = processSiblingsWithHyperlinks(pComponent, pattern);
		}

		return result;
	}

	protected void processComponentTextWithHyperlinks(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult, @NotNull Pattern pPattern) {
		processComponentText(pText, pOriginalStyle, pResult, pPattern,
				(matcher, res) -> {
					String url = matcher.group(2);
					if (url != null && !url.isEmpty()) {
						appendHyperlink(matcher.group(1), url, pOriginalStyle, res);
					}
				});
	}

	@NotNull
	public MutableComponent processSiblingsWithHyperlinks(@NotNull MutableComponent pComponent, @NotNull Pattern pPattern) {
		return processSiblings(pComponent, pPattern,
				this::processComponentTextWithHyperlinks);
	}

	private void appendHyperlink(@NotNull String pText, @NotNull String pUrl, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
		if (!IsValidUtils.isValidURL(pUrl)) {
			pResult.append(Component.literal(MarkdownConfig.hyperlinkPrefix + pText + MarkdownConfig.hyperlinkSuffix + "(" + pUrl + ")").setStyle(pOriginalStyle));
			return;
		}

		MutableComponent hyperlink = Component.literal(pText)
				.setStyle(pOriginalStyle
						.withColor(TextColor.fromRgb(0x1F5FE1))
						.withUnderlined(true)
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, pUrl)));

		pResult.append(hyperlink);
	}

	@Override
	protected @NotNull Boolean isFeatureEnabled() {
		return MarkdownConfig.isHyperlinkEnabled;
	}

	@Override
	protected @NotNull String getFeatureName() {
		return "Hyperlink";
	}
}
