package software.bluelib.test.markdown;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.markdown.syntax.*;

public class Markdown {

    @GameTest
    public static void bold(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            BoldTest.bold(pHelper);
            BoldTest.boldBold(pHelper);
            BoldTest.boldItalic(pHelper);
            BoldTest.boldUnderline(pHelper);
            BoldTest.boldStrikethrough(pHelper);
            BoldTest.boldHyperlink(pHelper);
            BoldTest.boldColor(pHelper);
            BoldTest.boldSpoiler(pHelper);
            BoldTest.boldCancel(pHelper);
        });
    }

    @GameTest
    public static void italic(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            ItalicTest.italic(pHelper);
            ItalicTest.italicBold(pHelper);
            ItalicTest.italicItalic(pHelper);
            ItalicTest.italicUnderline(pHelper);
            ItalicTest.italicStrikethrough(pHelper);
            ItalicTest.italicHyperlink(pHelper);
            ItalicTest.italicColor(pHelper);
            ItalicTest.italicSpoiler(pHelper);
            ItalicTest.italicCancel(pHelper);
        });
    }

    @GameTest
    public static void underline(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            UnderlineTest.underline(pHelper);
            UnderlineTest.underlineBold(pHelper);
            UnderlineTest.underlineItalic(pHelper);
            UnderlineTest.underlineUnderline(pHelper);
            UnderlineTest.underlineStrikethrough(pHelper);
            UnderlineTest.underlineHyperlink(pHelper);
            UnderlineTest.underlineColor(pHelper);
            UnderlineTest.underlineSpoiler(pHelper);
            UnderlineTest.underlineCancel(pHelper);
        });
    }

    @GameTest
    public static void strikethrough(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            StrikethroughTest.strikethrough(pHelper);
            StrikethroughTest.strikethroughBold(pHelper);
            StrikethroughTest.strikethroughItalic(pHelper);
            StrikethroughTest.strikethroughUnderline(pHelper);
            StrikethroughTest.strikethroughStrikethrough(pHelper);
            StrikethroughTest.strikethroughHyperlink(pHelper);
            StrikethroughTest.strikethroughColor(pHelper);
            StrikethroughTest.strikethroughSpoiler(pHelper);
            StrikethroughTest.strikethroughCancel(pHelper);
        });
    }

    @GameTest
    public static void hyperlink(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            HyperlinkTest.hyperlink(pHelper);
            HyperlinkTest.hyperlinkBold(pHelper);
            HyperlinkTest.hyperlinkItalic(pHelper);
            HyperlinkTest.hyperlinkUnderline(pHelper);
            HyperlinkTest.hyperlinkStrikethrough(pHelper);
            HyperlinkTest.hyperlinkHyperlink(pHelper);
            HyperlinkTest.hyperlinkColor(pHelper);
            HyperlinkTest.hyperlinkSpoiler(pHelper);
            HyperlinkTest.hyperlinkCancel(pHelper);
            HyperlinkTest.hyperlinkInvalid(pHelper);
        });
    }

    @GameTest
    public static void color(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            ColorTest.color(pHelper);
            ColorTest.colorBold(pHelper);
            ColorTest.colorItalic(pHelper);
            ColorTest.colorUnderline(pHelper);
            ColorTest.colorStrikethrough(pHelper);
            ColorTest.colorHyperlink(pHelper);
            ColorTest.colorColor(pHelper);
            ColorTest.colorSpoiler(pHelper);
            ColorTest.colorCancel(pHelper);
            ColorTest.colorInvalid(pHelper);
        });
    }

    @GameTest
    public static void spoiler(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            SpoilerTest.spoiler(pHelper);
            SpoilerTest.spoilerBold(pHelper);
            SpoilerTest.spoilerItalic(pHelper);
            SpoilerTest.spoilerUnderline(pHelper);
            SpoilerTest.spoilerStrikethrough(pHelper);
            SpoilerTest.spoilerHyperlink(pHelper);
            SpoilerTest.spoilerColor(pHelper);
            SpoilerTest.spoilerSpoiler(pHelper);
            SpoilerTest.spoilerCancel(pHelper);
        });
    }

    @GameTest
    public static void all(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            MarkdownStyleTest.testAllCombinations(pHelper);
        });
    }

    @GameTest
    public static void allTwice(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            MarkdownStyleTest.testAllTwiceCombinations(pHelper);
        });
    }

    @GameTest
    public static void allAndCancel(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            MarkdownStyleTest.testAllStyles(pHelper);
        });
    }

    @GameTest
    public static void allCancel(GameTestHelper pHelper) {
        pHelper.succeedIf(() -> {
            MarkdownStyleTest.testCancelOnlyStyles(pHelper);
        });
    }
}
