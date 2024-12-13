// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.markdown;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import software.bluelib.markdown.syntax.*;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A class responsible for parsing and formatting Markdown into Minecraft's {@link Component}.
 * <p>
 * This class processes text components and applies Markdown-style formatting ({@link Bold}, {@link Italic}, {@link Strikethrough}, {@link Underline}, {@link Spoiler}, {@link Hyperlink}, {@link Color}, {@link CopyToClipboard})
 * to the text. The formatting is controlled globally or individually through the {@link EnableMarkdownFor} and
 * {@link DisableMarkdownFor} inner classes.
 * </p>
 * <p>
 * Purpose: This class provides a utility for parsing and applying Markdown formatting in Minecraft chat messages.<br>
 * When: This class is used when a message needs to be formatted with Markdown syntax.<br>
 * Where: The class is invoked in various components where text formatting is required, typically in chat rendering or message construction.<br>
 * Additional Info: The formatting can be enabled or disabled globally or selectively for specific features like bold or italic. The settings are managed via the {@link EnableMarkdownFor} and {@link DisableMarkdownFor} classes.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #parseMarkdown(Component)} - Parses and applies Markdown formatting to a given message component.</li>
 * <li>{@link #enableMarkdown()} - Enables global Markdown formatting.</li>
 * <li>{@link #disableMarkdown()} - Disables global Markdown formatting.</li>
 * <li>{@link #enableMarkdownFor()} - Returns an instance of {@link EnableMarkdownFor} to enable specific Markdown features.</li>
 * <li>{@link #disableMarkdownFor()} - Returns an instance of {@link DisableMarkdownFor} to disable specific Markdown features.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see EnableMarkdownFor
 * @see DisableMarkdownFor
 * @see Bold
 * @see Italic
 * @see Strikethrough
 * @see Underline
 * @see Spoiler
 * @see Hyperlink
 * @see Color
 * @see CopyToClipboard
 * @since 1.1.0
 */
public class MarkdownParser {

    /**
     * A flag indicating whether global markdown processing is enabled.
     * <p>
     * Purpose: To control whether Markdown formatting is applied globally to components.<br>
     * When: This flag is checked every time a component is processed for Markdown formatting.<br>
     * Where: It is used in the {@link #parseMarkdown(Component)} method.<br>
     * Additional Info: If global Markdown is disabled, no formatting will be applied, regardless of other settings.
     * </p>
     *
     * @see #parseMarkdown(Component)
     * @since 1.1.0
     */
    private static boolean globalMarkdownEnabled = true;

    /**
     * Parses and applies Markdown formatting to a given {@link Component} message.
     * <p>
     * Purpose: To parse and apply Markdown formatting to a given message component.<br>
     * When: Called when a component is to be formatted with Markdown styles.<br>
     * Where: This method is invoked in various places where a text message is being processed for display.<br>
     * Additional Info: The method checks if global Markdown formatting is enabled and applies the relevant formats like bold, italic, etc.
     * </p>
     *
     * @param pMessage {@link Component} - The message component containing the text to format.
     * @return A {@link MutableComponent} with the Markdown formatting applied.
     * @author MeAlam
     * @see Bold
     * @see Italic
     * @see Strikethrough
     * @see Underline
     * @see Spoiler
     * @see Hyperlink
     * @see Color
     * @see CopyToClipboard
     * @since 1.1.0
     */
    public static MutableComponent parseMarkdown(Component pMessage) {
        if (!globalMarkdownEnabled) {
            BaseLogger.log(BaseLogLevel.INFO, "Global markdown is disabled, returning original message", true);
            return pMessage.copy();
        }

        String text = pMessage.getString();
        MutableComponent formattedMessage = Component.literal(text);

        formattedMessage = new Bold().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Bold: " + formattedMessage, true);
        formattedMessage = new Italic().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Italic: " + formattedMessage, true);
        formattedMessage = new Underline().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Underline: " + formattedMessage, true);
        formattedMessage = new Strikethrough().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Strikethrough: " + formattedMessage, true);
        formattedMessage = new Spoiler().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Spoiler: " + formattedMessage, true);
        formattedMessage = new Hyperlink().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Hyperlink: " + formattedMessage, true);
        formattedMessage = new Color().apply(formattedMessage);
        //BaseLogger.log(BaseLogLevel.INFO, "After Color: " + formattedMessage, true);
        formattedMessage = new CopyToClipboard().apply(formattedMessage, text);

        BaseLogger.log(BaseLogLevel.INFO, "Completed Message: " + formattedMessage, true);
        return formattedMessage;
    }

    /**
     * Enables global Markdown formatting.
     * <p>
     * Purpose: To enable global Markdown formatting for all messages.<br>
     * When: Called when global Markdown formatting needs to be enabled.<br>
     * Where: This method is typically called during initialization or configuration of the Markdown settings.<br>
     * Additional Info: Enabling global formatting will allow the system to apply all Markdown features like bold, italic, etc.
     * </p>
     *
     * @author MeAlam
     * @see #disableMarkdown()
     * @see EnableMarkdownFor
     * @see DisableMarkdownFor
     * @since 1.1.0
     */
    public static void enableMarkdown() {
        globalMarkdownEnabled = true;
        BaseLogger.log(BaseLogLevel.INFO, "Global markdown enabled", true);
    }

    /**
     * Disables global Markdown formatting.
     * <p>
     * Purpose: To disable global Markdown formatting for all messages.<br>
     * When: Called when global Markdown formatting should be disabled.<br>
     * Where: This method is typically invoked when the user wants to prevent any Markdown formatting from being applied.<br>
     * Additional Info: Disabling global formatting ensures no Markdown styles (e.g., bold, italic) are applied to any messages.
     * </p>
     *
     * @author MeAlam
     * @see #enableMarkdown()
     * @see EnableMarkdownFor
     * @see DisableMarkdownFor
     * @since 1.1.0
     */
    public static void disableMarkdown() {
        globalMarkdownEnabled = false;
        BaseLogger.log(BaseLogLevel.INFO, "Global markdown disabled", true);
    }

    /**
     * Returns a new instance of {@link EnableMarkdownFor} to enable Markdown for specific formats.
     * <p>
     * Purpose: To allow selective enabling of specific Markdown features.<br>
     * When: Called when specific Markdown features (e.g., bold, italic) need to be enabled.<br>
     * Where: Typically invoked in settings or configuration code to modify Markdown options.<br>
     * Additional Info: This method returns an instance of {@link EnableMarkdownFor}, which can be used for method chaining to enable various Markdown features.
     * </p>
     *
     * @return A new {@link EnableMarkdownFor} instance.
     * @author MeAlam
     * @see EnableMarkdownFor
     * @see DisableMarkdownFor
     * @since 1.1.0
     */
    public static EnableMarkdownFor enableMarkdownFor() {
        BaseLogger.log(BaseLogLevel.INFO, "Returning EnableMarkdownFor instance", true);
        return new EnableMarkdownFor();
    }

    /**
     * Returns a new instance of {@link DisableMarkdownFor} to disable Markdown for specific formats.
     * <p>
     * Purpose: To allow selective disabling of specific Markdown features.<br>
     * When: Called when specific Markdown features (e.g., bold, italic) need to be disabled.<br>
     * Where: Typically invoked in settings or configuration code to modify Markdown options.<br>
     * Additional Info: This method returns an instance of {@link DisableMarkdownFor}, which can be used for method chaining to disable various Markdown features.
     * </p>
     *
     * @return A new {@link DisableMarkdownFor} instance.
     * @author MeAlam
     * @see EnableMarkdownFor
     * @see DisableMarkdownFor
     * @since 1.1.0
     */
    public static DisableMarkdownFor disableMarkdownFor() {
        BaseLogger.log(BaseLogLevel.INFO, "Returning DisableMarkdownFor instance", true);
        return new DisableMarkdownFor();
    }

    /**
     * An inner class used to enable Markdown formatting for specific features.
     * <p>
     * Purpose: To enable specific Markdown formatting features like bold, italic, etc.<br>
     * When: This class is used when individual features of Markdown need to be enabled.<br>
     * Where: It is used in the {@link MarkdownParser#enableMarkdownFor()} method to selectively enable features.<br>
     * Additional Info: Enables features like bold, italic, etc., by chaining method calls.
     * </p>
     * <p>
     * Key Methods:
     * <ul>
     * <li>{@link #bold()} - Enables bold Markdown formatting.</li>
     * <li>{@link #italic()} - Enables italic Markdown formatting.</li>
     * <li>{@link #strikethrough()} - Enables strikethrough Markdown formatting.</li>
     * <li>{@link #underline()} - Enables underline Markdown formatting.</li>
     * <li>{@link #hyperlink()} - Enables hyperlink Markdown formatting.</li>
     * <li>{@link #spoiler()} - Enables spoiler Markdown formatting.</li>
     * <li>{@link #copyToClipboard()} - Enables copy to clipboard Markdown formatting.</li>
     * <li>{@link #color()} - Enables color Markdown formatting.</li>
     * </ul>
     *
     * @author MeAlam
     * @version 1.6.0
     * @see DisableMarkdownFor
     * @see Bold
     * @see Italic
     * @see Strikethrough
     * @see Underline
     * @see Spoiler
     * @see Hyperlink
     * @see Color
     * @see CopyToClipboard
     * @since 1.1.0
     */
    public static class EnableMarkdownFor {

        /**
         * Enables bold Markdown formatting.
         * <p>
         * Purpose: To enable bold formatting for text.<br>
         * When: Called when the bold feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable bold formatting.<br>
         * Additional Info: This affects the application of the bold style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Bold
         * @see DisableMarkdownFor
         * @since 1.1.0
         */
        public EnableMarkdownFor bold() {
            Bold.isBoldEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled bold markdown", true);
            return this;
        }

        /**
         * Enables italic Markdown formatting.
         * <p>
         * Purpose: To enable italic formatting for text.<br>
         * When: Called when the italic feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable italic formatting.<br>
         * Additional Info: This affects the application of the italic style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Italic
         * @see DisableMarkdownFor
         * @since 1.1.0
         */
        public EnableMarkdownFor italic() {
            Italic.isItalicEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled italic markdown", true);
            return this;
        }

        /**
         * Enables strikethrough Markdown formatting.
         * <p>
         * Purpose: To enable strikethrough formatting for text.<br>
         * When: Called when the strikethrough feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable strikethrough formatting.<br>
         * Additional Info: This affects the application of the strikethrough style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Strikethrough
         * @since 1.1.0
         */
        public EnableMarkdownFor strikethrough() {
            Strikethrough.isStrikethroughEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled strikethrough markdown", true);
            return this;
        }

        /**
         * Enables underline Markdown formatting.
         * <p>
         * Purpose: To enable underline formatting for text.<br>
         * When: Called when the underline feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable underline formatting.<br>
         * Additional Info: This affects the application of the underline style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Underline
         * @see DisableMarkdownFor
         * @since 1.1.0
         */
        public EnableMarkdownFor underline() {
            Underline.isUnderlineEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled underline markdown", true);
            return this;
        }

        /**
         * Enables hyperlink Markdown formatting.
         * <p>
         * Purpose: To enable hyperlink formatting for text.<br>
         * When: Called when the hyperlink feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable hyperlink formatting.<br>
         * Additional Info: This affects the application of the hyperlink style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Hyperlink
         * @see DisableMarkdownFor
         * @since 1.4.0
         */
        public EnableMarkdownFor hyperlink() {
            Hyperlink.isHyperlinkEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled hyperlink markdown", true);
            return this;
        }

        /**
         * Enables spoiler Markdown formatting.
         * <p>
         * Purpose: To enable spoiler formatting for text.<br>
         * When: Called when the spoiler feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable spoiler formatting.<br>
         * Additional Info: This affects the application of the spoiler style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Spoiler
         * @see DisableMarkdownFor
         * @since 1.5.0
         */
        public EnableMarkdownFor spoiler() {
            Spoiler.isSpoilerEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled spoiler markdown", true);
            return this;
        }

        /**
         * Enables CopyToClipboard Markdown formatting.
         * <p>
         * Purpose: To enable copy-to-clipboard formatting for text.<br>
         * When: Called when the copy-to-clipboard feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable copy-to-clipboard formatting.<br>
         * Additional Info: This affects the application of the copy-to-clipboard functionality in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see CopyToClipboard
         * @see DisableMarkdownFor
         * @since 1.6.0
         */
        public EnableMarkdownFor copyToClipboard() {
            CopyToClipboard.isCopyToClipboardEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled copy-to-clipboard markdown", true);
            return this;
        }

        /**
         * Enables color Markdown formatting.
         * <p>
         * Purpose: To enable color formatting for text.<br>
         * When: Called when the color feature should be enabled for Markdown.<br>
         * Where: Invoked in the {@link EnableMarkdownFor} instance when the user opts to enable color formatting.<br>
         * Additional Info: This affects the application of the color style in parsed messages.
         * </p>
         *
         * @return The {@link EnableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Color
         * @see DisableMarkdownFor
         * @since 1.6.0
         */
        public EnableMarkdownFor color() {
            Color.isColorEnabled = true;
            BaseLogger.log(BaseLogLevel.INFO, "Enabled color markdown", true);
            return this;
        }
    }

    /**
     * An inner class used to disable Markdown formatting for specific features.
     * <p>
     * Purpose: To disable specific Markdown formatting features like bold, italic, etc.<br>
     * When: This class is used when individual features of Markdown need to be disabled.<br>
     * Where: It is used in the {@link MarkdownParser#disableMarkdownFor()} method to selectively disable features.<br>
     * Additional Info: Disables features like bold, italic, etc., by chaining method calls.
     * </p>
     * <p>
     * Key Methods:
     * <ul>
     * <li>{@link #bold()} - Disables bold Markdown formatting.</li>
     * <li>{@link #italic()} - Disables italic Markdown formatting.</li>
     * <li>{@link #strikethrough()} - Disables strikethrough Markdown formatting.</li>
     * <li>{@link #underline()} - Disables underline Markdown formatting.</li>
     * <li>{@link #hyperlink()} - Disables hyperlink Markdown formatting.</li>
     * <li>{@link #spoiler()} - Disables spoiler Markdown formatting.</li>
     * <li>{@link #copyToClipboard()} - Disables copy to clipboard Markdown formatting.</li>
     * <li>{@link #color()} - Disables color Markdown formatting.</li>
     * </ul>
     *
     * @author MeAlam
     * @version 1.6.0
     * @see EnableMarkdownFor
     * @see Bold
     * @see Italic
     * @see Strikethrough
     * @see Underline
     * @see Spoiler
     * @see Hyperlink
     * @see Color
     * @see CopyToClipboard
     * @since 1.1.0
     */
    public static class DisableMarkdownFor {

        /**
         * Disables bold Markdown formatting.
         * <p>
         * Purpose: To disable bold formatting for text.<br>
         * When: Called when the bold feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable bold formatting.<br>
         * Additional Info: This affects the application of the bold style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Bold
         * @see EnableMarkdownFor
         * @since 1.1.0
         */
        public DisableMarkdownFor bold() {
            Bold.isBoldEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled bold markdown", true);
            return this;
        }

        /**
         * Disables italic Markdown formatting.
         * <p>
         * Purpose: To disable italic formatting for text.<br>
         * When: Called when the italic feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable italic formatting.<br>
         * Additional Info: This affects the application of the italic style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Italic
         * @see EnableMarkdownFor
         * @since 1.1.0
         */
        public DisableMarkdownFor italic() {
            Italic.isItalicEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled italic markdown", true);
            return this;
        }

        /**
         * Disables strikethrough Markdown formatting.
         * <p>
         * Purpose: To disable strikethrough formatting for text.<br>
         * When: Called when the strikethrough feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable strikethrough formatting.<br>
         * Additional Info: This affects the application of the strikethrough style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Strikethrough
         * @see EnableMarkdownFor
         * @since 1.1.0
         */
        public DisableMarkdownFor strikethrough() {
            Strikethrough.isStrikethroughEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled strikethrough markdown", true);
            return this;
        }

        /**
         * Disables underline Markdown formatting.
         * <p>
         * Purpose: To disable underline formatting for text.<br>
         * When: Called when the underline feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable underline formatting.<br>
         * Additional Info: This affects the application of the underline style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Underline
         * @see EnableMarkdownFor
         * @since 1.1.0
         */
        public DisableMarkdownFor underline() {
            Underline.isUnderlineEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled underline markdown", true);
            return this;
        }

        /**
         * Disables hyperlink Markdown formatting.
         * <p>
         * Purpose: To disable hyperlink formatting for text.<br>
         * When: Called when the hyperlink feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable hyperlink formatting.<br>
         * Additional Info: This affects the application of the hyperlink style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Hyperlink
         * @see EnableMarkdownFor
         * @since 1.4.0
         */
        public DisableMarkdownFor hyperlink() {
            Hyperlink.isHyperlinkEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled hyperlink markdown", true);
            return this;
        }

        /**
         * Disables spoiler Markdown formatting.
         * <p>
         * Purpose: To disable spoiler formatting for text.<br>
         * When: Called when the spoiler feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable spoiler formatting.<br>
         * Additional Info: This affects the application of the spoiler style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Spoiler
         * @see EnableMarkdownFor
         * @since 1.5.0
         */
        public DisableMarkdownFor spoiler() {
            Spoiler.isSpoilerEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled spoiler markdown", true);
            return this;
        }

        /**
         * Disables CopyToClipboard Markdown formatting.
         * <p>
         * Purpose: To disable copy-to-clipboard formatting for text.<br>
         * When: Called when the copy-to-clipboard feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable copy-to-clipboard formatting.<br>
         * Additional Info: This affects the application of the copy-to-clipboard functionality in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see CopyToClipboard
         * @see EnableMarkdownFor
         * @since 1.6.0
         */
        public DisableMarkdownFor copyToClipboard() {
            CopyToClipboard.isCopyToClipboardEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled copy-to-clipboard markdown", true);
            return this;
        }

        /**
         * Disables color Markdown formatting.
         * <p>
         * Purpose: To disable color formatting for text.<br>
         * When: Called when the color feature should be disabled for Markdown.<br>
         * Where: Invoked in the {@link DisableMarkdownFor} instance when the user opts to disable color formatting.<br>
         * Additional Info: This affects the application of the color style in parsed messages.
         * </p>
         *
         * @return The {@link DisableMarkdownFor} instance to allow method chaining.
         * @author MeAlam
         * @see Color
         * @see EnableMarkdownFor
         * @since 1.6.0
         */
        public DisableMarkdownFor color() {
            Color.isColorEnabled = false;
            BaseLogger.log(BaseLogLevel.INFO, "Disabled color markdown", true);
            return this;
        }
    }
}
