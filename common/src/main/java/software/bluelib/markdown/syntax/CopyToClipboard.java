// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.markdown.MarkdownFeature;

/**
 * Handles the application of the copy-to-clipboard feature to Markdown text.
 * <p>
 * Purpose: This class is responsible for adding copy-to-clipboard functionality to text within markdown syntax.<br>
 * When: The copy-to-clipboard action is applied when the text is processed and the feature is enabled.<br>
 * Where: Executed within the {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)} method when the feature is enabled.<br>
 * Additional Info: The feature is controlled by the {@link CopyToClipboard#isCopyToClipboardEnabled} flag.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #apply(MutableComponent, String)} - Applies the copy-to-clipboard functionality to the provided text.</li>
 * <li>{@link #isCopyToClipboardEnabled()} - Checks if the copy-to-clipboard feature is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see software.bluelib.markdown.MarkdownParser
 * @see MarkdownFeature
 * @since 1.5.0
 */
public class CopyToClipboard {

    /**
     * Flag that determines whether the copy-to-clipboard feature is enabled.
     * <p>
     * Purpose: This variable holds the state of the copy-to-clipboard feature (enabled or disabled).<br>
     * When: It is checked whenever the markdown text is processed to determine whether the copy-to-clipboard action should be applied.<br>
     * Where: It is used in the {@link CopyToClipboard#isCopyToClipboardEnabled()} method.<br>
     * Additional Info: This feature can be enabled or disabled globally through this flag.<br>
     * </p>
     *
     * @see CopyToClipboard#isCopyToClipboardEnabled()
     * @since 1.5.0
     */
    public static Boolean isCopyToClipboardEnabled = true;

    /**
     * Applies the copy-to-clipboard functionality to the provided text.
     * <p>
     * Purpose: This method takes the provided text and applies the copy-to-clipboard action, then appends it to the result.<br>
     * When: Called when processing a markdown string with copy-to-clipboard syntax.<br>
     * Where: Executed in {@link software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)}.<br>
     * Additional Info: The text is wrapped with a click event that enables copy-to-clipboard.<br>
     * </p>
     *
     * @param pMessage    The message component to be formatted with copy-to-clipboard.
     * @param pTextToCopy The text to be copied to clipboard.
     * @return The mutable component with the copy-to-clipboard functionality applied.
     * @author MeAlam
     * @see software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)
     * @since 1.5.0
     */
    public MutableComponent apply(MutableComponent pMessage, String pTextToCopy) {
        if (!isCopyToClipboardEnabled) {
            return pMessage;
        }
        MutableComponent result = Component.empty();

        for (Component sibling : pMessage.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                if (mutableSibling.getStyle().getClickEvent() == null) {
                    mutableSibling.setStyle(mutableSibling.getStyle()
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pTextToCopy)));
                }
                result.append(mutableSibling);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    /**
     * Checks if the copy-to-clipboard feature is enabled.
     * <p>
     * Purpose: Determines whether the copy-to-clipboard feature is active based on the {@link #isCopyToClipboardEnabled} flag.<br>
     * When: Called before processing the text to check if the feature should be applied.<br>
     * Where: Executed in {@link #apply(MutableComponent, String)}.<br>
     * Additional Info: This flag can be changed through {@link CopyToClipboard#isCopyToClipboardEnabled}.<br>
     * </p>
     *
     * @return {@code true} if copy-to-clipboard is enabled, {@code false} otherwise.
     * @author MeAlam
     * @see software.bluelib.markdown.MarkdownParser#parseMarkdown(Component)
     * @since 1.5.0
     */
    public static Boolean isCopyToClipboardEnabled() {
        return isCopyToClipboardEnabled;
    }
}
