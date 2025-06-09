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
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.internal.Translation;

@SuppressWarnings("unused")
public class CopyToClipboard {

    public MutableComponent apply(MutableComponent pMessage, String pTextToCopy) {
        if (!MarkdownConfig.isCopyToClipboardEnabled) {
            BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("markdown.copyToClipboard.disabled"));
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

    public static Boolean isCopyToClipboardEnabled() {
        return MarkdownConfig.isCopyToClipboardEnabled;
    }
}
