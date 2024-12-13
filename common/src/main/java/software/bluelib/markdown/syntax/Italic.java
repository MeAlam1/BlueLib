// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.markdown.MarkdownFeature;

/**
 * Handles the italic feature for Markdown text formatting.
 * <p>
 * Purpose: This class applies italic formatting to text within markdown syntax.<br>
 * When: The italic formatting is applied when the text is surrounded by a specified prefix and suffix.<br>
 * Where: Executed within the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method when the feature is enabled.<br>
 * Additional Info: The default prefix and suffix are both set to "*". The feature is controlled by the {@link Italic#isItalicEnabled} flag.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link Italic#appendFormattedText(String, Style, MutableComponent)} - Appends the formatted text with italic style to the result component.</li>
 * <li>{@link Italic#isFeatureEnabled()} - Checks if the italic feature is enabled.</li>
 * <li>{@link Italic#getFeatureName()} - Gets the feature's name.</li>
 * <li>{@link #setPrefix(String)} - Updates the prefix for italic formatting.</li>
 * <li>{@link #setSuffix(String)} - Updates the suffix for italic formatting.</li>
 * <li>{@link #getPrefix()} - Retrieves the current prefix for italic formatting.</li>
 * <li>{@link #getSuffix()} - Retrieves the current suffix for italic formatting.</li>
 * <li>{@link #isItalicEnabled()} - Retrieves whether italic formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MarkdownFeature
 * @see Style
 * @see MutableComponent
 * @since 1.1.0
 */
public class Italic extends MarkdownFeature {

    /**
     * Default prefix for italic formatting.
     * <p>
     * Purpose: This variable stores the default prefix for applying italic formatting.<br>
     * When: It is used when checking for the start of an italic formatted section.<br>
     * Where: Used in the {@link Italic#prefix} and {@link Italic#suffix} logic.<br>
     * Additional Info: The default value is "*", but it can be modified using {@link Italic#setPrefix(String)} and {@link Italic#setSuffix(String)}.<br>
     * </p>
     *
     * @see Italic#setSuffix(String)
     * @see Italic#setPrefix(String)
     * @see Italic#Suffix
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#getPrefix()
     * @see Italic#getSuffix()
     * @since 1.2.0
     */
    protected static String Prefix = "*";

    /**
     * Default suffix for italic formatting.
     * <p>
     * Purpose: This variable stores the default suffix for applying italic formatting.<br>
     * When: It is used when checking for the end of an italic formatted section.<br>
     * Where: Used in the {@link Italic#prefix} and {@link Italic#suffix} logic.<br>
     * Additional Info: The default value is "*", but it can be modified using {@link Italic#setPrefix(String)} and {@link Italic#setSuffix(String)}.<br>
     * </p>
     *
     * @see Italic#setSuffix(String)
     * @see Italic#setPrefix(String)
     * @see Italic#Prefix
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#getPrefix()
     * @see Italic#getSuffix()
     * @since 1.2.0
     */
    protected static String Suffix = "*";

    /**
     * Flag that determines whether the italic feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the italic feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether italic should be applied.<br>
     * Where: It is used in the {@link Italic#isFeatureEnabled()} and {@link Italic#isItalicEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see Italic#isFeatureEnabled()
     * @see Italic#isItalicEnabled()
     * @since 1.1.0
     */
    public static Boolean isItalicEnabled = true;

    /**
     * Constructor that initializes the prefix and suffix for the italic feature.
     * <p>
     * Purpose: This constructor sets the default prefix and suffix values for the italic formatting.<br>
     * When: It is invoked when creating an instance of the {@link Italic} class.<br>
     * Where: Executed within the {@link Italic} constructor.<br>
     * Additional Info: The default prefix and suffix are both set to "*".<br>
     * </p>
     *
     * @author MeAlam
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @see Italic#setPrefix(String)
     * @see Italic#setSuffix(String)
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#getSuffix()
     * @see Italic#getPrefix()
     * @since 1.1.0
     */
    public Italic() {
        prefix = Prefix;
        suffix = Suffix;
    }

    /**
     * Appends the formatted text with the italic style to the result component.
     * <p>
     * Purpose: This method takes the provided text and applies the italic style, then appends it to the result.<br>
     * When: Called when processing a markdown string with italic syntax.<br>
     * Where: Executed in {@link Italic#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)}.<br>
     * Additional Info: The text is wrapped with a style that enables italic formatting.<br>
     * </p>
     *
     * @param pText          The text to be formatted with the italic effect.
     * @param pOriginalStyle The original style applied to the text.
     * @param pResult        The mutable component that the formatted text will be appended to.
     * @author MeAlam
     * @see Italic#processComponentTextWithFormatting(String, Style, MutableComponent, java.util.regex.Pattern)
     * @since 1.6.0
     */
    @Override
    protected void appendFormattedText(String pText, Style pOriginalStyle, MutableComponent pResult) {
        MutableComponent italicText = Component.literal(pText)
                .setStyle(pOriginalStyle.withItalic(true));
        pResult.append(italicText);
    }

    /**
     * Checks if the italic feature is enabled.
     * <p>
     * Purpose: Determines whether the italic feature is active based on the {@link #isItalicEnabled} flag.<br>
     * When: Called before processing the text to check if the feature should be applied.<br>
     * Where: Executed in {@link MarkdownFeature#apply(MutableComponent)}.<br>
     * Additional Info: This flag can be changed through {@link Italic#isItalicEnabled}.<br>
     * </p>
     *
     * @return {@code true} if the italic is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Italic#isItalicEnabled
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected boolean isFeatureEnabled() {
        return isItalicEnabled;
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
     * @return The name of the feature, which is "Italic".
     * @author MeAlam
     * @see MarkdownFeature#apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected String getFeatureName() {
        return "Italic";
    }

    /**
     * Sets the prefix and suffix used for italic formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for italic formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Italic} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for italic formatting.
     * @param pSuffix The new suffix for italic formatting.
     * @author MeAlam
     * @author MeAlam
     * @see Italic#setSuffix(String)
     * @see Italic#getSuffix()
     * @see Italic#setPrefix(String)
     * @see Italic#getPrefix()
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @since 1.2.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
    }

    /**
     * Sets the prefix for italic formatting.
     * <p>
     * Purpose: This method sets a new prefix for the italic formatting.<br>
     * When: Called when a new prefix is desired.<br>
     * Where: Executed in code where customization is needed.<br>
     * Additional Info: Changes only the prefix used for italic formatting.<br>
     * </p>
     *
     * @param pPrefix The new prefix for italic formatting.
     * @author MeAlam
     * @see Italic#setSuffix(String)
     * @see Italic#getSuffix()
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#getPrefix()
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @since 1.2.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
    }

    /**
     * Sets the suffix for italic formatting.
     * <p>
     * Purpose: This method sets a new suffix for the italic formatting.<br>
     * When: Called when a new suffix is needed.<br>
     * Where: Executed when customization is desired.<br>
     * Additional Info: Changes only the suffix used for italic formatting.<br>
     * </p>
     *
     * @param pSuffix The new suffix for italic formatting.
     * @author MeAlam
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#getSuffix()
     * @see Italic#setPrefix(String)
     * @see Italic#getPrefix()
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @since 1.2.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
    }

    /**
     * Retrieves the current prefix for italic formatting.
     * <p>
     * Purpose: This method returns the current prefix used for italic formatting.<br>
     * When: Called when the current prefix value is needed.<br>
     * Where: Executed when the prefix is required for formatting.<br>
     * Additional Info: This value can be customized through {@link Italic#setPrefix(String)}.<br>
     * </p>
     *
     * @return The current prefix used for italic formatting.
     * @author MeAlam
     * @see Italic#setSuffix(String)
     * @see Italic#getSuffix()
     * @see Italic#setPrefix(String)
     * @see Italic#setPrefixSuffix(String, String) ()
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @since 1.2.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Retrieves the current suffix for italic formatting.
     * <p>
     * Purpose: This method returns the current suffix used for italic formatting.<br>
     * When: Called when the current suffix value is needed.<br>
     * Where: Executed when the suffix is required for formatting.<br>
     * Additional Info: This value can be customized through {@link Italic#setSuffix(String)}.<br>
     * </p>
     *
     * @return The current suffix used for italic formatting.
     * @author MeAlam
     * @see Italic#setSuffix(String)
     * @see Italic#setPrefixSuffix(String, String)
     * @see Italic#setPrefix(String)
     * @see Italic#getPrefix()
     * @see Italic#Prefix
     * @see Italic#Suffix
     * @since 1.2.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Checks if the italic formatting is enabled.
     * <p>
     * Purpose: This method checks whether italic formatting is enabled or disabled.<br>
     * When: Called when determining whether to apply italic formatting.<br>
     * Where: Executed in logic that processes markdown.<br>
     * Additional Info: It uses the global flag {@link Italic#isItalicEnabled} to determine the state.<br>
     * </p>
     *
     * @return {@code true} if italic formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Italic#isItalicEnabled
     * @since 1.2.0
     */
    public static boolean isItalicEnabled() {
        return isItalicEnabled;
    }
}
