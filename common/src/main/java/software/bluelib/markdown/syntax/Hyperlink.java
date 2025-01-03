// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import java.util.regex.Pattern;
import net.minecraft.network.chat.*;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.IsValidUtils;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A class for handling hyperlink Markdown formatting.
 * <p>
 * Purpose: This class applies hyperlink formatting to the content of a {@link MutableComponent} based on the feature's {@link #Prefix} and {@link #Suffix}.<br>
 * When: The hyperlink formatting is applied when the {@link #apply(MutableComponent)} method is called.<br>
 * Where: The formatting is applied to the content of the component and its siblings, if any.<br>
 * Additional Info: This class supports handling hyperlink Markdown features and can process both single text and text with components.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #apply(MutableComponent)} - Applies the hyperlink feature to a given component.</li>
 * <li>{@link #processComponentTextWithHyperlinks(String, Style, MutableComponent, Pattern)} - Processes text with hyperlink formatting.</li>
 * <li>{@link #processSiblingsWithHyperlinks(MutableComponent, Pattern)} - Processes siblings with hyperlink formatting.</li>
 * <li>{@link #appendHyperlink(String, String, Style, MutableComponent)} - Appends hyperlink formatted text to a component.</li>
 * <li>{@link #isFeatureEnabled()} - Checks if the feature is enabled.</li>
 * <li>{@link #getFeatureName()} - Gets the name of the feature.</li>
 * <li>{@link #setPrefixSuffix(String, String)} - Sets the prefix and suffix for hyperlink formatting.</li>
 * <li>{@link #setPrefix(String)} - Sets the prefix for hyperlink formatting.</li>
 * <li>{@link #setSuffix(String)} - Sets the suffix for hyperlink formatting.</li>
 * <li>{@link #getPrefix()} - Gets the prefix for hyperlink formatting.</li>
 * <li>{@link #getSuffix()} - Gets the suffix for hyperlink formatting.</li>
 * <li>{@link #isHyperlinkEnabled()} - Checks if the hyperlink feature is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @see MutableComponent
 * @see Pattern
 * @see Style
 * @since 1.4.0
 */
@SuppressWarnings("unused")
public class Hyperlink extends MarkdownFeature {

    /**
     * The prefix for the hyperlink feature.
     * <p>
     * Purpose: This field stores the prefix for the hyperlink feature, which is used to identify the start of the formatted text.<br>
     * When: The prefix is used to identify the start of the formatted text in the content.<br>
     * Where: It is used in the {@link #apply(MutableComponent)} method to apply the hyperlink feature.<br>
     * Additional Info: The prefix is used to identify the start of the formatted text, which is then processed accordingly.<br>
     * </p>
     *
     * @see #Suffix
     * @see #apply(MutableComponent)
     * @see #getSuffix()
     * @see #setSuffix(String)
     * @see #setPrefixSuffix(String, String)
     * @see #getPrefix()
     * @see #setPrefix(String)
     * @since 1.4.0
     */
    protected static String Prefix = "[";

    /**
     * The suffix for the hyperlink feature.
     * <p>
     * Purpose: This field stores the suffix for the hyperlink feature, which is used to identify the end of the formatted text.<br>
     * When: The suffix is used to identify the end of the formatted text in the content.<br>
     * Where: It is used in the {@link #apply(MutableComponent)} method to apply the hyperlink feature.<br>
     * Additional Info: The suffix is used to identify the end of the formatted text, which is then processed accordingly.<br>
     * </p>
     *
     * @see #Prefix
     * @see #apply(MutableComponent)
     * @see #getSuffix()
     * @see #setSuffix(String)
     * @see #setPrefixSuffix(String, String)
     * @see #getPrefix()
     * @see #setPrefix(String)
     * @since 1.4.0
     */
    protected static String Suffix = "]";

    /**
     * Flag that determines whether the hyperlink feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the hyperlink feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the hyperlink should be applied.<br>
     * Where: It is used in the {@link #isFeatureEnabled()} and {@link #isHyperlinkEnabled()} methods.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see #isFeatureEnabled()
     * @see #isHyperlinkEnabled()
     * @since 1.4.0
     */
    public static Boolean isHyperlinkEnabled = true;

    /**
     * Applies the hyperlink feature to the provided component.
     * <p>
     * Purpose: This method checks whether the feature is enabled and applies the corresponding formatting to the given {@link MutableComponent}. If the feature is disabled, it logs an informational message and returns the original component.<br>
     * When: The method is called when the hyperlink feature needs to be applied to a component.<br>
     * Where: It is called in the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method.<br>
     * Additional Info: The method uses a {@link Pattern} to find text matching the specified prefix and suffix for hyperlink formatting.<br>
     * </p>
     *
     * @param pComponent The component to apply hyperlink formatting to.
     * @return The component with the applied hyperlink formatting.
     * @author MeAlam
     * @see MutableComponent
     * @see Pattern
     * @see software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)
     * @since 1.6.0
     */
    public MutableComponent apply(MutableComponent pComponent) {
        if (!isHyperlinkEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Hyperlink formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(getPrefix()) + "(.*?)" + Pattern.quote(getSuffix()) + "\\((.*?)\\)");

        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithHyperlinks(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithHyperlinks(pComponent, pattern);
        }

        return result;
    }

    /**
     * Processes the text of a component with hyperlink formatting applied.
     * <p>
     * Purpose: This method processes the text of a component by applying specific formatting when the hyperlink pattern is matched.<br>
     * When: This method is called within {@link #processComponentText} to handle hyperlink formatting of text.<br>
     * Where: It is invoked within the {@link #apply} to process the text of a component.<br>
     * Additional Info: The method delegates to {@link #appendHyperlink} to apply the formatting for matched text.<br>
     * </p>
     *
     * @param pText          The text to be processed.
     * @param pOriginalStyle The original style of the component.
     * @param pResult        The component to append the processed text to.
     * @param pPattern       The pattern used to match hyperlink formatting.
     * @author MeAlam
     * @see #processComponentText
     * @see #appendHyperlink(String, String, Style, MutableComponent)
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    protected void processComponentTextWithHyperlinks(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> {
                    String url = matcher.group(2);
                    if (url != null && !url.isEmpty()) {
                        appendHyperlink(matcher.group(1), url, pOriginalStyle, res);
                    }
                });
    }

    /**
     * Processes the siblings of a component with hyperlink formatting.
     * <p>
     * Purpose: This method applies hyperlink formatting to the siblings of a component.<br>
     * When: This method is called to process all siblings of a component in hyperlink formatting.<br>
     * Where: It is called from {@link #apply(MutableComponent)} and related methods.<br>
     * Additional Info: The method iterates over all siblings and applies formatting to each of them individually.<br>
     * </p>
     *
     * @param pComponent The component whose siblings will be processed.
     * @param pPattern   The pattern used to match hyperlink formatting.
     * @return The component with formatted siblings.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    public MutableComponent processSiblingsWithHyperlinks(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithHyperlinks);
    }

    /**
     * Appends hyperlink formatted text to a component.
     * <p>
     * Purpose: This method appends hyperlink formatted text to a component, applying the specified style.<br>
     * When: This method is called when the hyperlink feature needs to format and append text.<br>
     * Where: It is invoked in {@link #processComponentTextWithHyperlinks} to handle formatted text.<br>
     * Additional Info: The method ensures that the appropriate style is applied to the appended text.<br>
     * </p>
     *
     * @param pText          The text to be appended.
     * @param pUrl           The URL to be linked.
     * @param pOriginalStyle The original style of the component.
     * @param pResult        The component to append the formatted text to.
     * @author MeAlam
     * @see Style
     * @see MutableComponent
     * @see TextColor
     * @see Component
     * @see ClickEvent.Action#OPEN_URL
     * @see TextColor#fromRgb(int)
     * @see #processComponentTextWithHyperlinks(String, Style, MutableComponent, Pattern)
     * @since 1.6.0
     */
    private void appendHyperlink(String pText, String pUrl, Style pOriginalStyle, MutableComponent pResult) {
        if (!IsValidUtils.isValidURL(pUrl)) {
            pResult.append(Component.literal(getPrefix() + pText + getSuffix() + "(" + pUrl + ")").setStyle(pOriginalStyle));
            return;
        }

        MutableComponent hyperlink = Component.literal(pText)
                .setStyle(pOriginalStyle
                        .withColor(TextColor.fromRgb(0x1F5FE1))
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, pUrl)));

        pResult.append(hyperlink);
    }

    /**
     * Checks if the hyperlink feature is enabled.
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
        return isHyperlinkEnabled;
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
        return "Hyperlink";
    }

    /**
     * Sets the prefix and suffix used for hyperlink formatting.
     * <p>
     * Purpose: This method allows the user to set both the prefix and suffix for hyperlink formatting.<br>
     * When: It is called to customize the prefix and suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix and suffix will affect all instances of the {@link Hyperlink} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for hyperlink.
     * @param pSuffix The new suffix for hyperlink.
     * @author MeAlam
     * @see #setSuffix(String)
     * @see #getSuffix()
     * @see #getPrefix()
     * @see #setPrefix(String)
     * @see #Suffix
     * @see #Prefix
     * @since 1.4.0
     */
    public static void setPrefixSuffix(String pPrefix, String pSuffix) {
        Prefix = pPrefix;
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix and suffix updated to: " + Prefix + " and " + Suffix, true);
    }

    /**
     * Sets the prefix used for hyperlink formatting.
     * <p>
     * Purpose: This method allows the user to set the prefix for hyperlink formatting.<br>
     * When: It is called to customize the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated prefix will affect all instances of the {@link Hyperlink} feature.<br>
     * </p>
     *
     * @param pPrefix The new prefix for hyperlink.
     * @author MeAlam
     * @see #setSuffix(String)
     * @see #setPrefixSuffix(String, String)
     * @see #getPrefix()
     * @see #getSuffix()
     * @see #Suffix
     * @see #Prefix
     * @since 1.4.0
     */
    public static void setPrefix(String pPrefix) {
        Prefix = pPrefix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink prefix updated to: " + Prefix, true);
    }

    /**
     * Sets the suffix used for hyperlink formatting.
     * <p>
     * Purpose: This method allows the user to set the suffix for hyperlink formatting.<br>
     * When: It is called to customize the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: The updated suffix will affect all instances of the {@link Hyperlink} feature.<br>
     * </p>
     *
     * @param pSuffix The new suffix for hyperlink.
     * @author MeAlam
     * @see #getSuffix()
     * @see #setPrefixSuffix(String, String)
     * @see #getPrefix()
     * @see #setPrefix(String)
     * @see #Suffix
     * @see #Prefix
     * @since 1.4.0
     */
    public static void setSuffix(String pSuffix) {
        Suffix = pSuffix;
        BaseLogger.log(BaseLogLevel.SUCCESS, "Hyperlink suffix updated to: " + Suffix, true);
    }

    /**
     * Gets the prefix used for hyperlink formatting.
     * <p>
     * Purpose: This method returns the current prefix used for hyperlink formatting.<br>
     * When: It is called to retrieve the prefix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This returns the default or custom prefix depending on prior settings.<br>
     * </p>
     *
     * @return The current prefix used for hyperlink.
     * @author MeAlam
     * @see #setSuffix(String)
     * @see #setPrefixSuffix(String, String)
     * @see #getSuffix()
     * @see #setPrefix(String)
     * @see #Suffix
     * @see #Prefix
     * @since 1.4.0
     */
    public static String getPrefix() {
        return Prefix;
    }

    /**
     * Gets the suffix used for hyperlink formatting.
     * <p>
     * Purpose: This method returns the current suffix used for hyperlink formatting.<br>
     * When: It is called to retrieve the suffix.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This returns the default or custom suffix depending on prior settings.<br>
     * </p>
     *
     * @return The current suffix used for hyperlink.
     * @author MeAlam
     * @see #setSuffix(String)
     * @see #setPrefixSuffix(String, String)
     * @see #getPrefix()
     * @see #setPrefix(String)
     * @see #Suffix
     * @see #Prefix
     * @since 1.4.0
     */
    public static String getSuffix() {
        return Suffix;
    }

    /**
     * Checks if the hyperlink feature is enabled.
     * <p>
     * Purpose: This method returns the current state of the hyperlink feature (enabled or disabled).<br>
     * When: It is called to check if the hyperlink feature is enabled.<br>
     * Where: Can be invoked from any class or method.<br>
     * Additional Info: This method reflects the current state of the {@link #isHyperlinkEnabled} flag.<br>
     * </p>
     *
     * @return {@code true} if the hyperlink feature is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see #isHyperlinkEnabled
     * @since 1.4.0
     */
    public static Boolean isHyperlinkEnabled() {
        return isHyperlinkEnabled;
    }
}
