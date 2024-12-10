package software.bluelib.test;

import net.minecraft.gametest.framework.GameTestRegistry;
import software.bluelib.test.markdown.MarkdownTestRegistry;

public class TestRegistry {

    @SuppressWarnings("deprecation")
    public static void registerTests() {
        GameTestRegistry.register(ExampleTest.class);
        GameTestRegistry.register(MarkdownTestRegistry.class);
    }
}
