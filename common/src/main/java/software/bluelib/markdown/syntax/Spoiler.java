// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;

/**
 * Handles the spoiler feature for Markdown text formatting.
 * <p>
 * Purpose: This class applies the spoiler formatting to text within markdown syntax.<br>
 * When: The spoiler is applied when the text is surrounded by a specified prefix and suffix.<br>
 * Where: Executed within the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method when the feature is enabled.<br>
 * Additional Info: The default prefix and suffix are both set to "||". The feature is controlled by the {@link Spoiler#isSpoilerEnabled} flag.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link Spoiler#appendFormattedText(String, Style, MutableComponent)} - Appends the formatted text with the spoiler style to the result component.</li>
 * <li>{@link Spoiler#isFeatureEnabled()} - Checks if the spoiler feature is enabled.</li>
 * <li>{@link Spoiler#getFeatureName()} - Gets the feature's name.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for Spoiler formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for Spoiler formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for Spoiler formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for Spoiler formatting.</li>
 * <li>{@link #isSpoilerEnabled()} - Retrieves whether Spoiler formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MarkdownFeature
 * @see Style
 * @see MutableComponent
 * @since 1.5.0
 */
public class Spoiler extends MarkdownFeature {

    /**
     * Default prefix for spoiler formatting.
     * <p>
     * Purpose: This variable stores the default prefix for applying spoiler formatting.<br>
     * When: It is used when checking for the start of a spoiler formatted section.<br>
     * Where: Used in the {@link Spoiler#prefix} and {@link Spoiler#suffix} logic.<br>
     * Additional Info: The default value is "||", but it can be modified using {@link Spoiler#setPrefix(String)} and {@link Spoiler#setSuffix(String)}.<br>
     * </p>
     *
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#Suffix
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#getPrefix()
     * @see Spoiler#getSuffix()
     * @since 1.5.0
     */
    protected static String Prefix = "||";

    /**
     * Default suffix for spoiler formatting.
     * <p>
     * Purpose: This variable stores the default suffix for applying spoiler formatting.<br>
     * When: It is used when checking for the end of a spoiler formatted section.<br>
     * Where: Used in the {@link Spoiler#prefix} and {@link Spoiler#suffix} logic.<br>
     * Additional Info: The default value is "||", but it can be modified using {@link Spoiler#setPrefix(String)} and {@link Spoiler#setSuffix(String)}.<br>
     * </p>
     *
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#Prefix
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#getPrefix()
     * @see Spoiler#getSuffix()
     * @since 1.5.0
     */
    protected static String Suffix = "||";

    /**
     * Flag that determines whether the spoiler feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the spoiler feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the spoiler should be applied.<br>
     * Where: It is used in the {@link Spoiler#isFeatureEnabled()} and {@link Spoiler#isSpoilerEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see Spoiler#isFeatureEnabled()
     * @see Spoiler#isSpoilerEnabled()
     * @since 1.5.0
     */
    public static Boolean isSpoilerEnabled = true;

    /**
     * Constructor that initializes the prefix and suffix for the spoiler feature.
     * <p>
     * Purpose: This constructor sets the default prefix and suffix values for the spoiler formatting.<br>
     * When: It is invoked when creating an instance of the {@link Spoiler} class.<br>
     * Where: Executed within the {@link Spoiler} constructor.<br>
     * Additional Info: The default prefix and suffix are both set to "||".<br>
     * </p>
     *
     * @author MeAlam
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#getSuffix()
     * @see Spoiler#getPrefix()
     * @since 1.5.0
     */
    public Spoiler() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * Appends the formatted text with the spoiler style to the result component.
     * <p>
     * Purpose: This method takes the provided text and applies the spoiler style, then appends it to the result.<br>
     * When: Called when processing a markdown string with spoiler syntax.<br>
     * Where: Executed in {@link Spoiler#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)}.<br>
     * Additional Info: The text is wrapped with a style that enables obfuscation (for hiding the content).<br>
     * </p>
     *
     * @param pText          The text to be formatted with the spoiler effect.
     * @param pOriginalStyle The original style applied to the text.
     * @param pResult        The mutable component that the formatted text will be appended to.
     * @author MeAlam
     * @see Spoiler#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)
     * @since 1.6.0
     */
    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent SpoilerText = Component.literal(pText)
                .setStyle(pOriginalStyle.withObfuscated(true));
        pResult.append(SpoilerText);
    }

    /**
     * Checks if the spoiler feature is enabled.
     * <p>
     * Purpose: Determines whether the spoiler feature is active based on the {@link #isSpoilerEnabled} flag.<br>
     * When: Called before processing the text to check if the feature should be applied.<br>
     * Where: Executed in {@link MarkdownFeature#apply(MutableComponent)}.<br>
     * Additional Info: This flag can be changed through {@link Spoiler#isSpoilerEnabled}.<br>
     * </p>
     *
     * @return {@code true} if the spoiler is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Spoiler#isSpoilerEnabled
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected boolean isFeatureEnabled() {
        return isSpoilerEnabled;
    }

    /**
     * Gets the feature's name.
     * <p>
     * Purpose: Provides the name of this Markdown feature for display purposes.<br>
     * When: It is called to return the feature name in logs or debugging output.<br>
     * Where: Used in the {@link MarkdownFeature#apply(MutableComponent)}.<br>
     * Additional Info: The name is returned as a simple string.<br>
     * </p>
     *
     * @return The name of the feature, which is "Spoiler".
     * @author MeAlam
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected String getFeatureName() {
        return "Spoiler";
    }

    /**
     * Sets the prefix and suffix used for spoiler formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for spoiler formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Spoiler} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for spoiler formatting.
     * @param pSuffix The new suffix for spoiler formatting.
     * @author MeAlam
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#getSuffix()
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#getPrefix()
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @since 1.5.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
    }

    /**
     * Sets the prefix for spoiler formatting.
     * <p>
     * Purpose: This method sets a new prefix for the spoiler formatting.<br>
     * When: Called when a new prefix is desired.<br>
     * Where: This method is used in the {@link Spoiler#setPrefixSuffix(String, String)} method.<br>
     * Additional Info: The prefix should be set before processing markdown text.<br>
     * </p>
     *
     * @param pPrefix The new prefix for spoiler formatting.
     * @author MeAlam
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#getSuffix()
     * @see Spoiler#getPrefix()
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @since 1.5.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
    }

    /**
     * Sets the suffix for spoiler formatting.
     * <p>
     * Purpose: This method sets a new suffix for the spoiler formatting.<br>
     * When: Called when a new suffix is desired.<br>
     * Where: This method is used in the {@link Spoiler#setPrefixSuffix(String, String)} method.<br>
     * Additional Info: The suffix should be set before processing markdown text.<br>
     * </p>
     *
     * @param pSuffix The new suffix for spoiler formatting.
     * @author MeAlam
     * @see Spoiler#getSuffix()
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#getPrefix()
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @since 1.5.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
    }

    /**
     * Retrieves the current prefix for spoiler formatting.
     * <p>
     * Purpose: This method retrieves the current prefix used for spoiler formatting.<br>
     * When: Called when the current prefix needs to be checked.<br>
     * Where: Used in {@link Spoiler#setPrefix(String)}.<br>
     * Additional Info: The value can be changed with the {@link Spoiler#setPrefix(String)} method.<br>
     * </p>
     *
     * @return The current prefix used for spoiler formatting.
     * @author MeAlam
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#getSuffix()
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @since 1.5.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Retrieves the current suffix for spoiler formatting.
     * <p>
     * Purpose: This method retrieves the current suffix used for spoiler formatting.<br>
     * When: Called when the current suffix needs to be checked.<br>
     * Where: Used in {@link Spoiler#setSuffix(String)}.<br>
     * Additional Info: The value can be changed with the {@link Spoiler#setSuffix(String)} method.<br>
     * </p>
     *
     * @return The current suffix used for spoiler formatting.
     * @author MeAlam
     * @see Spoiler#setSuffix(String)
     * @see Spoiler#setPrefixSuffix(String, String)
     * @see Spoiler#setPrefix(String)
     * @see Spoiler#getPrefix()
     * @see Spoiler#Prefix
     * @see Spoiler#Suffix
     * @since 1.5.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Retrieves whether the Spoiler feature is enabled.
     * <p>
     * Purpose: This method checks the state of the Spoiler feature.<br>
     * When: It is called to determine if Spoiler should be applied.<br>
     * Where: Used in the {@link Spoiler#isFeatureEnabled()} method.<br>
     * Additional Info: This can be controlled by the global feature flag {@link #isSpoilerEnabled}.<br>
     * </p>
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Spoiler#isSpoilerEnabled
     * @since 1.5.0
     */
    public static Boolean isSpoilerEnabled() {
        return isSpoilerEnabled;
    }
}
