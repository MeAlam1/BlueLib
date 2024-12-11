package software.bluelib.test.markdown;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.gametest.framework.GameTestHelper;
import software.bluelib.test.utils.MessageUtils;

public class MarkdownAllTest {

    private static final List<String> STYLES = List.of(
            "**bold**", // Bold
            "*italic*", // Italic
            "__Underline__", // Underline
            "~~Strikethrough~~", // Strikethrough
            "[Hyperlink](https://www.curseforge.com/minecraft/mc-mods/bluelib)", // Hyperlink
            "-#" + MessageUtils.getRandomHex() + "-(Color)" // Color
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
        int n = MarkdownAllTest.STYLES.size();
        int totalCombinations = 1 << n;

        for (int i = 0; i < totalCombinations; i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(MarkdownAllTest.STYLES.get(j));
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
