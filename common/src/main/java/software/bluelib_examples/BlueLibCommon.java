
package software.bluelib_examples;

import software.bluelib.api.net.NetworkRegistry;
import software.bluelib_examples.registry.EntityRegistry;

public class BlueLibCommon {

    public static void doRegistrations() {
        EntityRegistry.init();
        var networkRegistry = new software.bluelib_examples.registry.NetworkRegistry();
        NetworkRegistry.registerC2SPacketProvider(networkRegistry);
        NetworkRegistry.registerS2CPacketProvider(networkRegistry);
    }
}
