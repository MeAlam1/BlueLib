// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@code public class} representing the italic Markdown formatting feature.
 * <p>
 * This class applies italic formatting to text surrounded by asterisks (*). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for italic text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies italic formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix for italic formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for italic formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for italic formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for italic formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for italic formatting.</li>
 * <li>{@link #isItalicEnabled()} - Retrieves whether italic formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Italic extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Italic formatting.
     *
     * @since 1.2.0
     */
    protected static String Prefix = "*";

    /**
     * A {@code protected static} field representing the default suffix for Italic formatting.
     *
     * @since 1.2.0
     */
    protected static String Suffix = "*";

    /**
     * A {@code protected} {@link Boolean} that determines whether the italic formatting feature is enabled.
     *
     * @since 1.1.0
     */
    public static Boolean isItalicEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the italic formatting feature.
     * <p>
     * The constructor sets the prefix and suffix to asterisks (*) for identifying content to be made italic.
     * </p>
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public Italic() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * A {@code protected} {@link String} that applies the specific italic formatting to the input content.
     * <p>
     * This method overrides the {@link #applyFormat(String)} method from the {@link MarkdownFeature} class
     * to add italic formatting to the content by wrapping it with the italic Minecraft format (§o and §r).
     * </p>
     *
     * @param pContent {@link String} - The content to be formatted as italic.
     * @return The content wrapped with italic formatting.
     * @author MeAlam
     * @see MarkdownFeature
     * @see #applyString(String)
     * @since 1.1.0
     */
    @Override
    protected String applyFormat(String pContent) {
        if (!isItalicEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Italic is disabled. Returning original content.", true);
            return prefix + pContent + suffix;
        }
        return "§o" + pContent + "§r";
    }

    public MutableComponent applyItalic(MutableComponent pComponent) {
        if (!isItalicEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Italic formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix));
        BaseLogger.log(BaseLogLevel.INFO, "Applying italic formatting with pattern: " + pattern.pattern(), true);

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextForItalic(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsForItalic(pComponent, pattern);
        }

        return result;
    }

    private void processComponentTextForItalic(String text, Style originalStyle, MutableComponent result, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int lastIndex = 0;
        BaseLogger.log(BaseLogLevel.INFO, "Processing component text for italic: " + text, true);

        while (matcher.find()) {
            if (matcher.group(1).isEmpty()) {
                BaseLogger.log(BaseLogLevel.INFO, "Empty content between italic markers, skipping styling.", true);
                appendUnstyledText(text.substring(lastIndex, matcher.end()), result, originalStyle);
            } else if (matcher.start() > 0 && text.charAt(matcher.start() - 1) == '\\') {
                BaseLogger.log(BaseLogLevel.INFO, "Escape character found before italic prefix, skipping italic: " + matcher.group(0), true);
                appendUnstyledText(text.substring(lastIndex, matcher.start() - 1), result, originalStyle);
                appendUnstyledText(matcher.group(0), result, originalStyle);
            } else {
                BaseLogger.log(BaseLogLevel.INFO, "Applying italic to text: " + matcher.group(1), true);
                appendUnstyledText(text.substring(lastIndex, matcher.start()), result, originalStyle);
                appendItalic(matcher.group(1), originalStyle, result);
            }
            lastIndex = matcher.end();
        }

        appendUnstyledText(text.substring(lastIndex), result, originalStyle);
    }

    private MutableComponent processSiblingsForItalic(MutableComponent pComponent, Pattern pattern) {
        MutableComponent result = Component.empty();
        BaseLogger.log(BaseLogLevel.INFO, "Processing component siblings for italic.", true);

        for (Component sibling : pComponent.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                BaseLogger.log(BaseLogLevel.INFO, "Processing sibling component for italic: " + mutableSibling.getString(), true);
                processComponentTextForItalic(mutableSibling.getString(), mutableSibling.getStyle(), result, pattern);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    private void appendItalic(String text, Style originalStyle, MutableComponent result) {
        MutableComponent italicText = Component.literal(text)
                .setStyle(originalStyle.withItalic(true));

        BaseLogger.log(BaseLogLevel.INFO, "Appending italic text: " + italicText, true);
        result.append(italicText);
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for Italic formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Italic formatting.
     * @param pSuffix {@link String} - The new suffix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for Italic formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Italic formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Italic formatting.
     *
     * @return The current prefix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic prefix: " + Prefix, true);
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Italic formatting.
     *
     * @return The current suffix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getSuffix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic suffix: " + Suffix, true);
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether italic formatting is enabled.
     *
     * @return {@code true} if italic formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.2.0
     */
    public static Boolean isItalicEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic enabled status: " + isItalicEnabled, true);
        return isItalicEnabled;
    }
}
