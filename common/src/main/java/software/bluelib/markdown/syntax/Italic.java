// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the italic Markdown formatting feature.
 * <p>
 * This class applies italic formatting to text surrounded by asterisks (*). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for italic text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies italic formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix for italic formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for italic formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for italic formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for italic formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for italic formatting.</li>
 * <li>{@link #isItalicEnabled()} - Retrieves whether italic formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.4.0
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
    public static Boolean isItalicEnabled = true;

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

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent ItalicText = Component.literal(pText)
                .setStyle(pOriginalStyle.withItalic(true));
        pResult.append(ItalicText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isItalicEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Italic";
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic prefix updated to: " + Prefix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Italic suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Italic formatting.
     *
     * @return The current prefix for Italic formatting.
     * @author MeAlam
     * @since 1.2.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic prefix: " + Prefix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic suffix: " + Suffix, true);
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
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Italic enabled status: " + isItalicEnabled, true);
        return isItalicEnabled;
    }
}
