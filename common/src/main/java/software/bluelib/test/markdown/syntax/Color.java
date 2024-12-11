package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class Color {

    public static void color(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color test: §r -#" + MessageUtils.getRandomHex() + " -(Color)");
    }

    public static void colorBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color/bold test: §r -#" + MessageUtils.getRandomHex() + " -(Color) **bold**");
    }

    public static void colorItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color/italic test: §r -#" + MessageUtils.getRandomHex() + " -(Color) *italic*");
    }

    public static void colorUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color/underline test: §r -#" + MessageUtils.getRandomHex() + " -(Color) __Underline__");
    }

    public static void colorStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color/strikethrough test: §r -#" + MessageUtils.getRandomHex() + " -(Color) ~~Strikethrough~~");
    }

    public static void colorHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a color/hyperlink test: §r -#" + MessageUtils.getRandomHex() + " -(Color) [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }
}
