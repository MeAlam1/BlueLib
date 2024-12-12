// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the underline Markdown formatting feature.
 * <p>
 * This class applies underline formatting to text surrounded by double underscores (__). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for underlined text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies the specific underline formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix used for Underline formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix used for Underline formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix used for Underline formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix used for Underline formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix used for Underline formatting.</li>
 * <li>{@link #isUnderlineEnabled()} - Retrieves whether Underline formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.1.0
 */
public class Underline extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Underline formatting.
     *
     * @since 1.2.0
     */
    protected static String Prefix = "__";

    /**
     * A {@code protected static} field representing the default suffix for Underline formatting.
     *
     * @since 1.2.0
     */
    protected static String Suffix = "__";

    /**
     * A {@code protected} {@link Boolean} that determines whether the underline formatting feature is enabled.
     *
     * @since 1.1.0
     */
    public static Boolean isUnderlineEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the underline formatting feature.
     * <p>
     * The constructor sets the prefix and suffix to double underscores (__) for identifying content to be underlined.
     * </p>
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public Underline() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent UnderlineText = Component.literal(pText)
                .setStyle(pOriginalStyle.withUnderlined(true));
        pResult.append(UnderlineText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isUnderlineEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Underline";
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for Underline formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Underline formatting.
     * @param pSuffix {@link String} - The new suffix for Underline formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for Underline formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Underline formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Underline formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Underline formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Underline suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Underline formatting.
     *
     * @return The current prefix for Underline formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Underline prefix: " + Prefix, true);
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Underline formatting.
     *
     * @return The current suffix for Underline formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getSuffix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Underline suffix: " + Suffix, true);
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether Underline formatting is enabled.
     *
     * @return {@code true} if Underline formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.2.0
     */
    public static Boolean isUnderlineEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Underline enabled status: " + isUnderlineEnabled, true);
        return isUnderlineEnabled;
    }
}
