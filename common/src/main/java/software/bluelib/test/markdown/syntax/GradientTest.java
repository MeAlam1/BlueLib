package software.bluelib.test.markdown.syntax;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class GradientTest {

    public static void gradient(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient)");
    }

    public static void gradientBold(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/bold test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) **bold**");
    }

    public static void gradientItalic(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/italic test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) *italic*");
    }

    public static void gradientUnderline(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/underline test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) __Underline__");
    }

    public static void gradientStrikethrough(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/strikethrough test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) ~~Strikethrough~~");
    }

    public static void gradientHyperlink(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/hyperlink test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) [Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)");
    }

    public static void gradientColor(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/color test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) -#" + MessageUtils.getRandomHex() + "-(color)");
    }

    public static void gradientSpoiler(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/spoiler test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) ||spoiler||");
    }

    public static void gradientGradient(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a gradient/gradient test: §r -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient) -#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient)");
    }

    public static void gradientCancel(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is a canceled gradient test: §r \\-#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + ",#" + MessageUtils.getRandomHex() + "-(Gradient)");
    }

    public static void gradientInvalid(GameTestHelper pHelper) {
        MessageUtils.sendMessageToPlayers(pHelper, "§6 This is an invalid gradient test: §r -#invalidgradient-(gradient)");
    }
}
