package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class Underline {

    public static void underline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an underline test: §r __Underline__");
    }

    public static void underlineBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a underline/bold test: §r __Underline__ **bold**");
    }

    public static void underlineItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an underline/italic test: §r __Underline__ *italic*");
    }

    public static void underlineStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an underline/strikethrough test: §r __Underline__ ~~Strikethrough~~");
    }

    public static void underlineHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an underline/hyperlink test: §r __Underline__ [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void underlineColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an underline/color test: §r __Underline__ -#" + MessageUtils.getRandomHex() + " -(Color)");
    }
}
