// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.*;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.math.MiscUtils;

/**
 * A {@code public class} representing the Hyperlink Markdown formatting feature.
 * <p>
 * This class applies Hyperlink formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for Hyperlink text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyComponent(String)} - Applies Hyperlink formatting to the provided message.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix used for Hyperlink formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix used for Hyperlink formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix used for Hyperlink formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix used for Hyperlink formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix used for Hyperlink formatting.</li>
 * <li>{@link #isHyperlinkEnabled()} - Retrieves whether Hyperlink formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @see MarkdownFeature
 * @since 1.4.0
 */
public class Hyperlink extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Hyperlink formatting.
     *
     * @since 1.4.0
     */
    protected static String Prefix = "[";

    /**
     * A {@code protected static} field representing the default suffix for Hyperlink formatting.
     *
     * @since 1.4.0
     */
    protected static String Suffix = "]";

    /**
     * A {@code protected static} field that determines whether Hyperlink formatting is enabled.
     *
     * @since 1.4.0
     */
    public static Boolean isHyperlinkEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the Hyperlink formatting feature.
     * <p>
     * The constructor sets the instance prefix and suffix to match the static Prefix and Suffix values.
     * </p>
     *
     * @author MeAlam
     * @since 1.4.0
     */
    public Hyperlink() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * A {@code public} {@link MutableComponent} that applies Hyperlink formatting to the provided message.
     * <p>
     * This method applies Hyperlink formatting to the provided message, <br>
     * if the message contains a valid URL surrounded by the prefix and suffix. <br>
     * The method returns the formatted message with the Hyperlink applied, or the original message if no valid URL is found.
     * </p>
     *
     * @param pMessage {@link String} - The message to format.
     * @return {@link MutableComponent} - The formatted message with Hyperlink Markdown applied, or the original message.
     * @author MeAlam
     * @since 1.4.0
     */
    @Override
    public MutableComponent applyComponent(String pMessage) {
        if (!isHyperlinkEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Hyperlink formatting is disabled. Returning original content.", true);
            return Component.literal(pMessage);
        }

        MutableComponent finalMessage = Component.empty();
        int currentIndex = 0;

        while (currentIndex < pMessage.length()) {
            int openBracketIndex = pMessage.indexOf("[", currentIndex);
            int closeBracketIndex = pMessage.indexOf("]", openBracketIndex);
            int openParenIndex = pMessage.indexOf("(", closeBracketIndex);
            int closeParenIndex = pMessage.indexOf(")", openParenIndex);

            if (openBracketIndex == -1 || closeBracketIndex == -1 || openParenIndex == -1 || closeParenIndex == -1) {
                finalMessage.append(Component.literal(pMessage.substring(currentIndex)));
                break;
            }

            if (openBracketIndex > currentIndex) {
                finalMessage.append(Component.literal(pMessage.substring(currentIndex, openBracketIndex)));
            }

            String linkText = pMessage.substring(openBracketIndex + 1, closeBracketIndex).trim();
            String url = pMessage.substring(openParenIndex + 1, closeParenIndex).trim();

            if (!MiscUtils.isValidURL(url)) {
                finalMessage.append(Component.literal(pMessage.substring(openBracketIndex, closeParenIndex + 1)));
                currentIndex = closeParenIndex + 1;
                continue;
            }

            MutableComponent link = Component.literal(linkText)
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x1F5FE1)).withUnderlined(true)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
            finalMessage.append(link);

            currentIndex = closeParenIndex + 1;
        }

        return finalMessage;
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
     * @since 1.4.0
     */
    @Override
    protected String applyFormat(String pContent) {
        return pContent;
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for Hyperlink formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Hyperlink formatting.
     * @param pSuffix {@link String} - The new suffix for Hyperlink formatting.
     * @author MeAlam
     * @since 1.4.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for Hyperlink formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Hyperlink formatting.
     * @author MeAlam
     * @since 1.4.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Hyperlink formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Hyperlink formatting.
     * @author MeAlam
     * @since 1.4.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Hyperlink formatting.
     *
     * @return The current prefix for Hyperlink formatting.
     * @author MeAlam
     * @since 1.4.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Hyperlink prefix: " + Prefix, true);
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Hyperlink formatting.
     *
     * @return The current suffix for Hyperlink formatting.
     * @author MeAlam
     * @since 1.4.0
     */
    public static String getSuffix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Hyperlink suffix: " + Suffix, true);
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether Hyperlink formatting is enabled.
     *
     * @return {@code true} if Hyperlink formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.4.0
     */
    public static Boolean isHyperlinkEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Hyperlink enabled status: " + isHyperlinkEnabled, true);
        return isHyperlinkEnabled;
    }
}
