package software.bluelib.test.markdown;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class MarkdownStyleTest {

    private static final List<String> ALL_STYLES = List.of(
            "**bold**", // Bold
            "\\**bold**", // Bold Canceled
            "*italic*", // Italic
            "\\*italic*", // Italic Canceled
            "__Underline__", // Underline
            "\\__Underline__", // Underline Canceled
            "~~Strikethrough~~", // Strikethrough
            "\\~~Strikethrough~~", // Strikethrough Canceled
            "[Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)", // Hyperlink
            "\\[Hyperlink](https://modrinth.com/mod/bluelib)", // Hyperlink Canceled
            "-#" + MessageUtils.getRandomHex() + "-(Color)", // Color
            "\\-#" + MessageUtils.getRandomHex() + "-(Color)", // Color Canceled
            "||Spoiler||", // Spoiler
            "\\||Spoiler||" // Spoiler Canceled
    );

    private static final List<String> CANCEL_ONLY_STYLES = List.of(
            "\\**bold**", // Bold Canceled
            "\\*italic*", // Italic Canceled
            "\\__Underline__", // Underline Canceled
            "\\~~Strikethrough~~", // Strikethrough Canceled
            "\\[Hyperlink](https://modrinth.com/mod/bluelib)", // Hyperlink Canceled
            "\\-#" + MessageUtils.getRandomHex() + "-(Color)", // Color Canceled
            "\\||Spoiler||" // Spoiler Canceled
    );

    private static final List<String> ALL_TEST_STYLES = List.of(
            "**bold**", // Bold
            "*italic*", // Italic
            "__Underline__", // Underline
            "~~Strikethrough~~", // Strikethrough
            "[Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)", // Hyperlink
            "-#" + MessageUtils.getRandomHex() + "-(Color)", // Color
            "||Spoiler||" // Spoiler
    );

    private static final List<String> ALL_TWICE_STYLES = List.of(
            "**bold**", // Bold
            "**bold**", // Bold
            "*italic*", // Italic
            "*italic*", // Italic
            "__Underline__", // Underline
            "__Underline__", // Underline
            "~~Strikethrough~~", // Strikethrough
            "~~Strikethrough~~", // Strikethrough
            "[Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)", // Hyperlink
            "[Hyperlink](https://modrinth.com/mod/bluelib)", // Hyperlink
            "-#" + MessageUtils.getRandomHex() + "-(Color)", // Color
            "-#" + MessageUtils.getRandomHex() + "-(Color)", // Color
            "||Spoiler||", // Spoiler
            "||Spoiler||" // Spoiler
    );

    public static void testAllStyles(GameTestHelper pHelper) {
        testCombinations(pHelper, ALL_STYLES, "§6 This is an All/Cancel test: §r ");
    }

    public static void testCancelOnlyStyles(GameTestHelper pHelper) {
        testCombinations(pHelper, CANCEL_ONLY_STYLES, "§6 This is a Cancel test: §r ");
    }

    public static void testAllCombinations(GameTestHelper pHelper) {
        testCombinations(pHelper, ALL_TEST_STYLES, "§6 This is an All test: §r ");
    }

    public static void testAllTwiceCombinations(GameTestHelper pHelper) {
        testCombinations(pHelper, ALL_TWICE_STYLES, "§6 This is an All/All test: §r ");
    }

    private static void testCombinations(GameTestHelper pHelper, List<String> pStyles, String pPrefix) {
        generateCombinations(pStyles).forEach(combination -> {
            String message = buildMessage(combination, pPrefix);
            MessageUtils.sendMessageToPlayers(pHelper, message);
        });
    }

    private static List<List<String>> generateCombinations(List<String> pStyles) {
        List<List<String>> combinations = new ArrayList<>();
        int n = pStyles.size();
        int totalCombinations = 1 << n;

        for (int i = 0; i < totalCombinations; i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(pStyles.get(j));
                }
            }
            combinations.add(combination);
        }
        return combinations;
    }

    private static String buildMessage(List<String> pCombination, String pPrefix) {
        StringBuilder messageBuilder = new StringBuilder(pPrefix);
        pCombination.forEach(style -> messageBuilder.append(style).append(" "));
        return messageBuilder.toString().trim();
    }
}
