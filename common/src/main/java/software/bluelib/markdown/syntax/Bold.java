// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * Handles the application of the bold feature to a Markdown text.
 * <p>
 * Purpose: This class is responsible for adding bold formatting to text within markdown syntax.<br>
 * When: The bold is applied when the text is surrounded by a specified prefix and suffix.<br>
 * Where: Executed within the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method when the feature is enabled.<br>
 * Additional Info: The default prefix and suffix are both set to "**". The feature is controlled by the {@link Bold#isBoldEnabled} flag.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link Bold#appendFormattedText(String, Style, MutableComponent)} - Appends the formatted text with the bold style to the result component.</li>
 * <li>{@link Bold#isFeatureEnabled()} - Checks if the bold feature is enabled.</li>
 * <li>{@link Bold#getFeatureName()} - Gets the feature's name.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix for bold formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for bold formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for bold formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for bold formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for bold formatting.</li>
 * <li>{@link #isBoldEnabled()} - Retrieves whether bold formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @see software.bluelib.markdown.MarkdownParser
 * @see MarkdownFeature
 * @see Style
 * @see MutableComponent
 * @since 1.1.0
 */
@SuppressWarnings("unused")
public class Bold extends MarkdownFeature {

    /**
     * Default prefix for bold formatting.
     * <p>
     * Purpose: This variable stores the default prefix for applying bold formatting.<br>
     * When: It is used when checking for the start of a bold formatted section.<br>
     * Where: Used in the {@link Bold#prefix} and {@link Bold#suffix} logic.<br>
     * Additional Info: The default value is "**", but it can be modified using {@link Bold#setPrefix(String)} and {@link Bold#setSuffix(String)}.<br>
     * </p>
     *
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefix(String)
     * @see Bold#Suffix
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#getPrefix()
     * @see Bold#getSuffix()
     * @since 1.2.0
     */
    protected static String Prefix = "**";

    /**
     * Default suffix for bold formatting.
     * <p>
     * Purpose: This variable stores the default suffix for applying bold formatting.<br>
     * When: It is used when checking for the end of a bold formatted section.<br>
     * Where: Used in the {@link Bold#prefix} and {@link Bold#suffix} logic.<br>
     * Additional Info: The default value is "**", but it can be modified using {@link Bold#setPrefix(String)} and {@link Bold#setSuffix(String)}.<br>
     * </p>
     *
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefix(String)
     * @see Bold#Prefix
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#getPrefix()
     * @see Bold#getSuffix()
     * @since 1.2.0
     */
    protected static String Suffix = "**";

    /**
     * Flag that determines whether the bold feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the bold feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the bold should be applied.<br>
     * Where: It is used in the {@link Bold#isFeatureEnabled()} and {@link Bold#isBoldEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see Bold#isFeatureEnabled()
     * @see Bold#isBoldEnabled()
     * @since 1.1.0
     */
    public static Boolean isBoldEnabled = true;

    /**
     * Constructor that initializes the prefix and suffix for the bold feature.
     * <p>
     * Purpose: This constructor sets the default prefix and suffix values for the bold formatting.<br>
     * When: It is invoked when creating an instance of the {@link Bold} class.<br>
     * Where: Executed within the {@link Bold} constructor.<br>
     * Additional Info: The default prefix and suffix are both set to "**".<br>
     * </p>
     *
     * @author MeAlam
     * @see Bold#Prefix
     * @see Bold#Suffix
     * @see Bold#setPrefix(String)
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#getSuffix()
     * @see Bold#getPrefix()
     * @since 1.1.0
     */
    public Bold() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * Appends the formatted text with the bold style to the result component.
     * <p>
     * Purpose: This method takes the provided text and applies the bold style, then appends it to the result.<br>
     * When: Called when processing a markdown string with bold syntax.<br>
     * Where: Executed in {@link Bold#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)}.<br>
     * Additional Info: The text is wrapped with a style that enables bold formatting.<br>
     * </p>
     *
     * @param pText          The text to be formatted with bold.
     * @param pOriginalStyle The original style applied to the text.
     * @param pResult        The mutable component that the formatted text will be appended to.
     * @author MeAlam
     * @see Bold#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)
     * @since 1.6.0
     */
    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent boldText = Component.literal(pText)
                .setStyle(pOriginalStyle.withBold(true));
        pResult.append(boldText);
    }

    /**
     * Checks if the bold feature is enabled.
     * <p>
     * Purpose: Determines whether the bold feature is active based on the {@link #isBoldEnabled} flag.<br>
     * When: Called before processing the text to check if the feature should be applied.<br>
     * Where: Executed in {@link MarkdownFeature#apply(MutableComponent)}.<br>
     * Additional Info: This flag can be changed through {@link Bold#isBoldEnabled}.<br>
     * </p>
     *
     * @return {@code true} if bold is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Bold#isBoldEnabled
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected boolean isFeatureEnabled() {
        return isBoldEnabled;
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
     * @return The name of the feature, which is "Bold".
     * @author MeAlam
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected String getFeatureName() {
        return "Bold";
    }

    /**
     * Sets the prefix and suffix used for bold formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for bold formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Bold} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for bold.
     * @param pSuffix The new suffix for bold.
     * @author MeAlam
     * @see Bold#Suffix
     * @see Bold#setSuffix(String)
     * @see Bold#getSuffix()
     * @see Bold#setPrefix(String)
     * @see Bold#getPrefix()
     * @see Bold#Prefix
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * Sets the prefix for bold formatting.
     * <p>
     * Purpose: This method sets a new prefix for the bold formatting.<br>
     * When: Called when a new prefix is desired.<br>
     * Where: This method is used in the {@link Bold#setPrefixSuffix(String, String)} method.<br>
     * Additional Info: The prefix should be set before processing markdown text.<br>
     * </p>
     *
     * @param pPrefix The new prefix for bold formatting.
     * @author MeAlam
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#getSuffix()
     * @see Bold#getPrefix()
     * @see Bold#Prefix
     * @see Bold#Suffix
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold prefix updated to: " + Prefix, true);
    }

    /**
     * Sets the suffix for bold formatting.
     * <p>
     * Purpose: This method sets a new suffix for the bold formatting.<br>
     * When: Called when a new suffix is desired.<br>
     * Where: This method is used in the {@link Bold#setPrefixSuffix(String, String)} method.<br>
     * Additional Info: The suffix should be set before processing markdown text.<br>
     * </p>
     *
     * @param pSuffix The new suffix for bold formatting.
     * @author MeAlam
     * @see Bold#getSuffix()
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#setPrefix(String)
     * @see Bold#getPrefix()
     * @see Bold#Prefix
     * @see Bold#Suffix
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Bold suffix updated to: " + Suffix, true);
    }

    /**
     * Retrieves the current prefix for bold formatting.
     * <p>
     * Purpose: This method retrieves the current prefix used for bold formatting.<br>
     * When: Called when the current prefix needs to be checked.<br>
     * Where: Used in {@link Bold#setPrefix(String)}.<br>
     * Additional Info: The value can be changed with the {@link Bold#setPrefix(String)} method.<br>
     * </p>
     *
     * @return The current prefix used for bold formatting.
     * @author MeAlam
     * @see Bold#setPrefix(String)
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#setPrefix(String)
     * @see Bold#getSuffix()
     * @see Bold#Prefix
     * @see Bold#Suffix
     * @since 1.2.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Retrieves the current suffix for bold formatting.
     * <p>
     * Purpose: This method retrieves the current suffix used for bold formatting.<br>
     * When: Called when the current suffix needs to be checked.<br>
     * Where: Used in {@link Bold#setSuffix(String)}.<br>
     * Additional Info: The value can be changed with the {@link Bold#setSuffix(String)} method.<br>
     * </p>
     *
     * @return The current suffix used for bold formatting.
     * @author MeAlam
     * @see Bold#setSuffix(String)
     * @see Bold#setPrefixSuffix(String, String)
     * @see Bold#setPrefix(String)
     * @see Bold#getPrefix()
     * @see Bold#Prefix
     * @see Bold#Suffix
     * @since 1.2.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Retrieves whether the bold feature is enabled.
     * <p>
     * Purpose: This method checks the state of the bold feature.<br>
     * When: It is called to determine if bold should be applied.<br>
     * Where: Used in the {@link Bold#isFeatureEnabled()} method.<br>
     * Additional Info: This can be controlled by the global feature flag {@link #isBoldEnabled}.<br>
     * </p>
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Bold#isBoldEnabled
     * @since 1.2.0
     */
    public static Boolean isBoldEnabled() {
        return isBoldEnabled;
    }
}
