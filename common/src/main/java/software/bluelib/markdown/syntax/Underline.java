// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class Underline extends MarkdownFeature {

    protected static String Prefix = "__";

    protected static String Suffix = "__";

    public static Boolean isUnderlineEnabled = true;

    public Underline() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent UnderlineText = Component.literal(pText)
                .setStyle(pOriginalStyle.withUnderlined(true));
        pResult.append(UnderlineText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isUnderlineEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Underline";
    }

    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline prefix updated to: " + Prefix, true);
    }

    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline suffix updated to: " + Suffix, true);
    }

    public static String getPrefix() {
        return Prefix;
    }

    public static String getSuffix() {
        return Suffix;
    }

    public static Boolean isUnderlineEnabled() {
        return isUnderlineEnabled;
    }
}
