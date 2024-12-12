// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the Spoiler Markdown formatting feature.
 * <p>
 * This class applies Spoiler formatting to text surrounded by double underscores (__). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for Spoilerd text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies the specific Spoiler formatting to the input content.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix used for Spoiler formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix used for Spoiler formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix used for Spoiler formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix used for Spoiler formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix used for Spoiler formatting.</li>
 * <li>{@link #isSpoilerEnabled()} - Retrieves whether Spoiler formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.5.0
 * @see MarkdownFeature
 * @see #applyFormat(String)
 * @since 1.5.0
 */
public class Spoiler extends MarkdownFeature {

    /**
     * A {@code protected static} field representing the default prefix for Spoiler formatting.
     *
     * @since 1.5.0
     */
    protected static String Prefix = "||";

    /**
     * A {@code protected static} field representing the default suffix for Spoiler formatting.
     *
     * @since 1.5.0
     */
    protected static String Suffix = "||";

    /**
     * A {@code protected} {@link Boolean} that determines whether the Spoiler formatting feature is enabled.
     *
     * @since 1.5.0
     */
    public static Boolean isSpoilerEnabled = true;

    /**
     * A {@code public} constructor that initializes the prefix and suffix for the Spoiler formatting feature.
     * <p>
     * The constructor sets the prefix and suffix to double underscores (__) for identifying content to be Spoilerd.
     * </p>
     *
     * @author MeAlam
     * @since 1.5.0
     */
    public Spoiler() {
        prefix = Prefix;
        suffix = Suffix;
    }

    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent SpoilerText = Component.literal(pText)
                .setStyle(pOriginalStyle.withObfuscated(true));
        pResult.append(SpoilerText);
    }

    @Override
    protected boolean isFeatureEnabled() {
        return isSpoilerEnabled;
    }

    @Override
    protected String getFeatureName() {
        return "Spoiler";
    }

    /**
     * A {@code public static void} to update the prefix used for Spoiler formatting.
     *
     * @param pPrefix {@link String} - The new prefix for Spoiler formatting.
     * @author MeAlam
     * @since 1.5.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Spoiler prefix updated to: " + Prefix, true);
    }

    /**
     * A {@code public static void} to update the suffix used for Spoiler formatting.
     *
     * @param pSuffix {@link String} - The new suffix for Spoiler formatting.
     * @author MeAlam
     * @since 1.5.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Spoiler suffix updated to: " + Suffix, true);
    }

    /**
     * A {@code public static} {@link String} that retrieves the current prefix used for Spoiler formatting.
     *
     * @return The current prefix for Spoiler formatting.
     * @author MeAlam
     * @since 1.5.0
     */
    public static String getPrefix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Spoiler prefix: " + Prefix, true);
        return Prefix;
    }

    /**
     * A {@code public static} {@link String} that retrieves the current suffix used for Spoiler formatting.
     *
     * @return The current suffix for Spoiler formatting.
     * @author MeAlam
     * @since 1.5.0
     */
    public static String getSuffix() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Spoiler suffix: " + Suffix, true);
        return Suffix;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether Spoiler formatting is enabled.
     *
     * @return {@code true} if Spoiler formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.5.0
     */
    public static Boolean isSpoilerEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved Spoiler enabled status: " + isSpoilerEnabled, true);
        return isSpoilerEnabled;
    }
}
