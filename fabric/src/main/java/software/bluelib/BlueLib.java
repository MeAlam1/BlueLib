package software.bluelib;

import net.fabricmc.api.ModInitializer;

public class BlueLib implements ModInitializer {
    
    @Override
    public void onInitialize() {
        BlueLibCommon.init();
    }
}
