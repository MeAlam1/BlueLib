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
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import software.bluelib.event.FabricReloadHandler;
import software.bluelib.example.event.VariantProvider;
import software.bluelib.internal.registry.BlueEntityRegistry;
import software.bluelib.net.FabricNetworkManager;

public class BlueLib implements ModInitializer {

	private boolean hasInitialized = false;

	@Override
	public void onInitialize() {
		FabricEvents.register();

		BlueLibCommon.doRegistration();

		FabricNetworkManager.registerServerPackets();

		BlueEntityRegistry.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

		FabricReloadHandler.registerProvider(new VariantProvider());
		clientEndTick();
	}

	@NotNull
	private Boolean isClientEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	private void clientEndTick() {
		if (isClientEnvironment()) {
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (!hasInitialized) {
					hasInitialized = true;
					BlueLibCommon.init();
				}
			});
		}
	}
}
