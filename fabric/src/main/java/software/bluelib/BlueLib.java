/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import software.bluelib.event.ReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer {

	private boolean hasInitialized = false;

	@Override
	public void onInitialize() {
		BlueLibConstants.init();
		BlueLibCommon.doRegistration();
		FabricEvents.register();

		if (isClientEnvironment()) {
			BlueLibCommon.doClientRegistration();
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				FabricNetworkManager.registerClientHandlers();
				if (!hasInitialized) {
					hasInitialized = true;
					BlueLibCommon.init();
				}
			});
		}

		registerNetwork();
		
		ReloadHandler.registerProvider(new VariantProvider());
	}

	private void registerNetwork() {
		FabricNetworkManager.registerMessages();
		FabricNetworkManager.registerServerHandlers();
	}

	private boolean isClientEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
}
