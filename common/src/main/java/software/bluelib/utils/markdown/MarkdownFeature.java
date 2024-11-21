// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@code public abstract class} that represents a feature for applying formatting to Markdown-style text.
 * <p>
 * This class provides methods to apply specific formatting to a message surrounded by a prefix and suffix.
 * The formatting is only applied if markdown is enabled.
 * The {@link #apply(String)} method uses regular expressions to identify and format content between the prefix and suffix.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #apply(String)} - Applies formatting to the input message based on the prefix and suffix.</li>
 *   <li>{@link #escapeRegex(String)} - Escapes special characters in the prefix and suffix for use in regular expressions.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.2.0
 * @since 1.1.0
 */
public abstract class MarkdownFeature {

    /**
     * A {@code protected} field representing the prefix used to identify content that needs formatting. <br>
     * This field holds the beginning part of the string to match in the input message.
     *
     * @since 1.1.0
     */
    protected String prefix;

    /**
     * A {@code protected} field representing the suffix used to identify content that needs formatting. <br>
     * This field holds the ending part of the string to match in the input message.
     *
     * @since 1.1.0
     */
    protected String suffix;

    /**
     * A {@code public} {@link String} that applies formatting to the input message based on the prefix and suffix.
     * <p>
     * If markdown is disabled, it returns the original message. Otherwise, it searches for content between
     * the prefix and suffix and applies the defined formatting.
     * </p>
     *
     * @param pMessage {@link String} - The input message to be formatted.
     * @return The formatted message with applied changes.
     * @author MeAlam
     * @since 1.1.0
     */
    public String apply(String pMessage) {
        String escapedPrefix = escapeRegex(prefix);
        String escapedSuffix = escapeRegex(suffix);

        Pattern pattern = Pattern.compile("(?<!\\\\)" + escapedPrefix + "(.*?)" + escapedSuffix);
        Matcher matcher = pattern.matcher(pMessage);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String content = matcher.group(1);
            if (content.isEmpty()) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(prefix + suffix));
            } else {
                String formatted = applyFormat(content);
                matcher.appendReplacement(result, Matcher.quoteReplacement(formatted));
            }
        }

        matcher.appendTail(result);
        return result.toString().replaceAll("\\\\" + escapedPrefix, prefix);
    }


    /**
     * A {@code protected abstract} {@link String} that applies the specific formatting to the content between the prefix and suffix.
     * <p>
     * This method will be used by the {@link #apply(String)} method to format the content between the prefix and suffix.
     * </p>
     *
     * @param pContent {@link String} - The content to be formatted.
     * @return The formatted content.
     * @author MeAlam
     * @since 1.1.0
     */
    protected abstract String applyFormat(String pContent);

    /**
     * A {@code static} {@link String} that escapes special characters in the input string for safe use in regular expressions.
     * <p>
     * This method is used to ensure that the prefix and suffix are properly treated as literal strings
     * when used in regular expressions within the {@link #apply(String)} method.
     * </p>
     *
     * @param pInput The input string to escape.
     * @return A string with special regex characters escaped.
     * @author MeAlam
     * @since 1.1.0
     */
    static String escapeRegex(String pInput) {
        return pInput.replaceAll("([\\\\*+\\[\\](){}|.^$?])", "\\\\$1");
    }
}
