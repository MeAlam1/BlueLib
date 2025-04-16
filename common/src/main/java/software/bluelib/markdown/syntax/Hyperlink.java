// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.regex.Pattern;
import net.minecraft.network.chat.*;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.IsValidUtils;
import software.bluelib.utils.conversion.LinkUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class Hyperlink extends MarkdownFeature {

    protected static String Prefix = "[";

    protected static String Suffix = "]";

    public static Boolean isHyperlinkEnabled = true;

    public MutableComponent apply(MutableComponent pComponent) {
        if (!isHyperlinkEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Hyperlink formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(getPrefix()) + "(.*?)" + Pattern.quote(getSuffix()) + "\\((.*?)\\)");

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithHyperlinks(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithHyperlinks(pComponent, pattern);
        }

        return result;
    }

    protected void processComponentTextWithHyperlinks(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> {
                    String url = matcher.group(2);
                    if (url != null && !url.isEmpty()) {
                        appendHyperlink(matcher.group(1), url, pOriginalStyle, res);
                    }
                });
    }

    public MutableComponent processSiblingsWithHyperlinks(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithHyperlinks);
    }

    private void appendHyperlink(String pText, String pUrl, Style pOriginalStyle, MutableComponent pResult) {
        if (!IsValidUtils.isValidURL(pUrl)) {
            pResult.append(Component.literal(getPrefix() + pText + getSuffix() + "(" + pUrl + ")").setStyle(pOriginalStyle));
            return;
        }

        MutableComponent hyperlink = Component.literal(pText)
                .setStyle(pOriginalStyle
                        .withColor(TextColor.fromRgb(0x1F5FE1))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenUrl(LinkUtils.stringToUri(pUrl))));

        pResult.append(hyperlink);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isHyperlinkEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Hyperlink";
    }

    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix updated to: " + Prefix, true);
    }

    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink suffix updated to: " + Suffix, true);
    }

    public static String getPrefix() {
        return Prefix;
    }

    public static String getSuffix() {
        return Suffix;
    }

    public static Boolean isHyperlinkEnabled() {
        return isHyperlinkEnabled;
    }
}
