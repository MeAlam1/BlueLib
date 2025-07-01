/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.markdown.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.utils.IsValidUtils;
import software.bluelib.api.utils.conversion.ColorConverterUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.internal.BlueTranslation;
import software.bluelib.markdown.MarkdownFeature;

@SuppressWarnings("unused")
public class Color extends MarkdownFeature {

    public @NotNull MutableComponent apply(@NotNull MutableComponent pComponent) {
        if (!MarkdownConfig.isColorEnabled) {
            BaseLogger.log(true, BaseLogLevel.INFO, BlueTranslation.log("markdown.color.disabled"));
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(MarkdownConfig.colorPrefix) +
                "#([0-9A-Fa-f]{6}(?:,#([0-9A-Fa-f]{6}))*)" +
                Pattern.quote(MarkdownConfig.colorSuffix) + "\\((.*?)\\)");

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithColors(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithColors(pComponent, pattern);
        }

        return result;
    }

    protected void processComponentTextWithColors(@NotNull String pText, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult, @NotNull Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> {
                    Object colors = extractColorsFromMatcher(matcher);
                    String gradientText = matcher.group(matcher.groupCount());

                    appendColor(gradientText, colors, pOriginalStyle, res);
                });
    }

    @NotNull
    private Object extractColorsFromMatcher(@NotNull Matcher matcher) {
        List<Integer> colors = new ArrayList<>();

        String colorGroup = matcher.group(1);
        if (colorGroup != null) {
            String[] colorArray = colorGroup.split(",");
            for (String color : colorArray) {
                if (IsValidUtils.isValidColor(color)) {
                    colors.add(ColorConverterUtils.parseColorToHexString(color));
                } else {
                    BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("markdown.color.invalid", color));
                    return colorGroup;
                }
            }
        }
        return colors;
    }

    @SuppressWarnings("unchecked")
    private void appendColor(@NotNull String pColorText, @NotNull Object pColors, @NotNull Style pOriginalStyle, @NotNull MutableComponent pResult) {
        if (pColors instanceof String) {
            pResult.append(Component.literal(MarkdownConfig.colorPrefix + pColors + MarkdownConfig.colorSuffix + "(" + pColorText + ")")
                    .setStyle(pOriginalStyle));
            return;
        }

        if (pColors instanceof List<?> colorsList && !colorsList.isEmpty() && colorsList.getFirst() instanceof Integer) {
            List<Integer> colors = (List<Integer>) colorsList;

            if (colors.size() == 1) {
                int color = colors.getFirst();
                pResult.append(Component.literal(pColorText).setStyle(pOriginalStyle.withColor(TextColor.fromRgb(color))));
                return;
            }

            char[] characters = pColorText.toCharArray();
            int textLength = characters.length;
            int colorCount = colors.size();
            int segmentLength = textLength / (colorCount - 1);
            int remainder = textLength % (colorCount - 1);

            int charIndex = 0;

            for (int colorIndex = 0; colorIndex < colorCount - 1; colorIndex++) {
                int startColor = colors.get(colorIndex);
                int endColor = colors.get(colorIndex + 1);

                int currentSegmentLength = segmentLength + (colorIndex < remainder ? 1 : 0);

                for (int i = 0; i < currentSegmentLength && charIndex < textLength; i++, charIndex++) {
                    float positionRatio = (float) i / (currentSegmentLength - 1);
                    int interpolatedColor = interpolateColor(startColor, endColor, positionRatio);

                    pResult.append(Component.literal(String.valueOf(characters[charIndex]))
                            .setStyle(pOriginalStyle.withColor(TextColor.fromRgb(interpolatedColor))));
                }
            }
        } else {
            BaseLogger.log(true, BaseLogLevel.WARNING, BlueTranslation.log("markdown.color.list.invalid", pColors));
            pResult.append(Component.literal(MarkdownConfig.colorPrefix + pColors + MarkdownConfig.colorSuffix + "(" + pColorText + ")")
                    .setStyle(pOriginalStyle));
        }
    }

    @NotNull
    private Integer interpolateColor(@NotNull Integer startColor, @NotNull Integer endColor, @NotNull Float ratio) {
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int r = (int) (startR + (endR - startR) * ratio);
        int g = (int) (startG + (endG - startG) * ratio);
        int b = (int) (startB + (endB - startB) * ratio);

        return (r << 16) | (g << 8) | b;
    }

    @NotNull
    public MutableComponent processSiblingsWithColors(@NotNull MutableComponent pComponent, @NotNull Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithColors);
    }

    @Override
    protected @NotNull Boolean isFeatureEnabled() {
        return MarkdownConfig.isColorEnabled;
    }

    @Override
    protected @NotNull String getFeatureName() {
        return "Color";
    }

    /**
     * @return true if the color feature is enabled, false otherwise.
     * @deprecated Use {@link Color#isFeatureEnabled} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "2.2.0")
    public static Boolean isColorEnabled() {
        return MarkdownConfig.isColorEnabled;
    }
}
