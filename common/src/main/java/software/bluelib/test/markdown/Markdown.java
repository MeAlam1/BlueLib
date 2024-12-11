package software.bluelib.test.markdown;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.markdown.syntax.*;

public class Markdown {

    @GameTest
    public static void bold(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Bold.bold(pHelper);
            Bold.boldItalic(pHelper);
            Bold.boldUnderline(pHelper);
            Bold.boldStrikethrough(pHelper);
            Bold.boldHyperlink(pHelper);
            Bold.boldColor(pHelper);
        });
    }

    @GameTest
    public static void italic(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Italic.italic(pHelper);
            Italic.italicBold(pHelper);
            Italic.italicUnderline(pHelper);
            Italic.italicStrikethrough(pHelper);
            Italic.italicHyperlink(pHelper);
            Italic.italicColor(pHelper);
        });
    }

    @GameTest
    public static void underline(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Underline.underline(pHelper);
            Underline.underlineBold(pHelper);
            Underline.underlineItalic(pHelper);
            Underline.underlineStrikethrough(pHelper);
            Underline.underlineHyperlink(pHelper);
            Underline.underlineColor(pHelper);
        });
    }

    @GameTest
    public static void strikethrough(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Strikethrough.strikethrough(pHelper);
            Strikethrough.strikethroughBold(pHelper);
            Strikethrough.strikethroughItalic(pHelper);
            Strikethrough.strikethroughUnderline(pHelper);
            Strikethrough.strikethroughHyperlink(pHelper);
            Strikethrough.strikethroughColor(pHelper);
        });
    }

    @GameTest
    public static void hyperlink(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Hyperlink.hyperlink(pHelper);
            Hyperlink.hyperlinkBold(pHelper);
            Hyperlink.hyperlinkItalic(pHelper);
            Hyperlink.hyperlinkUnderline(pHelper);
            Hyperlink.hyperlinkStrikethrough(pHelper);
            Hyperlink.hyperlinkColor(pHelper);
        });
    }

    @GameTest
    public static void color(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            Color.color(pHelper);
            Color.colorBold(pHelper);
            Color.colorItalic(pHelper);
            Color.colorUnderline(pHelper);
            Color.colorStrikethrough(pHelper);
            Color.colorHyperlink(pHelper);
        });
    }

    @GameTest
    public static void all(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            All.testAllCombinations(pHelper);
        });
    }
}
