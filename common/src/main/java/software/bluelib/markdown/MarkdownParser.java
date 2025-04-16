// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.markdown.syntax.*;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class MarkdownParser {

    private static boolean globalMarkdownEnabled = true;

    public static MutableComponent parseMarkdown(Component pMessage) {
        if (!globalMarkdownEnabled) {
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

    public static void enableMarkdown() {
        globalMarkdownEnabled = true;
        BaseLogger.log(BaseLogLevel.INFO, "Global markdown enabled", true);
    }

    public static void disableMarkdown() {
        globalMarkdownEnabled = false;
        BaseLogger.log(BaseLogLevel.INFO, "Global markdown disabled", true);
    }

    public static EnableMarkdownFor enableMarkdownFor() {
        BaseLogger.log(BaseLogLevel.INFO, "Returning EnableMarkdownFor instance", true);
        return new EnableMarkdownFor();
    }

    public static DisableMarkdownFor disableMarkdownFor() {
        BaseLogger.log(BaseLogLevel.INFO, "Returning DisableMarkdownFor instance", true);
        return new DisableMarkdownFor();
    }

    public static class EnableMarkdownFor {

        public EnableMarkdownFor bold() {
            Bold.isBoldEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled bold markdown", true);
            return this;
        }

        public EnableMarkdownFor italic() {
            Italic.isItalicEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled italic markdown", true);
            return this;
        }

        public EnableMarkdownFor strikethrough() {
            Strikethrough.isStrikethroughEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled strikethrough markdown", true);
            return this;
        }

        public EnableMarkdownFor underline() {
            Underline.isUnderlineEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled underline markdown", true);
            return this;
        }

        public EnableMarkdownFor hyperlink() {
            Hyperlink.isHyperlinkEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled hyperlink markdown", true);
            return this;
        }

        public EnableMarkdownFor spoiler() {
            Spoiler.isSpoilerEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled spoiler markdown", true);
            return this;
        }

        public EnableMarkdownFor copyToClipboard() {
            CopyToClipboard.isCopyToClipboardEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled copy-to-clipboard markdown", true);
            return this;
        }

        public EnableMarkdownFor color() {
            Color.isColorEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled color markdown", true);
            return this;
        }
    }

    public static class DisableMarkdownFor {

        public DisableMarkdownFor bold() {
            Bold.isBoldEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled bold markdown", true);
            return this;
        }

        public DisableMarkdownFor italic() {
            Italic.isItalicEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled italic markdown", true);
            return this;
        }

        public DisableMarkdownFor strikethrough() {
            Strikethrough.isStrikethroughEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled strikethrough markdown", true);
            return this;
        }

        public DisableMarkdownFor underline() {
            Underline.isUnderlineEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled underline markdown", true);
            return this;
        }

        public DisableMarkdownFor hyperlink() {
            Hyperlink.isHyperlinkEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled hyperlink markdown", true);
            return this;
        }

        public DisableMarkdownFor spoiler() {
            Spoiler.isSpoilerEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled spoiler markdown", true);
            return this;
        }

        public DisableMarkdownFor copyToClipboard() {
            CopyToClipboard.isCopyToClipboardEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled copy-to-clipboard markdown", true);
            return this;
        }

        public DisableMarkdownFor color() {
            Color.isColorEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled color markdown", true);
            return this;
        }
    }
}
