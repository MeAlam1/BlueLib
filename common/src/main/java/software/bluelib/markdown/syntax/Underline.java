// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class Underline extends MarkdownFeature {

    public Underline() {
        prefix = MarkdownConfig.underlinePrefix;
        suffix = MarkdownConfig.underlineSuffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent UnderlineText = Component.literal(pText)
                .setStyle(pOriginalStyle.withUnderlined(true));
        pResult.append(UnderlineText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isUnderlineEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Underline";
    }
    
    public static Boolean isUnderlineEnabled() {
        return MarkdownConfig.isUnderlineEnabled;
    }
}
