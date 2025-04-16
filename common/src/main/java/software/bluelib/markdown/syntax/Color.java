// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import software.bluelib.config.MarkdownConfig;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.IsValidUtils;
import software.bluelib.utils.conversion.ColorConversionUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class Color extends MarkdownFeature {

    public MutableComponent apply(MutableComponent pComponent) {
        if (!MarkdownConfig.isColorEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Color formatting is disabled. Returning original content.", true);
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

    protected void processComponentTextWithColors(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> {
                    Object colors = extractColorsFromMatcher(matcher);
                    String gradientText = matcher.group(matcher.groupCount());

                    appendColor(gradientText, colors, pOriginalStyle, res);
                });
    }

    private Object extractColorsFromMatcher(Matcher matcher) {
        List<Integer> colors = new ArrayList<>();

        String colorGroup = matcher.group(1);
        if (colorGroup != null) {
            String[] colorArray = colorGroup.split(",");
            for (String color : colorArray) {
                if (IsValidUtils.isValidColor(color)) {
                    colors.add(ColorConversionUtils.parseColorToHexString(color));
                } else {
                    BaseLogger.log(BaseLogLevel.WARNING, "Invalid color detected: " + color, true);
                    return colorGroup;
                }
            }
        }

        BaseLogger.log(BaseLogLevel.INFO, "Extracted colors: " + colors, true);

        return colors;
    }

    private void appendColor(String pColorText, Object pColors, Style pOriginalStyle, MutableComponent pResult) {
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
            BaseLogger.log(BaseLogLevel.WARNING, "Invalid color list detected: " + pColors, true);
            pResult.append(Component.literal(MarkdownConfig.colorPrefix + pColors + MarkdownConfig.colorSuffix + "(" + pColorText + ")")
                    .setStyle(pOriginalStyle));
        }
    }

    private int interpolateColor(int startColor, int endColor, float ratio) {
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

    public MutableComponent processSiblingsWithColors(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithColors);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return MarkdownConfig.isColorEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Color";
    }

    public static Boolean isColorEnabled() {
        return MarkdownConfig.isColorEnabled;
    }
}
