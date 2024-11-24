// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import software.bluelib.utils.markdown.MarkdownParser;

/**
 * A {@code public class} responsible for handling server chat events and formatting chat messages using Markdown.
 * <p>
 * This class listens for chat messages on the server and applies Markdown formatting to the message content
 * using the {@link MarkdownParser}. The formatted message is then set as the new message to be broadcasted.
 * </p>
 *
 * @author MeAlam
 * @version 1.1.0
 * @since 1.1.0
 */
@EventBusSubscriber
public class ChatHandler {

    /**
     * A {@code public static} method that handles server chat events and formats the message using Markdown.
     * <p>
     * This method listens to the {@link ServerChatEvent} and applies Markdown formatting to the message using
     * the {@link MarkdownParser}. The formatted message is then set as the new message.
     * </p>
     *
     * @param pEvent {@link ServerChatEvent} - The event containing the original chat message to format.
     * @author MeAlam
     * @since 1.1.0
     */
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent pEvent) {
        Component originalMessage = pEvent.getMessage();
        Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage, pEvent.getPlayer());
        pEvent.setMessage(formattedMessage);
    }
}
