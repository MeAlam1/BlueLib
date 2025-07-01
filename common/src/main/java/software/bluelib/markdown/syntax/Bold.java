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
import org.jetbrains.annotations.NotNull;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Bold extends MarkdownFeature {

    public Bold() {
        prefix = MarkdownConfig.boldPrefix;
        suffix = MarkdownConfig.boldSuffix;
    }

    @Override
    protected void appendFormattedText(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
        MutableComponent boldText = Component.literal(pText)
                .setStyle(pOriginalStyle.withBold(true));
        pResult.append(boldText);
    }

    @Override
    protected @NotNull Boolean isFeatureEnabled() {
        return MarkdownConfig.isBoldEnabled;
    }

    @Override
    protected @NotNull String getFeatureName() {
        return "Bold";
    }

    public static Boolean isBoldEnabled() {
        return MarkdownConfig.isBoldEnabled;
    }
}
