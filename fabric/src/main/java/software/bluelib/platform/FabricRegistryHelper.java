// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.platform;

import software.bluelib.BlueLibConstants;
import software.bluelib.net.FabricNetworkManager;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public BlueLibConstants.NetworkManager getNetwork() {
        return new FabricNetworkManager();
    }
}
