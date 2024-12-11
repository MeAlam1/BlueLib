package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class Italic {

    public static void italic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic test: §r *italic*");
    }

    public static void italicBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a italic/bold test: §r *italic* **bold**");
    }

    public static void italicUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/underline test: §r *italic* __Underline__");
    }

    public static void italicStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/strikethrough test: §r *italic* ~~Strikethrough~~");
    }

    public static void italicHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/hyperlink test: §r *italic* [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void italicColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/color test: §r *italic* -#" + MessageUtils.getRandomHex() + " -(Color)");
    }
}
