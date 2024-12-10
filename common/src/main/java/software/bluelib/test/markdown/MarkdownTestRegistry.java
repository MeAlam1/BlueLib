package software.bluelib.test.markdown;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class MarkdownTestRegistry {

    @GameTest
    public static void boldTest(GameTestHelper pHelper) {
        pHelper.succeed();
    }
}
