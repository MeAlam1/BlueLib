// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Strikethrough extends MarkdownFeature {

    public Strikethrough() {
        prefix = MarkdownConfig.strikethroughPrefix;
        suffix = MarkdownConfig.strikethroughSuffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent StrikethroughText = Component.literal(pText)
                .setStyle(pOriginalStyle.withStrikethrough(true));
        pResult.append(StrikethroughText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isStrikethroughEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Strikethrough";
    }

    public static boolean isStrikethroughEnabled() {
        return MarkdownConfig.isStrikethroughEnabled;
    }
}
