/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class CopyToClipboard extends MarkdownFeature {

	@NotNull
	public MutableComponent apply(@NotNull MutableComponent pMessage, @NotNull String pTextToCopy) {
		if (!MarkdownConfig.isCopyToClipboardEnabled) {
			BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("markdown.copyToClipboard.disabled"));
			return pMessage;
		}
		MutableComponent result = Component.empty();

		for (Component sibling : pMessage.getSiblings()) {
			if (sibling instanceof MutableComponent mutableSibling) {
				if (mutableSibling.getStyle().getClickEvent() == null) {
					mutableSibling.setStyle(mutableSibling.getStyle()
							.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pTextToCopy)));
				}
				result.append(mutableSibling);
			} else {
				result.append(sibling);
			}
		}

		return result;
	}

	/**
	 * @return true if the CopyToClipboard feature is enabled, false otherwise.
	 * @deprecated Use {@link CopyToClipboard#isFeatureEnabled} instead.
	 */
	@NotNull
	@Deprecated(forRemoval = true, since = "2.2.0")
	public static Boolean isCopyToClipboardEnabled() {
		return MarkdownConfig.isCopyToClipboardEnabled;
	}

	@Override
	protected @NotNull Boolean isFeatureEnabled() {
		return MarkdownConfig.isCopyToClipboardEnabled;
	}

	@Override
	protected @NotNull String getFeatureName() {
		return "CopyToClipboard";
	}
}
