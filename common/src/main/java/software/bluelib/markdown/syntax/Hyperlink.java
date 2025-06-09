/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import java.util.regex.Pattern;
import net.minecraft.network.chat.*;
import software.bluelib.api.utils.IsValidUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.internal.Translation;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Hyperlink extends MarkdownFeature {

    public MutableComponent apply(MutableComponent pComponent) {
        if (!MarkdownConfig.isHyperlinkEnabled) {
            BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("markdown.hyperlink.disabled"));
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(MarkdownConfig.hyperlinkPrefix) + "(.*?)" + Pattern.quote(MarkdownConfig.hyperlinkSuffix) + "\\((.*?)\\)");

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
            pResult.append(Component.literal(MarkdownConfig.hyperlinkPrefix + pText + MarkdownConfig.hyperlinkSuffix + "(" + pUrl + ")").setStyle(pOriginalStyle));
            return;
        }

        MutableComponent hyperlink = Component.literal(pText)
                .setStyle(pOriginalStyle
                        .withColor(TextColor.fromRgb(0x1F5FE1))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, pUrl)));

        pResult.append(hyperlink);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isHyperlinkEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Hyperlink";
    }

    public static Boolean isHyperlinkEnabled() {
        return MarkdownConfig.isHyperlinkEnabled;
    }
}
