// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

import net.minecraft.network.chat.*;
import software.bluelib.utils.MiscUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the Hyperlink Markdown formatting feature.
 * <p>
 * This class applies Hyperlink formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for Hyperlink text.
 * </p>
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
    protected static Boolean isHyperlinkEnabled = true;

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
    public MutableComponent applyLast(String pMessage) {
        if (!isHyperlinkEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Hyperlink formatting is disabled. Returning original content.", true);
            return Component.literal(pMessage);
        }

        String[] splitMessage = splitMessage(pMessage);

        if (splitMessage[0].endsWith("\\")) {
            String modifiedMessage = pMessage.substring(0, pMessage.lastIndexOf("\\"))
                    + pMessage.substring(pMessage.lastIndexOf("\\") + 1);
            return Component.literal(modifiedMessage);
        }

        if (!MiscUtils.isValidURL(splitMessage[2])) {
            return Component.literal(pMessage);
        }

        MutableComponent partOne = splitMessage[0].isEmpty() ? Component.empty() : Component.literal(splitMessage[0]);
        MutableComponent link = Component.literal(splitMessage[1])
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, splitMessage[2])));
        MutableComponent partTwo = splitMessage[3].isEmpty() ? Component.empty() : Component.literal(splitMessage[3]);

        return partOne.append(Component.literal(" "))
                .append(link)
                .append(Component.literal(" "))
                .append(partTwo);

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
     * A {@code static} {@link String}{@code []} that splits the provided message into parts.
     * <p>
     * The method identifies the text before the hyperlink, the link text, the URL, and the text after the hyperlink.
     * </p>
     *
     * @param pMessage {@link String} - The message to split.
     * @return {@link String}{@code []} - An array containing:
     * <ul>
     *   <li>Text before the hyperlink</li>
     *   <li>Link text</li>
     *   <li>URL</li>
     *   <li>Text after the hyperlink</li>
     * </ul>
     * @author MeAlam
     * @since 1.4.0
     */
    static String[] splitMessage(String pMessage) {
        int openBracketIndex = pMessage.indexOf("[");
        int closeBracketIndex = pMessage.indexOf("]", openBracketIndex);
        int openParenIndex = pMessage.indexOf("(", closeBracketIndex);
        int closeParenIndex = pMessage.indexOf(")", openParenIndex);

        String beforeLink = (openBracketIndex > 0) ? pMessage.substring(0, openBracketIndex).trim() : "";
        String linkText = (openBracketIndex != -1 && closeBracketIndex != -1)
                ? pMessage.substring(openBracketIndex + 1, closeBracketIndex).trim()
                : " ";
        String url = (openParenIndex != -1 && closeParenIndex != -1)
                ? pMessage.substring(openParenIndex + 1, closeParenIndex).trim()
                : " ";
        String afterLink = (closeParenIndex != -1) ? pMessage.substring(closeParenIndex + 1).trim() : "";

        if (!url.isEmpty() && linkText.isEmpty()) {
            linkText = url;
        }

        return new String[]{beforeLink, linkText, url, afterLink};
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
