package software.bluelib.loader;

import net.fabricmc.api.ModInitializer;
import software.bluelib.loader.service.GeckoLibNetworking;


public final class GeckoLib implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLibConstants.init();
        GeckoLibNetworking.init();
    }
}
