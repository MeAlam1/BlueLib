package software.bluelib.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class ExampleTest {

    @GameTest
    public static void alwaysSuccess(GameTestHelper pHelper) {
        pHelper.succeed();
    }
}
