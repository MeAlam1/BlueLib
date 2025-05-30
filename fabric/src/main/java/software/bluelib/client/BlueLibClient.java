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
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;
import software.bluelib.client.loader.cache.ResourceCache;
import software.bluelib.net.FabricNetworkManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
public class BlueLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FabricNetworkManager.registerClientHandlers();
		BlueLibCommon.doClientRegistration();

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
				.registerReloadListener(new IdentifiableResourceReloadListener() {
					@Override
					public ResourceLocation getFabricId() {
						return ResourceCache.RELOAD_LISTENER_ID;
					}

					@Override
					public @NotNull CompletableFuture<Void> reload(PreparationBarrier pSynchronizer, ResourceManager pResourceManager,
					                                               ProfilerFiller pPrepareProfiler, ProfilerFiller pApplyProfiler, Executor pPrepareExecutor,
					                                               Executor pApplyExecutor) {
						return ResourceCache.reload(pSynchronizer, pResourceManager, pPrepareProfiler, pApplyProfiler, pPrepareExecutor, pApplyExecutor);
					}
				});
	}
}
