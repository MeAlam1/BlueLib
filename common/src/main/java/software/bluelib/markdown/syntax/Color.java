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
     * A {@code public} constructor that initializes the prefix and suffix for the Color formatting feature.
     * <p>
     * The constructor sets the instance prefix and suffix to match the static Prefix and Suffix values.
     * </p>
     *
     * @author MeAlam
     * @since 1.6.0
     */
    public Color() {
        prefix = Prefix;
        suffix = Suffix;
    }

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
    public MutableComponent applyColor(MutableComponent pComponent) {
        if (!isColorEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Color formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        MutableComponent result = Component.empty();
        BaseLogger.log(BaseLogLevel.INFO, "Starting to process component: " + pComponent.getString(), true);

        Pattern pattern = Pattern.compile(
                prefix + "(#?[0-9A-Fa-f]{6}|\\d{1,3}(?:,\\d{1,3}){2,3})" + suffix + "\\((.*?)\\)");

        for (Component sibling : pComponent.getSiblings()) {
            BaseLogger.log(BaseLogLevel.INFO, "Processing sibling: " + sibling.getString(), true);

            if (sibling instanceof MutableComponent mutableSibling) {
                String siblingText = mutableSibling.getString();
                BaseLogger.log(BaseLogLevel.INFO, "Sibling text: " + siblingText, true);

                Matcher matcher = pattern.matcher(siblingText);
                MutableComponent styledSibling = Component.empty();

                int lastIndex = 0;

                while (matcher.find()) {
                    String beforeMatch = siblingText.substring(lastIndex, matcher.start());

                    if (beforeMatch.endsWith("\\")) {
                        BaseLogger.log(BaseLogLevel.INFO, "Escape sequence detected before prefix. Skipping markdown application.", true);

                        styledSibling.append(Component.literal(beforeMatch.substring(0, beforeMatch.length() - 1)));

                        styledSibling.append(Component.literal(matcher.group(0)));

                        lastIndex = matcher.end();
                        continue;
                    }

                    String color = matcher.group(1).trim();
                    String text = matcher.group(2).trim();

                    BaseLogger.log(BaseLogLevel.INFO, "Found match: color=" + color + ", text=" + text, true);

                    if (ColorConversionUtils.isValidColor(color)) {
                        int colorConverted = ColorConversionUtils.parseColorToHexString(color);
                        BaseLogger.log(BaseLogLevel.INFO, "Color " + color + " converted to: " + colorConverted, true);

                        String unstyledText = siblingText.substring(lastIndex, matcher.start());
                        BaseLogger.log(BaseLogLevel.INFO, "Appending unstyled text: " + unstyledText, true);
                        styledSibling.append(Component.literal(unstyledText));

                        MutableComponent coloredText = Component.literal(text)
                                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(colorConverted)));
                        BaseLogger.log(BaseLogLevel.INFO, "Appending styled text: " + text, true);
                        styledSibling.append(coloredText);

                        lastIndex = matcher.end();
                    } else {
                        BaseLogger.log(BaseLogLevel.WARNING, "Invalid color: " + color, true);
                        BaseLogger.log(BaseLogLevel.WARNING, "Returning original component due to invalid color.", true);
                        return pComponent;
                    }
                }

                String remainingText = siblingText.substring(lastIndex);
                if (!remainingText.isEmpty()) {
                    BaseLogger.log(BaseLogLevel.INFO, "Appending remaining text: " + remainingText, true);
                    styledSibling.append(Component.literal(remainingText));
                }

                BaseLogger.log(BaseLogLevel.INFO, "Final styled sibling: " + styledSibling.getString(), true);
                result.append(styledSibling);
            } else {
                BaseLogger.log(BaseLogLevel.INFO, "Sibling is not mutable. Appending as-is: " + sibling.getString(), true);
                result.append(sibling);
            }
        }

        BaseLogger.log(BaseLogLevel.INFO, "Final result component: " + result.getString(), true);
        return result;
    }


    /**
     * Overrides the {@link MarkdownFeature#applyFormat(String)} method to apply the formatting logic.
     * <p>
     * Currently, this method does not modify the provided content and simply returns it unchanged.
     * </p>
     *
     * @param pContent {@link String} - The content to format.
     * @return {@link String} - The content unchanged.
     * @author MeAlam
     * @since 1.6.0
     */
    @Override
    protected String applyFormat(String pContent) {
        return pContent;
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Color prefix: " + Prefix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Color suffix: " + Suffix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Color enabled status: " + isColorEnabled, true);
        return isColorEnabled;
    }
}
