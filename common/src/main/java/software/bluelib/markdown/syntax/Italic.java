// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Italic extends MarkdownFeature {

    public Italic() {
        prefix = MarkdownConfig.italicPrefix;
        suffix = MarkdownConfig.italicSuffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent italicText = Component.literal(pText)
                .setStyle(pOriginalStyle.withItalic(true));
        pResult.append(italicText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isItalicEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Italic";
    }

    public static boolean isItalicEnabled() {
        return MarkdownConfig.isItalicEnabled;
    }
}
