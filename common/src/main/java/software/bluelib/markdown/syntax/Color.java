// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.*;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.conversion.ColorConversionUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the Color Markdown formatting feature.
 * <p>
 * This class applies Color formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for Color text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyComponent(String)} - Applies Color formatting to the provided message.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix used for Color formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix used for Color formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix used for Color formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix used for Color formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix used for Color formatting.</li>
 * <li>{@link #isColorEnabled()} - Retrieves whether Color formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MarkdownFeature
 * @since 1.6.0
 */
public class Color extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Color formatting.
     *
     * @since 1.6.0
     */
    protected static String Prefix = "-";

    /**
     * A {@code protected static} field representing the default suffix for Color formatting.
     *
     * @since 1.6.0
     */
    protected static String Suffix = "-";

    /**
     * A {@code protected static} field that determines whether Color formatting is enabled.
     *
     * @since 1.6.0
     */
    public static Boolean isColorEnabled = true;

    /**
     * A {@code public} {@link MutableComponent} that applies Color formatting to the provided message.
     * <p>
     * This method applies Color formatting to the provided message, <br>
     * if the message contains a valid URL surrounded by the prefix and suffix. <br>
     * The method returns the formatted message with the Color applied, or the original message if no valid URL is found.
     * </p>
     *
     * @param pComponent {@link MutableComponent} - The message to format.
     * @return {@link MutableComponent} - The formatted message with Color Markdown applied, or the original message.
     * @author MeAlam
     * @since 1.6.0
     */
    public MutableComponent apply(MutableComponent pComponent) {
        if (!isColorEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Color formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        // Updated pattern to match color code and text inside parentheses
        Pattern pattern = Pattern.compile(Pattern.quote(getPrefix()) + "(#[0-9A-Fa-f]{6})" + Pattern.quote(getSuffix()) + "\\((.*?)\\)");

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithColors(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithColors(pComponent, pattern);
        }

        return result;
    }

    protected void processComponentTextWithColors(String text, Style originalStyle, MutableComponent result, Pattern pattern) {
        processComponentText(text, originalStyle, result, pattern,
                (matcher, res) -> {
                    String color = matcher.group(1);
                    String colorText = matcher.group(2);
                    if (color != null && !color.isEmpty()) {
                        appendColor(colorText, color, originalStyle, res);
                    }
                });
    }

    private void appendColor(String colorText, String pColor, Style originalStyle, MutableComponent result) {
        if (ColorConversionUtils.isValidColor(pColor)) {
            result.append(Component.literal(colorText)
                    .setStyle(originalStyle.withColor(TextColor.fromRgb(ColorConversionUtils.parseColorToHexString(pColor)))));
        } else {
            result.append(Component.literal(colorText).setStyle(originalStyle));
        }
    }


    public MutableComponent processSiblingsWithColors(MutableComponent component, Pattern pattern) {
        return processSiblings(component, pattern,
                this::processComponentTextWithColors);
    }

    @Override
    protected void appendFormattedText(String text, Style originalStyle, MutableComponent result) {
        // Due to the nature of the Color feature, this method is not used.
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isColorEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Color";
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for Color formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Color formatting.
     * @param pSuffix {@link String} - The new suffix for Color formatting.
     * @author MeAlam
     * @since 1.6.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for Color formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Color formatting.
     * @author MeAlam
     * @since 1.6.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Color formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Color formatting.
     * @author MeAlam
     * @since 1.6.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Color formatting.
     *
     * @return The current prefix for Color formatting.
     * @author MeAlam
     * @since 1.6.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Color formatting.
     *
     * @return The current suffix for Color formatting.
     * @author MeAlam
     * @since 1.6.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether Color formatting is enabled.
     *
     * @return {@code true} if Color formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.6.0
     */
    public static Boolean isColorEnabled() {
        return isColorEnabled;
    }
}
