// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.test.utils;

import java.util.List;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.markdown.MarkdownParser;

/**
 * A {@code public class} that provides utility methods for tests related to messages and chat.
 * Key Methods:
 * <ul>
 * <li>{@link #sendMessageToPlayers(GameTestHelper, String)} - Sends a formatted message to all players on the server.</li>
 * <li>{@link #getRandomHex()} - Generates a random six-digit hexadecimal value.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @since 1.6.0
 */
public class MessageUtils {

    /**
     * A {@code public static} method that sends a formatted message to all players on the server.
     * <p>
     * This method retrieves the list of players from the {@link GameTestHelper} and applies Markdown
     * formatting to the message using the {@link MarkdownParser#parseMarkdown(Component)} method. If
     * no players are found or the message formatting fails, the test will fail with an appropriate
     * error message.
     * </p>
     *
     * @param pHelper  {@link GameTestHelper} - The game test helper providing context and utilities.
     * @param pMessage {@link String} - The message to be sent to players.
     * @author MeAlam
     * @since 1.6.0
     */
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

    /**
     * A {@code public static} method that generates a random six-digit hexadecimal value.
     * <p>
     * This method creates a random integer between 100,000 and 999,999, simulating the range of a
     * six-digit hexadecimal number.
     * </p>
     *
     * @return {@link Integer} - A randomly generated six-digit hexadecimal value.
     * @author MeAlam
     * @since 1.6.0
     */
    public static int getRandomHex() {
        return 100000 + new java.util.Random().nextInt(900000);
    }
}
