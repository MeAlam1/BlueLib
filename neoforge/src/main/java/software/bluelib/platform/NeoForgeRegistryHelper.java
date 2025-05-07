// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import software.bluelib.BlueLibConstants;
import software.bluelib.net.NeoForgeNetworkManager;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new NeoForgeNetworkManager();
    }
}
