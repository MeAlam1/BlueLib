// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import software.bluelib.markdown.MarkdownParser;

@EventBusSubscriber
public class ChatHandler {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent pEvent) {
        Component originalMessage = pEvent.getMessage();
        Component formattedMessage = MarkdownParser.parseMarkdown(originalMessage);
        pEvent.setMessage(formattedMessage);
    }
}
