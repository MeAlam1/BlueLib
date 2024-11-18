// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

/**
 * A {@code public class} representing the strikethrough Markdown formatting feature.
 * <p>
 * This class applies strikethrough formatting to text surrounded by tilde characters (~~). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for strikethrough text.
 * </p>
 *
 * @author MeAlam
 * @version 1.1.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Strikethrough extends MarkdownFeature {

    /**
     * A {@code protected} {@link Boolean} that determines whether the strikethrough formatting feature is enabled.
     *
     * @since 1.1.0
     */
    protected static Boolean isStrikethroughEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the strikethrough formatting feature.
     * <p>
     * The constructor sets the prefix and suffix to tildes (~~) for identifying content to be made strikethrough.
     * </p>
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public Strikethrough() {
        prefix = "~~";
        suffix = "~~";
    }

    /**
     * A {@code protected} {@link String} that applies the specific strikethrough formatting to the input content.
     * <p>
     * This method overrides the {@link #applyFormat(String)} method from the {@link MarkdownFeature} class
     * to add strikethrough formatting to the content by wrapping it with the strikethrough Minecraft format (§m and §r).
     * </p>
     *
     * @param pContent {@link String} - The content to be formatted with strikethrough.
     * @return The content wrapped with strikethrough formatting.
     * @author MeAlam
     * @see MarkdownFeature
     * @see #apply(String)
     * @since 1.1.0
     */
    @Override
    protected String applyFormat(String pContent) {
        if (!isStrikethroughEnabled) {
            return prefix + pContent + suffix;
        }
        return "§m" + pContent + "§r";
    }
}
