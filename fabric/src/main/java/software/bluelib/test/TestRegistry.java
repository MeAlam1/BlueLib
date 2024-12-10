package software.bluelib.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTestRegistry;
import software.bluelib.test.markdown.MarkdownTestRegistry;

public class TestRegistry implements FabricGameTest {

    public static void registerTests() {
        GameTestRegistry.register(ExampleTest.class);
        GameTestRegistry.register(MarkdownTestRegistry.class);
    }
}
