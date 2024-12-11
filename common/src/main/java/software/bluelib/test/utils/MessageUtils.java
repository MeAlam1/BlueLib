package software.bluelib.test.utils;

import java.util.List;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.markdown.MarkdownParser;

public class MessageUtils {

    public static void sendMessageToPlayers(GameTestHelper pHelper, String pMessage) {
        List<ServerPlayer> players = pHelper.getLevel().getServer().getPlayerList().getPlayers();
        if (players.isEmpty()) {
            pHelper.fail("No players found");
            return;
        }
        Component result = MarkdownParser.parseMarkdown(Component.literal(pMessage));
        if (result == null) {
            pHelper.fail("Failed to format message");
            return;
        }

        players.forEach(player -> player.sendSystemMessage(result));
    }

    public static int getRandomHex() {
        return 100000 + new java.util.Random().nextInt(900000);
    }
}
