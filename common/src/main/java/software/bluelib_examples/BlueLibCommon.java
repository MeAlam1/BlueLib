
package software.bluelib_examples;

import org.jetbrains.annotations.ApiStatus;
import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.registry.BlueNetworkRegistry;
import software.bluelib_examples.registry.EntityRegistry;

public class BlueLibCommon {

    @ApiStatus.Internal
    public static BlueNetworkRegistry getRegistry() {
        return new BlueNetworkRegistry();
    }

    @ApiStatus.Internal
    public static void doServerRegistration() {
        EntityRegistry.init();
        NetworkRegistry.registerC2SPacketProvider(getRegistry());
    }

    @ApiStatus.Internal
    public static void doClientRegistration() {
        NetworkRegistry.registerS2CPacketProvider(getRegistry());
    }
}
