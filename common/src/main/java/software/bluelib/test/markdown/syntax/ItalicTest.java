package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class ItalicTest {

    public static void italic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic test: §r *italic*");
    }

    public static void italicBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/bold test: §r *italic* **bold**");
    }

    public static void italicItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/italic test: §r *italic* *italic*");
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
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an italic/color test: §r *italic* -#" + MessageUtils.getRandomHex() + "-(Color)");
    }

    public static void italicSpoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a italic/spoiler test: §r *italic* ||spoiler||");
    }

    public static void italicCancel(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an canceled italic test: §r \\*italic*");
    }
}
