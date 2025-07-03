/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import java.util.Objects;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.markdown.MarkdownParser;

public class ChatHandler {

	public static boolean onAllowChat(@NotNull PlayerChatMessage pPlayerChatMessage, @NotNull ServerPlayer pServerPlayer, @NotNull ChatType.Bound pBound) {
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
