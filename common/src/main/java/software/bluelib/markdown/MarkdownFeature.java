// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import software.bluelib.utils.QuadConsumer;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public abstract class} for handling Markdown formatting features.
 * <p>
 * Purpose: This class applies specific formatting to the content of a {@link MutableComponent} based on the feature's {@link #prefix} and {@link #suffix}. It handles both plain text and components with siblings.<br>
 * When: The Markdown formatting is applied when the {@link #apply(MutableComponent)} method is called.<br>
 * Where: The formatting is applied to the content of the component and its siblings, if any.<br>
 * Additional Info: This class supports handling different types of Markdown features (e.g., bold, italic) and can process both single text and text with components.<br>
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #apply(MutableComponent)} - Applies the Markdown feature to a given component.</li>
 * <li>{@link #processComponentText(String, Style, MutableComponent, Pattern, BiConsumer)} - Processes text with Markdown formatting.</li>
 * <li>{@link #processComponentTextWithFormatting(String, Style, MutableComponent, Pattern)} - Processes text with specific formatting.</li>
 * <li>{@link #processSiblings(MutableComponent, Pattern, QuadConsumer)} - Processes siblings of a component with Markdown formatting.</li>
 * <li>{@link #processSiblingsWithFormatting(MutableComponent, Pattern)} - Processes siblings with specific Markdown formatting.</li>
 * <li>{@link #appendFormattedText(String, Style, MutableComponent)} - Appends formatted text to a component.</li>
 * <li>{@link #appendUnstyledText(String, MutableComponent, Style)} - Appends unstyled text to a component.</li>
 * <li>{@link #isFeatureEnabled()} - Checks if the feature is enabled.</li>
 * <li>{@link #getFeatureName()} - Gets the name of the feature.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MutableComponent
 * @see Pattern
 * @see Style
 * @see QuadConsumer
 * @see BiConsumer
 * @since 1.1.0
 */
public abstract class MarkdownFeature {

    /**
     * The prefix for the Markdown feature.
     * <p>
     * Purpose: This field stores the prefix for the Markdown feature, which is used to identify the start of the formatted text.<br>
     * When: The prefix is used to identify the start of the formatted text in the content.<br>
     * Where: It is used in the {@link #apply(MutableComponent)} method to apply the Markdown feature.<br>
     * Additional Info: The prefix is used to identify the start of the formatted text, which is then processed accordingly.<br>
     * </p>
     *
     * @since 1.1.0
     */
    protected String prefix;

    /**
     * The suffix for the Markdown feature.
     * <p>
     * Purpose: This field stores the suffix for the Markdown feature, which is used to identify the end of the formatted text.<br>
     * When: The suffix is used to identify the end of the formatted text in the content.<br>
     * Where: It is used in the {@link #apply(MutableComponent)} method to apply the Markdown feature.<br>
     * Additional Info: The suffix is used to identify the end of the formatted text, which is then processed accordingly.<br>
     * </p>
     *
     * @since 1.1.0
     */
    protected String suffix;

    /**
     * Applies the Markdown feature to the provided component.
     * <p>
     * Purpose: This method checks whether the feature is enabled and applies the corresponding formatting to the given {@link MutableComponent}. If the feature is disabled, it logs an informational message and returns the original component.<br>
     * When: The method is called when the Markdown feature needs to be applied to a component.<br>
     * Where: It is called in the {@link MarkdownParser#parseMarkdown(Component)} method.<br>
     * Additional Info: The method uses a {@link Pattern} to find text matching the specified prefix and suffix for Markdown formatting.<br>
     * </p>
     *
     * @param pComponent The component to apply Markdown formatting to.
     * @return The component with the applied Markdown formatting.
     * @author MeAlam
     * @see MarkdownParser#parseMarkdown(Component)
     * @see MutableComponent
     * @see Pattern
     * @since 1.6.0
     */
    public MutableComponent apply(MutableComponent pComponent) {
        if (!isFeatureEnabled()) {
            BaseLogger.log(BaseLogLevel.INFO, getFeatureName() + " formatting is disabled. Returning original content.", true);
            return pComponent;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "(.*?)" + Pattern.quote(suffix));
        MutableComponent result = Component.empty();

        if (pComponent.getSiblings().isEmpty()) {
            processComponentTextWithFormatting(pComponent.getString(), pComponent.getStyle(), result, pattern);
        } else {
            result = processSiblingsWithFormatting(pComponent, pattern);
        }

        return result;
    }

    /**
     * Processes the text of a component using Markdown formatting.
     * <p>
     * Purpose: This method applies Markdown formatting to the text in a {@link MutableComponent} based on a specified pattern and special text handler.<br>
     * When: This method is called when processing the text of a component for Markdown formatting.<br>
     * Where: It is invoked in {@link #apply(MutableComponent)} or related methods to process the component's text.<br>
     * Additional Info: This method handles escaping of special characters and applies formatting only to text matching the pattern.<br>
     * </p>
     *
     * @param pText               The text to be processed.
     * @param pOriginalStyle      The original style of the component.
     * @param pResult             The component to append the processed text to.
     * @param pPattern            The pattern used to match Markdown formatting.
     * @param pSpecialTextHandler A handler to apply special formatting.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @see MutableComponent
     * @since 1.6.0
     */
    protected void processComponentText(
            String pText,
            Style pOriginalStyle,
            MutableComponent pResult,
            Pattern pPattern,
            BiConsumer<Matcher, MutableComponent> pSpecialTextHandler) {
        Matcher matcher = pPattern.matcher(pText);
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.group(1).isEmpty()) {
                appendUnstyledText(pText.substring(lastIndex, matcher.end()), pResult, pOriginalStyle);
            } else if (matcher.start() > 0 && pText.charAt(matcher.start() - 1) == '\\') {
                appendUnstyledText(pText.substring(lastIndex, matcher.start() - 1), pResult, pOriginalStyle);
                appendUnstyledText(matcher.group(0), pResult, pOriginalStyle);
            } else {
                appendUnstyledText(pText.substring(lastIndex, matcher.start()), pResult, pOriginalStyle);
                pSpecialTextHandler.accept(matcher, pResult);
            }
            lastIndex = matcher.end();
        }

        appendUnstyledText(pText.substring(lastIndex), pResult, pOriginalStyle);
    }

    /**
     * Processes the text of a component with Markdown formatting applied.
     * <p>
     * Purpose: This method processes the text of a component by applying specific formatting when the Markdown pattern is matched.<br>
     * When: This method is called within {@link #processComponentText} to handle Markdown formatting of text.<br>
     * Where: It is invoked within the {@link #apply} to process the text of a component.<br>
     * Additional Info: The method delegates to {@link #appendFormattedText} to apply the formatting for matched text.<br>
     * </p>
     *
     * @param pText          The text to be processed.
     * @param pOriginalStyle The original style of the component.
     * @param pResult        The component to append the processed text to.
     * @param pPattern       The pattern used to match Markdown formatting.
     * @author MeAlam
     * @see #processComponentText
     * @see #appendFormattedText
     * @see #apply
     * @see Style
     * @see Pattern
     * @since 1.6.0
     */
    protected void processComponentTextWithFormatting(String pText, Style pOriginalStyle, MutableComponent pResult, Pattern pPattern) {
        processComponentText(pText, pOriginalStyle, pResult, pPattern,
                (matcher, res) -> appendFormattedText(matcher.group(1), pOriginalStyle, res));
    }

    /**
     * Processes the siblings of a component with Markdown formatting.
     * <p>
     * Purpose: This method applies Markdown formatting to the siblings of a component.<br>
     * When: This method is called to process all siblings of a component in Markdown formatting.<br>
     * Where: It is called from {@link #apply(MutableComponent)} and related methods.<br>
     * Additional Info: The method iterates over all siblings and applies formatting to each of them individually.<br>
     * </p>
     *
     * @param pComponent        The component whose siblings will be processed.
     * @param pPattern          The pattern used to match Markdown formatting.
     * @param pSiblingProcessor A consumer for processing each sibling's text with Markdown formatting.
     * @return The component with formatted siblings.
     * @author MeAlam
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    protected MutableComponent processSiblings(
            MutableComponent pComponent,
            Pattern pPattern,
            QuadConsumer<String, Style, MutableComponent, Pattern> pSiblingProcessor) {
        MutableComponent result = Component.empty();

        for (Component sibling : pComponent.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                pSiblingProcessor.accept(
                        mutableSibling.getString(),
                        mutableSibling.getStyle(),
                        result,
                        pPattern);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    /**
     * Processes the siblings of a component with specific Markdown formatting applied.
     * <p>
     * Purpose: This method applies Markdown formatting to all siblings of a component.<br>
     * When: It is called to apply formatting to the siblings of the component in the {@link #apply(MutableComponent)} method.<br>
     * Where: It is invoked from {@link #processSiblings} to handle sibling components.<br>
     * Additional Info: The method uses {@link #processComponentTextWithFormatting} to handle the text formatting.<br>
     * </p>
     *
     * @param pComponent The component whose siblings will be formatted.
     * @param pPattern   The pattern used to match Markdown formatting.
     * @return The component with formatted siblings.
     * @author MeAlam
     * @see #processSiblings
     * @see #processComponentTextWithFormatting
     * @see #apply(MutableComponent)
     * @since 1.6.0
     */
    protected MutableComponent processSiblingsWithFormatting(MutableComponent pComponent, Pattern pPattern) {
        return processSiblings(pComponent, pPattern,
                this::processComponentTextWithFormatting);
    }

    /**
     * Appends formatted text to a component.
     * <p>
     * Purpose: This method appends formatted text to a component, applying the specified style.<br>
     * When: This method is called when the Markdown feature needs to format and append text.<br>
     * Where: It is invoked in {@link #processComponentTextWithFormatting} to handle formatted text.<br>
     * Additional Info: The method ensures that the appropriate style is applied to the appended text.<br>
     * </p>
     *
     * @param pText   The text to be appended.
     * @param pStyle  The style to be applied to the text.
     * @param pResult The component to append the formatted text to.
     * @author MeAlam
     * @see #processComponentTextWithFormatting
     * @since 1.6.0
     */
    protected void appendFormattedText(String pText, Style pStyle, MutableComponent pResult) {
        pResult.append(Component.literal(pText).setStyle(pStyle));
    }

    /**
     * Appends unstyled text to a component.
     * <p>
     * Purpose: This method appends unstyled text to a component.<br>
     * When: This method is called when plain, unstyled text needs to be appended.<br>
     * Where: It is invoked within other methods like {@link #processComponentText} to append plain text.<br>
     * Additional Info: The method adds the unstyled text directly without modifying the component's style.<br>
     * </p>
     *
     * @param pText          The text to be appended.
     * @param pResult        The component to append the unstyled text to.
     * @param pOriginalStyle The original style for the text.
     * @author MeAlam
     * @see #processComponentText
     * @since 1.6.0
     */
    protected void appendUnstyledText(String pText, MutableComponent pResult, Style pOriginalStyle) {
        pResult.append(Component.literal(pText).setStyle(pOriginalStyle));
    }

    /**
     * Checks if the feature is enabled.
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
    protected abstract boolean isFeatureEnabled();

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
    protected abstract String getFeatureName();
}
