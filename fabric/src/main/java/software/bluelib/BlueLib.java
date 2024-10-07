package software.bluelib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BlueLib implements ModInitializer {

    private boolean hasInitialized = false;

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!hasInitialized) {
                hasInitialized = true;
                BlueLibCommon.init();
            }
        });
    }
}
