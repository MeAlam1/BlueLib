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
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Italic formatting.
     *
     * @return The current prefix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
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
        return isItalicEnabled;
    }
}
