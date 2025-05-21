/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

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
