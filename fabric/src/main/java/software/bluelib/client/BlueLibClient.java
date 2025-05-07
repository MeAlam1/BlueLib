// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bluelib.net.FabricNetworkManager;

@Environment(EnvType.CLIENT)
public class BlueLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworkManager.registerClientHandlers();
    }
}
