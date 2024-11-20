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
     * A {@code public static} {@link Boolean} that handles the chat message event and formats the message using Markdown.
     * <p>
     * This method checks if the message is being formatted. If it is, the original message is not allowed.
     * </p>
     *
     * @param pPlayerChatMessage {@link PlayerChatMessage} - The chat message to be formatted.
     * @param pServerPlayer      {@link ServerPlayer} - The player sending the chat message.
     * @param pBound             {@link ChatType.Bound} - The chat type bound to the message.
     * @return {@code true} if the original message is allowed; {@code false} otherwise.
     * @author MeAlam
     * @since 1.1.0
     */
    public static boolean onAllowChat(PlayerChatMessage pPlayerChatMessage, ServerPlayer pServerPlayer, ChatType.Bound pBound) {
        Component originalMessage = pPlayerChatMessage.decoratedContent();
        Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage);
        if (!formattedMessage.equals(originalMessage)) {
            PlayerChatMessage newPlayerChatMessage = new PlayerChatMessage(
                    pPlayerChatMessage.link(),
                    pPlayerChatMessage.signature(),
                    pPlayerChatMessage.signedBody(),
                    formattedMessage,
                    pPlayerChatMessage.filterMask()
            );

            pServerPlayer.sendChatMessage(OutgoingChatMessage.create(newPlayerChatMessage), false,
                    pBound.withTargetName(Objects.requireNonNull(pServerPlayer.getDisplayName())));
        }
        return formattedMessage.equals(originalMessage);
    }
}
