/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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
import software.bluelib.BlueLibConstants;

@Environment(EnvType.CLIENT)
public class BlueLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
				.registerReloadListener(new IdentifiableResourceReloadListener() {

					@Override
					public @NotNull ResourceLocation getFabricId() {
						return BlueLibConstants.BlueLoader.RELOAD_LISTENER_ID;
					}

					@Override
					public @NotNull CompletableFuture<Void> reload(
							@NotNull PreparationBarrier pSynchronizer,
							@NotNull ResourceManager pResourceManager,
							@NotNull ProfilerFiller pPrepareProfiler,
							@NotNull ProfilerFiller pApplyProfiler,
							@NotNull Executor pPrepareExecutor,
							@NotNull Executor pApplyExecutor) {
						return ResourceCache.reloadClient(pSynchronizer, pResourceManager, pPrepareProfiler, pApplyProfiler, pPrepareExecutor, pApplyExecutor);
					}
				});
	}
}
