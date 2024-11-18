package software.bluelib.utils.markdown;

import net.minecraft.network.chat.Component;

public class MarkdownParser {
    private static boolean globalMarkdownEnabled = true;

    private static String escapeRegex(String pInput) {
        return pInput.replaceAll("([\\\\*+\\[\\](){}|.^$?])", "\\\\$1");
    }

    public static Component parseMarkdown(Component pMessage) {
        if (!globalMarkdownEnabled) {
            return pMessage;
        }

        String text = pMessage.getString();
        text = new Bold().apply(text);
        text = new Italic().apply(text);
        text = new Strikethrough().apply(text);
        text = new Underline().apply(text);
        text = new Hyperlink().apply(text);
        return Component.literal(text);
    }

    public static void enableMarkdown() {
        globalMarkdownEnabled = true;
    }

    public static void disableMarkdown() {
        globalMarkdownEnabled = false;
    }

    public static EnableMarkdownFor enableMarkdownFor() {
        return new EnableMarkdownFor();
    }

    public static DisableMarkdownFor disableMarkdownFor() {
        return new DisableMarkdownFor();
    }

    public static class EnableMarkdownFor {
        public EnableMarkdownFor bold() {
            new Bold().enable();
            return this;
        }

        public EnableMarkdownFor italic() {
            new Italic().enable();
            return this;
        }

        public EnableMarkdownFor strikethrough() {
            new Strikethrough().enable();
            return this;
        }

        public EnableMarkdownFor underline() {
            new Underline().enable();
            return this;
        }

        public EnableMarkdownFor hyperlink() {
            new Hyperlink().enable();
            return this;
        }
    }

    public static class DisableMarkdownFor {
        public DisableMarkdownFor bold() {
            new Bold().disable();
            return this;
        }

        public DisableMarkdownFor italic() {
            new Italic().disable();
            return this;
        }

        public DisableMarkdownFor strikethrough() {
            new Strikethrough().disable();
            return this;
        }

        public DisableMarkdownFor underline() {
            new Underline().disable();
            return this;
        }

        public DisableMarkdownFor hyperlink() {
            new Hyperlink().disable();
            return this;
        }
    }
}
