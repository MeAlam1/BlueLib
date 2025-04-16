// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Italic extends MarkdownFeature {

    protected static String Prefix = "*";

    protected static String Suffix = "*";

    public static Boolean isItalicEnabled = true;

    public Italic() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent italicText = Component.literal(pText)
                .setStyle(pOriginalStyle.withItalic(true));
        pResult.append(italicText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isItalicEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Italic";
    }

    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
    }

    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
    }

    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
    }

    public static String getPrefix() {
        return Prefix;
    }

    public static String getSuffix() {
        return Suffix;
    }

    public static boolean isItalicEnabled() {
        return isItalicEnabled;
    }
}
