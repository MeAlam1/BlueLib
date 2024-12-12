// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the strikethrough Markdown formatting feature.
 * <p>
 * This class applies strikethrough formatting to text surrounded by tilde characters (~~). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for strikethrough text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies the Strikethrough formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix for Strikethrough formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for Strikethrough formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for Strikethrough formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for Strikethrough formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for Strikethrough formatting.</li>
 * <li>{@link #isStrikethroughEnabled()} - Retrieves whether Strikethrough formatting is enabled.</li>
 * </ul>
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
    public static Boolean isStrikethroughEnabled = true;

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

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent StrikethroughText = Component.literal(pText)
                .setStyle(pOriginalStyle.withStrikethrough(true));
        pResult.append(StrikethroughText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isStrikethroughEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Strikethrough";
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
