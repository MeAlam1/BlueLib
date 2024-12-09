package software.bluelib.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import software.bluelib.BlueLibConstants;

@GameTestHolder
public class ExampleTest {

    @GameTest(templateNamespace = BlueLibConstants.MOD_ID)
    public void alwaysSucceed(GameTestHelper pHelper) {
        pHelper.succeed();
    }
}
