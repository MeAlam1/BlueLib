// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.syntax.*;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class MarkdownParser {

    public static MutableComponent parseMarkdown(Component pMessage) {
        if (!MarkdownConfig.isMarkdownEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Global markdown is disabled, returning original message", true);
            return pMessage.copy();
        }

        String text = pMessage.getString();
        MutableComponent formattedMessage = Component.literal(text);

        formattedMessage = new Bold().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Bold: " + formattedMessage, true);
        formattedMessage = new Italic().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Italic: " + formattedMessage, true);
        formattedMessage = new Underline().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Underline: " + formattedMessage, true);
        formattedMessage = new Strikethrough().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Strikethrough: " + formattedMessage, true);
        formattedMessage = new Spoiler().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Spoiler: " + formattedMessage, true);
        formattedMessage = new Hyperlink().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Hyperlink: " + formattedMessage, true);
        formattedMessage = new Color().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Color: " + formattedMessage, true);
        formattedMessage = new CopyToClipboard().apply(formattedMessage, text);

        BaseLogger.log(BaseLogLevel.INFO, "Completed Message: " + formattedMessage, true);
        return formattedMessage;
    }
}
