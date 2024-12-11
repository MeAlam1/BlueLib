package software.bluelib.test.markdown;

import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class AllCancel {

    private static final List<String> STYLES = List.of(
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
            "\\-#" + MessageUtils.getRandomHex() + "-(Color)" // Color Canceled
    );

    public static void testAllCombinations(GameTestHelper pHelper) {
        List<List<String>> combinations = generateCombinations();

        for (List<String> combination : combinations) {
            String styledMessage = buildMessage(combination);
            MessageUtils.sendMessageToPlayers(pHelper, styledMessage);
        }
    }

    private static List<List<String>> generateCombinations() {
        List<List<String>> combinations = new ArrayList<>();
        int n = AllCancel.STYLES.size();
        int totalCombinations = 1 << n;

        for (int i = 0; i < totalCombinations; i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(AllCancel.STYLES.get(j));
                }
            }
            combinations.add(combination);
        }
        return combinations;
    }

    private static String buildMessage(List<String> combination) {
        StringBuilder messageBuilder = new StringBuilder("§6 This is a test: §r ");
        for (String style : combination) {
            messageBuilder.append(style).append(" ");
        }
        return messageBuilder.toString().trim();
    }
}
