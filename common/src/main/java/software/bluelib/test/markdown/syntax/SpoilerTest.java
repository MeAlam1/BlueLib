package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class SpoilerTest {

    public static void spoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler test: §r ||spoiler||");
    }

    public static void spoilerBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/bold test: §r ||spoiler|| **bold**");
    }

    public static void spoilerItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/italic test: §r ||spoiler|| *italic*");
    }

    public static void spoilerUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/underline test: §r ||spoiler|| __Underline__");
    }

    public static void spoilerStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/strikethrough test: §r ||spoiler|| ~~Strikethrough~~");
    }

    public static void spoilerHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/hyperlink test: §r ||spoiler|| [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void spoilerColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/color test: §r ||spoiler|| -#" + MessageUtils.getRandomHex() + "-(Color)");
    }

    public static void spoilerSpoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a spoiler/spoiler test: §r ||spoiler|| ||spoiler||");
    }

    public static void spoilerCancel(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a canceled spoiler test: §r \\||spoiler||");
    }
}
