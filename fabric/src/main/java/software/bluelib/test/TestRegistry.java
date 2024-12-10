package software.bluelib.test;

import net.minecraft.gametest.framework.GameTestRegistry;

public class TestRegistry {

    public static void registerTests() {
        GameTestRegistry.register(ExampleTest.class);
    }
}
