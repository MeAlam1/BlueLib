// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * Handles the application of the strikethrough feature to Markdown text.
 * <p>
 * Purpose: This class is responsible for adding strikethrough formatting to text within markdown syntax.<br>
 * When: The strikethrough is applied when the text is surrounded by a specified prefix and suffix.<br>
 * Where: Executed within the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method when the feature is enabled.<br>
 * Additional Info: The default prefix and suffix are both set to "~~". The feature is controlled by the {@link Strikethrough#isStrikethroughEnabled} flag.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link Strikethrough#appendFormattedText(String, Style, MutableComponent)} - Appends the formatted text with the strikethrough style to the result component.</li>
 * <li>{@link Strikethrough#isFeatureEnabled()} - Checks if the strikethrough feature is enabled.</li>
 * <li>{@link Strikethrough#getFeatureName()} - Gets the feature's name.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Updates the prefix and suffix for Strikethrough formatting.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for Strikethrough formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for Strikethrough formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for Strikethrough formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for Strikethrough formatting.</li>
 * <li>{@link #isStrikethroughEnabled()} - Retrieves whether Strikethrough formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MarkdownFeature
 * @see Style
 * @see MutableComponent
 * @since 1.1.0
 */
public class Strikethrough extends MarkdownFeature {

    /**
     * Default prefix for strikethrough formatting.
     * <p>
     * Purpose: This variable stores the default prefix for applying strikethrough formatting.<br>
     * When: It is used when checking for the start of a strikethrough formatted section.<br>
     * Where: Used in the {@link Strikethrough#prefix} and {@link Strikethrough#suffix} logic.<br>
     * Additional Info: The default value is "~~", but it can be modified using {@link Strikethrough#setPrefix(String)} and {@link Strikethrough#setSuffix(String)}.<br>
     * </p>
     *
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#Suffix
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#getSuffix()
     * @since 1.2.0
     */
    protected static String Prefix = "~~";

    /**
     * Default suffix for strikethrough formatting.
     * <p>
     * Purpose: This variable stores the default suffix for applying strikethrough formatting.<br>
     * When: It is used when checking for the end of a strikethrough formatted section.<br>
     * Where: Used in the {@link Strikethrough#prefix} and {@link Strikethrough#suffix} logic.<br>
     * Additional Info: The default value is "~~", but it can be modified using {@link Strikethrough#setPrefix(String)} and {@link Strikethrough#setSuffix(String)}.<br>
     * </p>
     *
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#Prefix
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#getSuffix()
     * @since 1.2.0
     */
    protected static String Suffix = "~~";

    /**
     * Flag that determines whether the strikethrough feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the strikethrough feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the strikethrough should be applied.<br>
     * Where: It is used in the {@link Strikethrough#isFeatureEnabled()} and {@link Strikethrough#isStrikethroughEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see Strikethrough#isFeatureEnabled()
     * @see Strikethrough#isStrikethroughEnabled()
     * @since 1.1.0
     */
    public static Boolean isStrikethroughEnabled = true;

    /**
     * Constructor that initializes the prefix and suffix for the strikethrough feature.
     * <p>
     * Purpose: This constructor sets the default prefix and suffix values for the strikethrough formatting.<br>
     * When: It is invoked when creating an instance of the {@link Strikethrough} class.<br>
     * Where: Executed within the {@link Strikethrough} constructor.<br>
     * Additional Info: The default prefix and suffix are both set to "~~".<br>
     * </p>
     *
     * @author MeAlam
     * @see Strikethrough#Prefix
     * @see Strikethrough#Suffix
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#getSuffix()
     * @see Strikethrough#getPrefix()
     * @since 1.1.0
     */
    public Strikethrough() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * Appends the formatted text with the strikethrough style to the result component.
     * <p>
     * Purpose: This method takes the provided text and applies the strikethrough style, then appends it to the result.<br>
     * When: Called when processing a markdown string with strikethrough syntax.<br>
     * Where: Executed in {@link Strikethrough#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)}.<br>
     * Additional Info: The text is wrapped with a style that enables strikethrough.<br>
     * </p>
     *
     * @param pText          The text to be formatted with strikethrough.
     * @param pOriginalStyle The original style applied to the text.
     * @param pResult        The mutable component that the formatted text will be appended to.
     * @author MeAlam
     * @see Strikethrough#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)
     * @since 1.6.0
     */
    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent StrikethroughText = Component.literal(pText)
                .setStyle(pOriginalStyle.withStrikethrough(true));
        pResult.append(StrikethroughText);
    }

    /**
     * Checks if the strikethrough feature is enabled.
     * <p>
     * Purpose: Determines whether the strikethrough feature is active based on the {@link #isStrikethroughEnabled} flag.<br>
     * When: Called before processing the text to check if the feature should be applied.<br>
     * Where: Executed in {@link MarkdownFeature#apply(MutableComponent)}.<br>
     * Additional Info: This flag can be changed through {@link Strikethrough#isStrikethroughEnabled}.<br>
     * </p>
     *
     * @return {@code true} if strikethrough is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Strikethrough#isStrikethroughEnabled
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected boolean isFeatureEnabled() {
        return isStrikethroughEnabled;
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
     * @return The name of the feature, which is "Strikethrough".
     * @author MeAlam
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected String getFeatureName() {
        return "Strikethrough";
    }

    /**
     * Sets the prefix and suffix used for strikethrough formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for strikethrough formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Strikethrough} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for strikethrough.
     * @param pSuffix The new suffix for strikethrough.
     * @author MeAlam
     * @see Strikethrough#Suffix
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#getSuffix()
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#Prefix
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * Sets the prefix used for strikethrough formatting.
     * <p>
     * Purpose: This method allows the user to set the prefix for strikethrough formatting.<br>
     * When: It is called to customize the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix will affect all instances of the {@link Strikethrough} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for strikethrough.
     * @author MeAlam
     * @see Strikethrough#Suffix
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#getSuffix()
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#Prefix
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough prefix updated to: " + Prefix, true);
    }

    /**
     * Sets the suffix used for strikethrough formatting.
     * <p>
     * Purpose: This method allows the user to set the suffix for strikethrough formatting.<br>
     * When: It is called to customize the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated suffix will affect all instances of the {@link Strikethrough} feature.<br>
     * </p>
     *
     * @param pSuffix The new suffix for strikethrough.
     * @author MeAlam
     * @see Strikethrough#Suffix
     * @see Strikethrough#getSuffix()
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#Prefix
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Strikethrough suffix updated to: " + Suffix, true);
    }

    /**
     * Gets the prefix used for strikethrough formatting.
     * <p>
     * Purpose: This method returns the current prefix used for strikethrough formatting.<br>
     * When: It is called to retrieve the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: Returns the value of the {@link #Prefix} variable.<br>
     * </p>
     *
     * @return The current prefix for strikethrough.
     * @author MeAlam
     * @see Strikethrough#Suffix
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#getSuffix()
     * @see Strikethrough#Prefix
     * @since 1.2.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Gets the suffix used for strikethrough formatting.
     * <p>
     * Purpose: This method returns the current suffix used for strikethrough formatting.<br>
     * When: It is called to retrieve the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: Returns the value of the {@link #Suffix} variable.<br>
     * </p>
     *
     * @return The current suffix for strikethrough.
     * @author MeAlam
     * @see Strikethrough#Suffix
     * @see Strikethrough#setSuffix(String)
     * @see Strikethrough#setPrefixSuffix(String, String)
     * @see Strikethrough#setPrefix(String)
     * @see Strikethrough#getPrefix()
     * @see Strikethrough#Prefix
     * @since 1.2.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Retrieves whether the strikethrough feature is enabled.
     * <p>
     * Purpose: This method checks the state of the strikethrough feature.<br>
     * When: It is called to determine if strikethrough should be applied.<br>
     * Where: Used in the {@link Strikethrough#isFeatureEnabled()} method.<br>
     * Additional Info: This can be controlled by the global feature flag {@link #isStrikethroughEnabled}.<br>
     * </p>
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Strikethrough#isStrikethroughEnabled
     * @since 1.2.0
     */
    public static boolean isStrikethroughEnabled() {
        return isStrikethroughEnabled;
    }
}
