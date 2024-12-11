package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class HyperlinkTest {

    public static void hyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink test: §r [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void hyperlinkBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink/bold test: §r [Hyperlink](https://modrinth.com/mod/bluelib) **bold**");
    }

    public static void hyperlinkItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an hyperlink/italic test: §r [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib-common) *italic*");
    }

    public static void hyperlinkUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an hyperlink/underline test: §r [Hyperlink](https://github.com/MeAlam1/BlueLib) __Underline__");
    }

    public static void hyperlinkStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink/strikethrough test: §r [Hyperlink](https://github.com/users/MeAlam1/projects/6) ~~Strikethrough~~");
    }

    public static void hyperlinkHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink/hyperlink test: §r [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib) [Hyperlink](https://modrinth.com/mod/bluelib)");
    }

    public static void hyperlinkColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink/color test: §r [Hyperlink](https://github.com/MeAlam1/BlueLib/issues) -#" + MessageUtils.getRandomHex() + "-(Color)");
    }

    public static void hyperlinkSpoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a hyperlink/spoiler test: §r [Hyperlink](https://github.com/MeAlam1/BlueLib/issues) ||spoiler||");
    }

    public static void hyperlinkCancel(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a canceled hyperlink test: §r \\[Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void hyperlinkInvalid(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an invalid hyperlink test: §r [Hyperlink](invalidLink)");
    }
}
