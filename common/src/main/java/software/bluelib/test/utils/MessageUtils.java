// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.test.utils;

import java.util.List;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import software.bluelib.markdown.MarkdownParser;

/**
 * Utility class for sending messages to players and generating random hex values.
 * <p>
 * Purpose: This class provides utility methods for sending formatted messages to players and generating random hex values.<br>
 * When: Used during game tests to communicate with players and generate random values.<br>
 * Where: Invoked in game test scenarios and other utility contexts.<br>
 * Additional Info: The messages are formatted using the {@link MarkdownParser} class.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #sendMessageToPlayers(GameTestHelper, String)} - Sends a formatted message to all players.</li>
 * <li>{@link #getRandomHex()} - Generates a random hex value.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see MarkdownParser
 * @see GameTestHelper
 * @see ServerPlayer
 * @see Component
 * @since 1.6.0
 */
public class MessageUtils {

    /**
     * Sends a formatted message to all players in the game test.
     * <p>
     * Purpose: This method sends a message to all players after formatting it using Markdown.<br>
     * When: Called during game tests to communicate with players.<br>
     * Where: Used in game test scenarios where player communication is required.<br>
     * Additional Info: If no players are found or the message formatting fails, the test will fail.
     * </p>
     *
     * @param pHelper  The game test helper instance.
     * @param pMessage The message to be sent to players.
     * @author MeAlam
     * @see MarkdownParser
     * @see GameTestHelper
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
     * Generates a random hex value.
     * <p>
     * Purpose: This method generates a random hex value within a specified range.<br>
     * When: Called when a random hex value is needed.<br>
     * Where: Used in various utility contexts where random values are required.<br>
     * Additional Info: The generated hex value is between 100000 and 999999.
     * </p>
     *
     * @return A random hex value.
     * @author MeAlam
     * @see java.util.Random
     * @since 1.6.0
     */
    public static int getRandomHex() {
        return 100000 + new java.util.Random().nextInt(900000);
    }
}
