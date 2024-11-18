package software.bluelib.example.event;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.utils.markdown.MarkdownParser;

import java.util.Objects;

/**
 * A {@code public class} responsible for handling server chat events and formatting chat messages using Markdown.
 * <p>
 * This class listens for chat messages on the server and applies Markdown formatting to the message content
 * using the {@link MarkdownParser}. The formatted message is then set as the new message to be broadcasted.
 * </p>
 */
public class ChatHandler {

    /**
     * A {@code public static} method that handles server chat events and formats the message using Markdown.
     * <p>
     * This method checks when a message gets sent and applies Markdown formatting to the message using
     * the {@link MarkdownParser}. The formatted message is then set as the new message.
     * </p>
     *
     * @param pPlayerChatMessage The original chat message.
     * @param pServerPlayer      The player who sent the message.
     * @param pBound             The chat type bound.
     */
    public static void onServerChat(PlayerChatMessage pPlayerChatMessage, ServerPlayer pServerPlayer, ChatType.Bound pBound) {
        Component originalMessage = pPlayerChatMessage.decoratedContent();
        Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage);
        PlayerChatMessage newPlayerChatMessage = new PlayerChatMessage(
                pPlayerChatMessage.link(),
                pPlayerChatMessage.signature(),
                pPlayerChatMessage.signedBody(),
                formattedMessage,
                pPlayerChatMessage.filterMask()
        );

        pServerPlayer.sendChatMessage(OutgoingChatMessage.create(newPlayerChatMessage), false, pBound.withTargetName(Objects.requireNonNull(pServerPlayer.getDisplayName())));
    }
}
