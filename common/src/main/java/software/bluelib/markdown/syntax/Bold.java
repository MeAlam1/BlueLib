// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the bold Markdown formatting feature.
 * <p>
 * This class applies bold formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for bold text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies bold formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix used for bold formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix used for bold formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix used for bold formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix used for bold formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix used for bold formatting.</li>
 * <li>{@link #isBoldEnabled()} - Retrieves whether bold formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
 * @see MarkdownFeature
 * @since 1.1.0
 */
public class Bold extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for bold formatting.
     *
     * @since 1.2.0
     */
    protected static String Prefix = "**";

    /**
     * A {@code protected static} field representing the default suffix for bold formatting.
     *
     * @since 1.2.0
     */
    protected static String Suffix = "**";

    /**
     * A {@code protected static} field that determines whether bold formatting is enabled.
     *
     * @since 1.1.0
     */
    public static Boolean isBoldEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the bold formatting feature.
     * <p>
     * The constructor sets the instance prefix and suffix to match the static Prefix and Suffix values.
     * </p>
     *
     * @author MeAlam
     * @since 1.1.0
     */
    public Bold() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent boldText = Component.literal(pText)
                .setStyle(pOriginalStyle.withBold(true));
        pResult.append(boldText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isBoldEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Bold";
    }

    /**
     * A {@code public static void} to update the prefix and suffix used for bold formatting.
     *
     * @param pPrefix {@link String} - The new prefix for bold formatting.
     * @param pSuffix {@link String} - The new suffix for bold formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * A {@code public static void} to update the prefix used for bold formatting.
     *
     * @param pPrefix {@link String} - The new prefix for bold formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for bold formatting.
     *
     * @param pSuffix {@link String} - The new suffix for bold formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for bold formatting.
     *
     * @return The current prefix for bold formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for bold formatting.
     *
     * @return The current suffix for bold formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether bold formatting is enabled.
     *
     * @return {@code true} if bold formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.2.0
     */
    public static Boolean isBoldEnabled() {
        return isBoldEnabled;
    }
}
