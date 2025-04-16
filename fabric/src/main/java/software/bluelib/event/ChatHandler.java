// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import java.util.Objects;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.markdown.MarkdownParser;

public class ChatHandler {

    public static boolean onAllowChat(PlayerChatMessage pPlayerChatMessage, ServerPlayer pServerPlayer, ChatType.Bound pBound) {
        Component originalMessage = pPlayerChatMessage.decoratedContent();
        Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage);
        if (!formattedMessage.equals(originalMessage)) {
            PlayerChatMessage newPlayerChatMessage = new PlayerChatMessage(
                    pPlayerChatMessage.link(),
                    pPlayerChatMessage.signature(),
                    pPlayerChatMessage.signedBody(),
                    formattedMessage,
                    pPlayerChatMessage.filterMask());

            pServerPlayer.sendChatMessage(OutgoingChatMessage.create(newPlayerChatMessage), false,
                    pBound.withTargetName(Objects.requireNonNull(pServerPlayer.getDisplayName())));
        }
        return formattedMessage.equals(originalMessage);
    }
}
