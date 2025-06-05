/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bluelib.BlueLibCommon;
import software.bluelib.net.FabricNetworkManager;

@Environment(EnvType.CLIENT)
public class BlueLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    }
}
