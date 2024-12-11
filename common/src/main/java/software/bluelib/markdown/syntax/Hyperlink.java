// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * @version 1.6.0
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
     * @param pComponent {@link String} - The message to format.
     * @return {@link MutableComponent} - The formatted message with Hyperlink Markdown applied, or the original message.
     * @author MeAlam
     * @since 1.6.0
     */
    public MutableComponent applyHyperlink(MutableComponent pComponent) {
        if (!isHyperlinkEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Hyperlink formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix) + "\\((.*?)\\)");
        BaseLogger.log(BaseLogLevel.INFO, "Using regex pattern: " + pattern.pattern(), true);

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "No siblings found. Processing the component itself.", true);
            processComponentText(pComponent.getString(), result, pattern);
        } else {
            BaseLogger.log(BaseLogLevel.INFO, "Processing component with siblings.", true);
            result = processSiblings(pComponent, pattern);
        }

        BaseLogger.log(BaseLogLevel.INFO, "Final result component: " + result.getString(), true);
        return result;
    }

    private void processComponentText(String text, MutableComponent result, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int lastIndex = 0;

        while (matcher.find()) {
            appendUnstyledText(text.substring(lastIndex, matcher.start()), result);
            appendHyperlink(matcher.group(1), matcher.group(2), result);
            lastIndex = matcher.end();
        }

        appendUnstyledText(text.substring(lastIndex), result);
    }

    private MutableComponent processSiblings(MutableComponent pComponent, Pattern pattern) {
        MutableComponent result = Component.empty();

        for (Component sibling : pComponent.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                BaseLogger.log(BaseLogLevel.INFO, "Processing sibling: " + mutableSibling.getString(), true);
                processComponentText(mutableSibling.getString(), result, pattern);
            } else {
                BaseLogger.log(BaseLogLevel.INFO, "Sibling is not mutable. Appending as-is: " + sibling.getString(), true);
                result.append(sibling);
            }
        }

        return result;
    }

    private void appendUnstyledText(String text, MutableComponent result) {
        if (!text.isEmpty()) {
            BaseLogger.log(BaseLogLevel.INFO, "Appending unstyled text: " + text, true);
            result.append(Component.literal(text));
        }
    }

    private void appendHyperlink(String linkText, String url, MutableComponent result) {
        BaseLogger.log(BaseLogLevel.INFO, "Matched text: " + linkText + ", URL: " + url, true);

        if (!MiscUtils.isValidURL(url)) {
            BaseLogger.log(BaseLogLevel.WARNING, "Invalid URL detected: " + url, true);
            result.append(Component.literal(prefix + linkText + suffix + "(" + url + ")"));
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        MutableComponent hyperlink = Component.literal(linkText)
                .setStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(0x1F5FE1))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));

        result.append(hyperlink);
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
