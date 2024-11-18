// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

/**
 * A {@code public class} representing the bold Markdown formatting feature.
 * <p>
 * This class applies bold formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for bold text.
 * </p>
 *
 * @author MeAlam
 * @version 1.1.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Bold extends MarkdownFeature {

    /**
     * A {@code protected} {@link Boolean} that determines whether the bold formatting feature is enabled.
     *
     * @since 1.1.0
     */
    protected static Boolean isBoldEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the bold formatting feature.
     * <p>
     * The constructor sets the prefix and suffix to double asterisks (**) for identifying content to be made bold.
     * </p>
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public Bold() {
        prefix = "**";
        suffix = "**";
    }

    /**
     * A {@code protected} {@link String} that applies the specific bold formatting to the input content.
     * <p>
     * This method overrides the {@link #applyFormat(String)} method from the {@link MarkdownFeature} class
     * to add bold formatting to the content by wrapping it with the bold Minecraft format (§l and §r).
     * </p>
     *
     * @param pContent {@link String} - The content to be formatted as bold.
     * @return The content wrapped with bold formatting.
     * @author MeAlam
     * @see MarkdownFeature
     * @see #apply(String)
     * @since 1.1.0
     */
    @Override
    protected String applyFormat(String pContent) {
        if (!isBoldEnabled) {
            return prefix + pContent + suffix;
        }
        return "§l" + pContent + "§r";
    }
}
