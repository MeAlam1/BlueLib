/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.QuadConsumer;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.BlueTranslation;

public abstract class MarkdownFeature {

	@Nullable
	protected String prefix;

	@Nullable
	protected String suffix;

	@NotNull
	public MutableComponent apply(@NotNull MutableComponent pComponent) {
		if (!isFeatureEnabled()) {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("markdown.feature.disabled", getFeatureName()));
			return pComponent;
		}

		if (prefix == null || suffix == null) {
			BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("markdown.feature.prefix_suffix_not_set", getFeatureName()));
			return pComponent;
		}
		Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix));
		MutableComponent result = Component.empty();

		if (pComponent.getSiblings().isEmpty()) {
			processComponentTextWithFormatting(pComponent.getString(), pComponent.getStyle(), result, pattern);
		} else {
			result = processSiblingsWithFormatting(pComponent, pattern);
		}

		return result;
	}

	protected void processComponentText(
			@NotNull String pText,
			@NotNull Style pOriginalStyle,
			@NotNull MutableComponent pResult,
			@NotNull Pattern pPattern,
			@NotNull BiConsumer<Matcher, MutableComponent> pSpecialTextHandler) {
		Matcher matcher = pPattern.matcher(pText);
		int lastIndex = 0;

		while (matcher.find()) {
			if (matcher.group(1).isEmpty()) {
				appendUnstyledText(pText.substring(lastIndex, matcher.end()), pResult, pOriginalStyle);
			} else if (matcher.start() > 0 && pText.charAt(matcher.start() - 1) == '\\') {
				appendUnstyledText(pText.substring(lastIndex, matcher.start() - 1), pResult, pOriginalStyle);
				appendUnstyledText(matcher.group(0), pResult, pOriginalStyle);
			} else {
				appendUnstyledText(pText.substring(lastIndex, matcher.start()), pResult, pOriginalStyle);
				pSpecialTextHandler.accept(matcher, pResult);
			}
			lastIndex = matcher.end();
		}

		appendUnstyledText(pText.substring(lastIndex), pResult, pOriginalStyle);
	}

	protected void processComponentTextWithFormatting(
			@NotNull String pText,
			@NotNull Style pOriginalStyle,
			@NotNull MutableComponent pResult,
			@NotNull Pattern pPattern) {
		processComponentText(pText, pOriginalStyle, pResult, pPattern,
				(matcher, res) -> appendFormattedText(matcher.group(1), pOriginalStyle, res));
	}

	@NotNull
	protected MutableComponent processSiblings(
			@NotNull MutableComponent pComponent,
			@NotNull Pattern pPattern,
			@NotNull QuadConsumer<String, Style, MutableComponent, Pattern> pSiblingProcessor) {
		MutableComponent result = Component.empty();

		for (Component sibling : pComponent.getSiblings()) {
			if (sibling instanceof MutableComponent mutableSibling) {
				pSiblingProcessor.accept(
						mutableSibling.getString(),
						mutableSibling.getStyle(),
						result,
						pPattern);
			} else {
				result.append(sibling);
			}
		}

		return result;
	}

	@NotNull
	protected MutableComponent processSiblingsWithFormatting(@NotNull MutableComponent pComponent, @NotNull Pattern pPattern) {
		return processSiblings(pComponent, pPattern,
				this::processComponentTextWithFormatting);
	}

	protected void appendFormattedText(@NotNull String pText, @NotNull Style pStyle, @NotNull MutableComponent pResult) {
		pResult.append(Component.literal(pText).setStyle(pStyle));
	}

	protected void appendUnstyledText(@NotNull String pText, @NotNull MutableComponent pResult, @NotNull Style pOriginalStyle) {
		pResult.append(Component.literal(pText).setStyle(pOriginalStyle));
	}

	@NotNull
	protected abstract Boolean isFeatureEnabled();

	@NotNull
	protected abstract String getFeatureName();
}
