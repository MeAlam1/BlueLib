// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Spoiler extends MarkdownFeature {

    protected static String Prefix = "||";

    protected static String Suffix = "||";

    public static Boolean isSpoilerEnabled = true;

    public Spoiler() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent SpoilerText = Component.literal(pText)
                .setStyle(pOriginalStyle.withObfuscated(true));
        pResult.append(SpoilerText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isSpoilerEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Spoiler";
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

    public static Boolean isSpoilerEnabled() {
        return isSpoilerEnabled;
    }
}
