// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown.syntax;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.markdown.MarkdownFeature;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code public class} representing the CopyToClipboard Markdown formatting feature.
 * <p>
 * This class applies CopyToClipboard formatting to text surrounded by double asterisks (**). It extends the
 * {@link MarkdownFeature} class and overrides the {@link #applyFormat(String)} method to provide
 * the specific formatting logic for CopyToClipboard text.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #applyFormat(String)} - Applies CopyToClipboard formatting to the input content.</li>
 * <li>{@link #isCopyToClipboardEnabled()} - Retrieves whether CopyToClipboard formatting is enabled.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.5.0
 * @see MarkdownFeature
 * @since 1.5.0
 */
public class CopyToClipboard extends MarkdownFeature {

    /**
     * A {@code protected static} field that determines whether CopyToClipboard formatting is enabled.
     *
     * @since 1.5.0
     */
    public static Boolean isCopyToClipboardEnabled = true;

    /**
     * Overrides the {@link MarkdownFeature#applyFormat(String)} method to apply the formatting logic.
     * <p>
     * Currently, this method does not modify the provided content and simply returns it unchanged.
     * </p>
     *
     * @param pContent {@link String} - The content to format.
     * @return {@link String} - The content unchanged.
     * @author MeAlam
     * @since 1.5.0
     */
    @Override
    protected String applyFormat(String pContent) {
        return pContent;
    }

    /**
     * A {@link MutableComponent} that applies CopyToClipboard formatting to the input message.
     * <br>
     * This method iterates over the siblings of the input message and applies the CopyToClipboard formatting
     * to each sibling that does not already have a click event set. The text to copy is set to the provided
     * {@code textToCopy} parameter.
     *
     * @param pMessage   {@link MutableComponent} - The input message to be formatted.
     * @param textToCopy {@link String} - The text to copy to the clipboard.
     * @author MeAlam
     * @since 1.5.0
     */
    public MutableComponent applyCopyToClipboard(MutableComponent pMessage, String textToCopy) {
        MutableComponent result = Component.literal("");

        for (Component sibling : pMessage.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                if (mutableSibling.getStyle().getClickEvent() == null) {
                    mutableSibling.setStyle(mutableSibling.getStyle()
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, textToCopy)));
                }
                result.append(mutableSibling);
            } else {
                result.append(sibling);
            }
        }

        return result;
    }

    /**
     * A {@code public static} {@link Boolean} that retrieves whether CopyToClipboard formatting is enabled.
     *
     * @return {@code true} if CopyToClipboard formatting is enabled, {@code false} otherwise.
     * @author MeAlam
     * @since 1.5.0
     */
    public static Boolean isCopyToClipboardEnabled() {
        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved CopyToClipboard enabled status: " + isCopyToClipboardEnabled, true);
        return isCopyToClipboardEnabled;
    }
}
