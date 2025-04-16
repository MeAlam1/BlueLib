// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Spoiler extends MarkdownFeature {

    public Spoiler() {
        prefix = MarkdownConfig.spoilerPrefix;
        suffix = MarkdownConfig.spoilerSuffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent SpoilerText = Component.literal(pText)
                .setStyle(pOriginalStyle.withObfuscated(true));
        pResult.append(SpoilerText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isSpoilerEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Spoiler";
    }

    public static Boolean isSpoilerEnabled() {
        return MarkdownConfig.isSpoilerEnabled;
    }
}
