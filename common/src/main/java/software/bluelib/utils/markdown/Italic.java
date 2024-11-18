// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

/**
 * A {@code public class} representing the italic Markdown formatting feature.
 * <p>
 * This class applies italic formatting to text surrounded by asterisks (*). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for italic text.
 * </p>
 *
 * @author MeAlam
 * @version 1.1.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Italic extends MarkdownFeature {

    /**
     * A {@code protected} {@link Boolean} that determines whether the italic formatting feature is enabled.
     *
     * @since 1.1.0
     */
    protected static Boolean isItalicEnabled = true;

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
        prefix = "*";
        suffix = "*";
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
     * @see #apply(String)
     * @since 1.1.0
     */
    @Override
    protected String applyFormat(String pContent) {
        if (!isItalicEnabled) {
            return prefix + pContent + suffix;
        }
        return "§o" + pContent + "§r";
    }
}
