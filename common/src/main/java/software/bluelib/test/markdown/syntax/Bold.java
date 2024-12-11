package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class Bold {

    public static void bold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold test: §r **bold**");
    }

    public static void boldItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** *italic*");
    }

    public static void boldUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** __Underline__");
    }

    public static void boldStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** ~~Strikethrough~~");
    }

    public static void boldHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void boldColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a bold/italic test: §r **bold** -#" + MessageUtils.getRandomHex() + " -(Color)");
    }
}
