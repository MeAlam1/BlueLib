/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.api.utils.QuadConsumer;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.internal.Translation;

public abstract class MarkdownFeature {

    protected String prefix;

    protected String suffix;

    public MutableComponent apply(MutableComponent pComponent) {
        if (!isFeatureEnabled()) {
            BaseLogger.log(true, BaseLogLevel.INFO, Translation.log("markdown.feature.disabled", getFeatureName()));
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix));
        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithFormatting(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithFormatting(pComponent, pattern);
        }

        return result;
    }

    protected void processComponentText(
            String pText,
            Style pOriginalStyle,
            MutableComponent pResult,
            Pattern pPattern,
            BiConsumer<Matcher, MutableComponent> pSpecialTextHandler) {
        Matcher matcher = pPattern.matcher(pText);
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.group(1).isEmpty()) {
                appendUnstyledText(pText.substring(lastIndex, matcher.end()), pResult, pOriginalStyle);
            } else if (matcher.start() > 0 && pText.charAt(matcher.start() - 1) == '\\') {
                appendUnstyledText(pText.substring(lastIndex, matcher.start() - 1), pResult, pOriginalStyle);
                appendUnstyledText(matcher.group(0), pResult, pOriginalStyle);
            } else {
                appendUnstyledText(pText.substring(lastIndex, matcher.start()), pResult, pOriginalStyle);
                pSpecialTextHandler.accept(matcher, pResult);
            }
            lastIndex = matcher.end();
        }

        appendUnstyledText(pText.substring(lastIndex), pResult, pOriginalStyle);
    }

    protected void processComponentTextWithFormatting(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> appendFormattedText(matcher.group(1), pOriginalStyle, res));
    }

    protected MutableComponent processSiblings(
            MutableComponent pComponent,
            Pattern pPattern,
            QuadConsumer<String, Style, MutableComponent, Pattern> pSiblingProcessor) {
        MutableComponent result = Component.empty();

        for (Component sibling : pComponent.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                pSiblingProcessor.accept(
                        mutableSibling.getString(),
                        mutableSibling.getStyle(),
                        result,
                        pPattern);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    protected MutableComponent processSiblingsWithFormatting(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithFormatting);
    }

    protected void appendFormattedText(String pText, Style pStyle, MutableComponent pResult) {
        pResult.append(Component.literal(pText).setStyle(pStyle));
    }

    protected void appendUnstyledText(String pText, MutableComponent pResult, Style pOriginalStyle) {
        pResult.append(Component.literal(pText).setStyle(pOriginalStyle));
    }

    protected abstract boolean isFeatureEnabled();

    protected abstract String getFeatureName();
}
