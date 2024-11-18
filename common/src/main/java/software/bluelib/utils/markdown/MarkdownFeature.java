// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

import java.util.logging.Level;

/**
 * A {@code public abstract class} that represents a feature for applying formatting to Markdown-style text.
 * <p>
 * This class provides methods to apply specific formatting to a message surrounded by a prefix and suffix.
 * The formatting is only applied if the feature is enabled.
 * The {@link #apply(String)} method uses regular expressions to identify and format content between the prefix and suffix.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #apply(String)} - Applies formatting to the input message based on the prefix and suffix.</li>
 *   <li>{@link #setPrefixSuffix(String, String)} - Sets new prefix and suffix for identifying content to format.</li>
 *   <li>{@link #enable()} - Enables the feature, allowing formatting to be applied.</li>
 *   <li>{@link #disable()} - Disables the feature, preventing formatting from being applied.</li>
 *   <li>{@link #isEnabled()} - Checks if the feature is enabled.</li>
 *   <li>{@link #escapeRegex(String)} - Escapes special characters in the prefix and suffix for use in regular expressions.</li>
 * </ul>
 *
 * @author MeAlam
 * @since 1.1.0
 * @version 1.1.0
 */
public abstract class MarkdownFeature {

    /**
     * A {@code protected} field indicating whether markdown formatting is enabled.<br>
     * When {@code true}, formatting will be applied to the message.
     */
    protected boolean enabled = true;

    /**
     * A {@code protected} field representing the prefix used to identify content that needs formatting. <br>
     * This field holds the beginning part of the string to match in the input message.
     */
    protected String prefix;

    /**
     * A {@code protected} field representing the suffix used to identify content that needs formatting. <br>
     * This field holds the ending part of the string to match in the input message.
     */
    protected String suffix;

    /**
     * Applies the specific formatting to the message surrounded by the defined prefix and suffix.
     * <p>
     * If the feature is disabled, it returns the original message. Otherwise, it searches for content between
     * the prefix and suffix and applies the defined formatting.
     * </p>
     *
     * @param pMessage {@link String} - The input message to be formatted.
     * @return The formatted message with applied changes.
     *
     * @author MeAlam
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
     *
     * @author MeAlam
     * @since 1.1.0
     */
    protected abstract String applyFormat(String pContent);

    /**
     * Sets the new prefix and suffix that will be used for identifying content to apply formatting.
     *
     * @param pNewPrefix The new prefix to define the start of the formatted content.
     * @param pNewSuffix The new suffix to define the end of the formatted content.
     */
    public void setPrefixSuffix(String pNewPrefix, String pNewSuffix) {
        // Set the prefix and suffix to new values.
        prefix = pNewPrefix;
        suffix = pNewSuffix;
    }

    /**
     * Enables this feature, allowing formatting to be applied to messages.
     * When enabled, the {@link #apply(String)} method will modify messages.
     */
    public void enable() {
        enabled = true;
    }

    /**
     * Disables this feature, preventing any formatting from being applied.
     * When disabled, the {@link #apply(String)} method will return the original message without any changes.
     */
    public void disable() {
        enabled = false;
    }

    /**
     * Checks if this feature is enabled.
     *
     * @return {@code true} if the feature is enabled; {@code false} if it is disabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Escapes special characters in the input string for safe use in regular expressions.
     * <p>
     * This method is used to ensure that the prefix and suffix are properly treated as literal strings
     * when used in regular expressions within the {@link #apply(String)} method.
     * </p>
     *
     * @param pInput The input string to escape.
     * @return A string with special regex characters escaped.
     */
    static String escapeRegex(String pInput) {
        // Escape all characters that have special meaning in regular expressions.
        return pInput.replaceAll("([\\\\*+\\[\\](){}|.^$?])", "\\\\$1");
    }
}
