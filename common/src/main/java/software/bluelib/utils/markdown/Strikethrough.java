// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.markdown;

import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the strikethrough Markdown formatting feature.
 * <p>
 * This class applies strikethrough formatting to text surrounded by tilde characters (~~). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for strikethrough text.
 * </p>
 *
 * @author MeAlam
 * @version 1.4.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Strikethrough extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Strikethrough formatting.
     *
     * @since 1.2.0
     */
    protected static String Prefix = "~~";

    /**
     * A {@code protected static} field representing the default suffix for Strikethrough formatting.
     *
     * @since 1.2.0
     */
    protected static String Suffix = "~~";

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
        prefix = Prefix;
        suffix = Suffix;
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
            BaseLogger.log(BaseLogLevel.INFO, "Strikethrough is disabled. Returning original content.", true);
            return prefix + pContent + suffix;
        }
        return "§m" + pContent + "§r";
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for Strikethrough formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Strikethrough formatting.
     * @param pSuffix {@link String} - The new suffix for Strikethrough formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for Strikethrough formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Strikethrough formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Strikethrough formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Strikethrough formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Strikethrough formatting.
     *
     * @return The current prefix for Strikethrough formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Strikethrough prefix: " + Prefix, true);
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Strikethrough formatting.
     *
     * @return The current suffix for Strikethrough formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getSuffix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Strikethrough suffix: " + Suffix, true);
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether Strikethrough formatting is enabled.
     *
     * @return {@code true} if Strikethrough formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.2.0
     */
    public static Boolean isStrikethroughEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Strikethrough enabled status: " + isStrikethroughEnabled, true);
        return isStrikethroughEnabled;
    }
}
