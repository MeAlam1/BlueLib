// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.IsValidUtils;
import software.bluelib.utils.conversion.ColorConversionUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A class for handling color formatting in Markdown.
 * <p>
 * Purpose: This class applies color formatting to the content of a {@link MutableComponent} based on the feature's {@link #Prefix} and {@link #Suffix}.<br>
 * When: The color formatting is applied when the {@link #apply(MutableComponent)} method is called.<br>
 * Where: The formatting is applied to the content of the component and its siblings, if any.<br>
 * Additional Info: This class supports handling color formatting in Markdown and can process both single text and text with components.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #apply(MutableComponent)} - Applies the color feature to a given component.</li>
 * <li>{@link #processComponentTextWithColors(String, Style, MutableComponent, Pattern)} - Processes text with color formatting.</li>
 * <li>{@link #processSiblingsWithColors(MutableComponent, Pattern)} - Processes siblings with color formatting.</li>
 * <li>{@link #appendColor(String, String, Style, MutableComponent)} - Appends color formatted text to a component.</li>
 * <li>{@link #isFeatureEnabled()} - Checks if the feature is enabled.</li>
 * <li>{@link #getFeatureName()} - Gets the name of the feature.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Sets the prefix and suffix for color formatting.</li>
 * <li>{@link #setPrefix(String)} - Sets the prefix for color formatting.</li>
 * <li>{@link #setSuffix(String)} - Sets the suffix for color formatting.</li>
 * <li>{@link #getPrefix()} - Gets the prefix for color formatting.</li>
 * <li>{@link #getSuffix()} - Gets the suffix for color formatting.</li>
 * <li>{@link #isColorEnabled()} - Checks if the color feature is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @see MutableComponent
 * @see Pattern
 * @see Style
 * @see MarkdownFeature
 * @see ColorConversionUtils
 * @see BaseLogger
 * @see BaseLogLevel
 * @since 1.6.0
 */
@SuppressWarnings("unused")
public class Color extends MarkdownFeature {

    /**
     * Default prefix for color formatting.
     * <p>
     * Purpose: This variable stores the default prefix for applying color formatting.<br>
     * When: It is used when checking for the start of a color formatted section.<br>
     * Where: Used in several methods to determine the start of a color formatted section.<br>
     * Additional Info: The default value is "-", but it can be modified using {@link Color#setPrefix(String)} and {@link Color#setSuffix(String)}.<br>
     * </p>
     *
     * @see Color#setSuffix(String)
     * @see Color#setPrefix(String)
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#getSuffix()
     * @see Color#getPrefix()
     * @since 1.6.0
     */
    protected static String Prefix = "-";

    /**
     * Default suffix for color formatting.
     * <p>
     * Purpose: This variable stores the default suffix for applying color formatting.<br>
     * When: It is used when checking for the end of a color formatted section.<br>
     * Where: Used in several methods to determine the start of a color formatted section.<br>
     * Additional Info: The default value is "-", but it can be modified using {@link Color#setPrefix(String)} and {@link Color#setSuffix(String)}.<br>
     * </p>
     *
     * @see Color#setSuffix(String)
     * @see Color#setPrefix(String)
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#getSuffix()
     * @see Color#getPrefix()
     * @since 1.6.0
     */
    protected static String Suffix = "-";

    /**
     * Flag that determines whether the color feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the color feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the color should be applied.<br>
     * Where: It is used in the {@link Color#isFeatureEnabled()} and {@link Color#isColorEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see Color#isFeatureEnabled()
     * @see Color#isColorEnabled()
     * @since 1.6.0
     */
    public static Boolean isColorEnabled = true;

    /**
     * Applies the color feature to the provided component.
     * <p>
     * Purpose: This method checks whether the feature is enabled and applies the corresponding formatting to the given {@link MutableComponent}. If the feature is disabled, it logs an informational message and returns the original component.<br>
     * When: The method is called when the color feature needs to be applied to a component.<br>
     * Where: It is called in the {@link MarkdownFeature#apply(MutableComponent)} method.<br>
     * Additional Info: The method uses a {@link Pattern} to find text matching the specified prefix and suffix for color formatting.<br>
     * </p>
     *
     * @param pComponent The component to apply color formatting to.
     * @return The component with the applied color formatting.
     * @author MeAlam
     * @see MarkdownFeature#apply(MutableComponent)
     * @see MutableComponent
     * @see Pattern
     * @since 1.6.0
     */
    public MutableComponent apply(MutableComponent pComponent) {
        if (!isColorEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Color formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(getPrefix()) + "(#[0-9A-Fa-f]{6})" + Pattern.quote(getSuffix()) + "\\((.*?)\\)");

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithColors(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithColors(pComponent, pattern);
        }

        return result;
    }

    /**
     * Processes the text of a component with color formatting applied.
     * <p>
     * Purpose: This method processes the text of a component by applying specific formatting when the color pattern is matched.<br>
     * When: This method is called within {@link #processComponentText} to handle color formatting of text.<br>
     * Where: It is invoked within the {@link #apply} to process the text of a component.<br>
     * Additional Info: The method delegates to {@link #appendColor} to apply the formatting for matched text.<br>
     * </p>
     *
     * @param pText          The text to be processed.
     * @param pOriginalStyle The original style of the component.
     * @param pResult        The component to append the processed text to.
     * @param pPattern       The pattern used to match color formatting.
     * @author MeAlam
     * @see #processComponentText
     * @see #appendColor
     * @see #apply
     * @see Style
     * @see Pattern
     * @since 1.6.0
     */
    protected void processComponentTextWithColors(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> {
                    String color = matcher.group(1);
                    String colorText = matcher.group(2);
                    if (color != null && !color.isEmpty()) {
                        appendColor(colorText, color, pOriginalStyle, res);
                    }
                });
    }

    /**
     * Appends color formatted text to a component.
     * <p>
     * Purpose: This method appends color formatted text to a component, applying the specified style.<br>
     * When: This method is called when the color feature needs to format and append text.<br>
     * Where: It is invoked in {@link #processComponentTextWithColors} to handle formatted text.<br>
     * Additional Info: The method ensures that the appropriate style is applied to the appended text.<br>
     * </p>
     *
     * @param colorText      The text to be appended.
     * @param pColor         The color to be applied to the text.
     * @param pOriginalStyle The original style of the component.
     * @param pResult        The component to append the formatted text to.
     * @author MeAlam
     * @see #processComponentTextWithColors
     * @see Style
     * @see MutableComponent
     * @see TextColor
     * @see Component
     * @see TextColor#fromRgb(int)
     * @since 1.6.0
     */
    private void appendColor(String colorText, String pColor, Style pOriginalStyle, MutableComponent pResult) {
        if (IsValidUtils.isValidColor(pColor)) {
            pResult.append(Component.literal(colorText)
                    .setStyle(pOriginalStyle.withColor(TextColor.fromRgb(ColorConversionUtils.parseColorToHexString(pColor)))));
        } else {
            pResult.append(Component.literal(colorText).setStyle(pOriginalStyle));
        }
    }

    /**
     * Processes the siblings of a component with color formatting.
     * <p>
     * Purpose: This method applies color formatting to the siblings of a component.<br>
     * When: This method is called to process all siblings of a component in color formatting.<br>
     * Where: It is called from {@link #apply(MutableComponent)} and related methods.<br>
     * Additional Info: The method iterates over all siblings and applies formatting to each of them individually.<br>
     * </p>
     *
     * @param pComponent The component whose siblings will be processed.
     * @param pPattern   The pattern used to match color formatting.
     * @return The component with formatted siblings.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    public MutableComponent processSiblingsWithColors(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithColors);
    }

    /**
     * Checks if the color feature is enabled.
     * <p>
     * Purpose: This method checks if the feature is enabled and returns the result.<br>
     * When: It is called to determine if the feature is enabled.<br>
     * Where: It is invoked in {@link #apply(MutableComponent)} to check if the feature is enabled.<br>
     * Additional Info: The method is overridden in subclasses to check the specific feature's status.<br>
     * </p>
     *
     * @return {@code true} if the feature is enabled; {@code false} otherwise.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected boolean isFeatureEnabled() {
        return isColorEnabled;
    }

    /**
     * Gets the name of the feature.
     * <p>
     * Purpose: This method returns the name of the feature.<br>
     * When: It is called to get the name of the feature.<br>
     * Where: It is invoked in {@link #apply(MutableComponent)} to log an informational message.<br>
     * Additional Info: The method is overridden in subclasses to return the specific feature's name.<br>
     * </p>
     *
     * @return The name of the feature.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    @Override
    protected String getFeatureName() {
        return "Color";
    }

    /**
     * Sets the prefix and suffix used for color formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for color formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Color} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for color.
     * @param pSuffix The new suffix for color.
     * @author MeAlam
     * @see Color#setSuffix(String)
     * @see Color#getSuffix()
     * @see Color#setPrefix(String)
     * @see Color#getPrefix()
     * @see Color#Suffix
     * @see Color#Prefix
     * @since 1.6.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * Sets the prefix used for color formatting.
     * <p>
     * Purpose: This method allows the user to set the prefix for color formatting.<br>
     * When: It is called to customize the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix will affect all instances of the {@link Color} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for color.
     * @author MeAlam
     * @see Color#setSuffix(String)
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#getSuffix()
     * @see Color#getPrefix()
     * @see Color#Suffix
     * @see Color#Prefix
     * @since 1.6.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color prefix updated to: " + Prefix, true);
    }

    /**
     * Sets the suffix used for color formatting.
     * <p>
     * Purpose: This method allows the user to set the suffix for color formatting.<br>
     * When: It is called to customize the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated suffix will affect all instances of the {@link Color} feature.<br>
     * </p>
     *
     * @param pSuffix The new suffix for color.
     * @author MeAlam
     * @see Color#getSuffix()
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#setPrefix(String)
     * @see Color#getPrefix()
     * @see Color#Suffix
     * @see Color#Prefix
     * @since 1.6.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Color suffix updated to: " + Suffix, true);
    }

    /**
     * Gets the prefix used for color formatting.
     * <p>
     * Purpose: This method returns the current prefix used for color formatting.<br>
     * When: It is called to retrieve the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This returns the default or custom prefix depending on prior settings.<br>
     * </p>
     *
     * @return The current prefix used for color.
     * @author MeAlam
     * @see Color#setSuffix(String)
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#setPrefix(String)
     * @see Color#getSuffix()
     * @see Color#Suffix
     * @see Color#Prefix
     * @since 1.6.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Gets the suffix used for color formatting.
     * <p>
     * Purpose: This method returns the current suffix used for color formatting.<br>
     * When: It is called to retrieve the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This returns the default or custom suffix depending on prior settings.<br>
     * </p>
     *
     * @return The current suffix used for color.
     * @author MeAlam
     * @see Color#setSuffix(String)
     * @see Color#setPrefixSuffix(String, String)
     * @see Color#setPrefix(String)
     * @see Color#getPrefix()
     * @see Color#Suffix
     * @see Color#Prefix
     * @since 1.6.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Checks if the color feature is enabled.
     * <p>
     * Purpose: This method returns the current state of the color feature (enabled or disabled).<br>
     * When: It is called to check if the color feature is enabled.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This method reflects the current state of the {@link #isColorEnabled} flag.<br>
     * </p>
     *
     * @return {@code true} if the color feature is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see Color#isColorEnabled
     * @since 1.6.0
     */
    public static Boolean isColorEnabled() {
        return isColorEnabled;
    }
}
