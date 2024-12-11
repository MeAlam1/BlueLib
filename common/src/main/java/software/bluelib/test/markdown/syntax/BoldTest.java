package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class BoldTest {

    public static void bold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold test: §r **bold**");
    }

    public static void boldBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/bold test: §r **bold** **bold**");
    }

    public static void boldItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** *italic*");
    }

    public static void boldUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/underline test: §r **bold** __Underline__");
    }

    public static void boldStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/strikethrough test: §r **bold** ~~Strikethrough~~");
    }

    public static void boldHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/hyperlink test: §r **bold** [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void boldColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/color test: §r **bold** -#" + MessageUtils.getRandomHex() + "-(Color)");
    }

    public static void boldSpoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/spoiler test: §r **bold** ||spoiler||");
    }

    public static void boldCancel(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a canceled bold test: §r \\**bold**");
    }
}
