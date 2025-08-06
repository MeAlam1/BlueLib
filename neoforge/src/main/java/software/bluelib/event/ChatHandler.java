/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.event;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.markdown.MarkdownParser;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class ChatHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onServerChat(@NotNull ServerChatEvent pEvent) {
		Component originalMessage = pEvent.getMessage();
		Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage);
		pEvent.setMessage(formattedMessage);
	}
}
