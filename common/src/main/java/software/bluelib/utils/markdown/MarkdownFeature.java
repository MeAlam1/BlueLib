// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

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
 *   <li>{@link #setPrefixSuffix(String, String)} - Sets new prefix and suffix for identifying content to format.</li>
 *   <li>{@link #enable()} - Enables markdown, allowing formatting to be applied.</li>
 *   <li>{@link #disable()} - Disables markdown, preventing formatting from being applied.</li>
 *   <li>{@link #isEnabled()} - Checks if markdown is enabled.</li>
 *   <li>{@link #escapeRegex(String)} - Escapes special characters in the prefix and suffix for use in regular expressions.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.1.0
 * @see java.util.logging.Logger
 * @since 1.1.0
 */
public abstract class MarkdownFeature {

    /**
     * A {@code protected} field indicating whether markdown formatting is enabled.<br>
     * When {@code true}, formatting will be applied to the message.
     *
     * @since 1.1.0
     */
    protected boolean enabled = true;

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
     * @see java.util.logging.Logger
     * @see java.util.logging.Level
     * @since 1.1.0
     */
    public String apply(String pMessage) {
        if (!enabled) return pMessage;

        return pMessage.replaceAll(escapeRegex(prefix) + "(.*?)" + escapeRegex(suffix), applyFormat("$1"));
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
     * @see java.util.logging.Logger
     * @since 1.1.0
     */
    protected abstract String applyFormat(String pContent);

    /**
     * A {@code public} {@code void} that sets the new prefix and suffix that will be used for identifying content to apply formatting.
     *
     * @param pNewPrefix The new prefix to define the start of the formatted content.
     * @param pNewSuffix The new suffix to define the end of the formatted content.
     * @author MeAlam
     * @see java.util.logging.Logger
     * @since 1.1.0
     */
    public void setPrefixSuffix(String pNewPrefix, String pNewSuffix) {
        prefix = pNewPrefix;
        suffix = pNewSuffix;
    }

    /**
     * A {@code public} {@code void} that enables markdown, allowing formatting to be applied to messages.
     * When enabled, the {@link #apply(String)} method will modify messages.
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public void enable() {
        enabled = true;
    }

    /**
     * A {@code public} {@code void} that disables markdown, preventing any formatting from being applied.
     * When disabled, the {@link #apply(String)} method will return the original message without any changes.
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public void disable() {
        enabled = false;
    }

    /**
     * A {@code public} {@link boolean} method that checks if markdown is enabled.
     *
     * @return {@code true} if markdown is enabled; {@code false} if it is disabled.
     * @author MeAlam
     * @since 1.1.0
     */
    public boolean isEnabled() {
        return enabled;
    }

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
     * @see java.util.logging.Logger
     * @since 1.1.0
     */
    static String escapeRegex(String pInput) {
        return pInput.replaceAll("([\\\\*+\\[\\](){}|.^$?])", "\\\\$1");
    }
}
