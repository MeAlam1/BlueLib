/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.syntax.*;

@SuppressWarnings("unused")
public class MarkdownParser {

    public static MutableComponent parseMarkdown(Component pMessage) {
        if (!MarkdownConfig.isMarkdownEnabled) {
            BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("markdown.disabled"));
            return pMessage.copy();
        }

        String text = pMessage.getString();
        MutableComponent formattedMessage = Component.literal(text);

        formattedMessage = new Bold().apply(formattedMessage);
        formattedMessage = new Italic().apply(formattedMessage);
        formattedMessage = new Underline().apply(formattedMessage);
        formattedMessage = new Strikethrough().apply(formattedMessage);
        formattedMessage = new Spoiler().apply(formattedMessage);
        formattedMessage = new Hyperlink().apply(formattedMessage);
        formattedMessage = new Color().apply(formattedMessage);
        formattedMessage = new CopyToClipboard().apply(formattedMessage, text);

        BaseLogger.log(true, BaseLogLevel.INFO, BlueLibCommon.Translation.log("markdown.message"));
        return formattedMessage;
    }
}
