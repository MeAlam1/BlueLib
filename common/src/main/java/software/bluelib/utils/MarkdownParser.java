package software.bluelib.utils;

import net.minecraft.network.chat.Component;

public class MarkdownParser {
    private static boolean globalMarkdownEnabled = true;

    // Individual feature switches
    private static boolean boldEnabled = true;
    private static boolean italicEnabled = true;
    private static boolean strikethroughEnabled = true;

    public static Component parseMarkdown(Component pMessage) {
        if (!globalMarkdownEnabled) {
            return pMessage;
        }

        String text = pMessage.getString();

        if (boldEnabled) {
            text = applyBold(text);
        }
        if (italicEnabled) {
            text = applyItalic(text);
        }
        if (strikethroughEnabled) {
            text = applyStrikethrough(text);
        }

        return Component.literal(text);
    }

    // Bold
    private static String applyBold(String pMessage) {
        return pMessage.replaceAll("\\*\\*(.*?)\\*\\*", "§l$1§r");
    }

    // Italic
    private static String applyItalic(String pMessage) {
        return pMessage.replaceAll("\\*(.*?)\\*", "§o$1§r");
    }

    // Strikethrough
    private static String applyStrikethrough(String pMessage) {
        return pMessage.replaceAll("~~(.*?)~~", "§m$1§r");
    }

    public static void setGlobalMarkdownEnabled(boolean pEnabled) {
        globalMarkdownEnabled = pEnabled;
    }

    public static boolean isGlobalMarkdownEnabled() {
        return globalMarkdownEnabled;
    }

    public static void setBoldEnabled(boolean pEnabled) {
        boldEnabled = pEnabled;
    }

    public static boolean isBoldEnabled() {
        return boldEnabled;
    }

    public static void setItalicEnabled(boolean pEnabled) {
        italicEnabled = pEnabled;
    }

    public static boolean isItalicEnabled() {
        return italicEnabled;
    }

    public static void setStrikethroughEnabled(boolean pEnabled) {
        strikethroughEnabled = pEnabled;
    }

    public static boolean isStrikethroughEnabled() {
        return strikethroughEnabled;
    }
}
