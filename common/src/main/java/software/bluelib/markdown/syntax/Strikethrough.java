// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class Strikethrough extends MarkdownFeature {

    protected static String Prefix = "~~";

    protected static String Suffix = "~~";

    public static Boolean isStrikethroughEnabled = true;

    public Strikethrough() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent StrikethroughText = Component.literal(pText)
                .setStyle(pOriginalStyle.withStrikethrough(true));
        pResult.append(StrikethroughText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isStrikethroughEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Strikethrough";
    }

    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix updated to: " + Prefix, true);
    }

    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough suffix updated to: " + Suffix, true);
    }

    public static String getPrefix() {
        return Prefix;
    }

    public static String getSuffix() {
        return Suffix;
    }

    public static boolean isStrikethroughEnabled() {
        return isStrikethroughEnabled;
    }
}
