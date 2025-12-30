/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import java.util.function.BiConsumer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.event.IEventProxy;
import software.bluelib.config.LoggerConfig;
import software.bluelib.example.render.entity.ExampleRender;
import software.bluelib.internal.registry.BlueEntityRegistry;
import software.bluelib.platform.IPlatformClient;
import software.bluelib.platform.IPlatformHelper;
import software.bluelib.platform.IRegistryHelper;

@SuppressWarnings("unused")
public class BlueLibCommonClient {

	public static void registerRenderers(@NotNull BiConsumer<EntityType<? extends Entity>, @NotNull EntityRendererProvider> pEntityRenderers,
	                                     @NotNull BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> pBlockEntityRenderers) {
		if (LoggerConfig.isExampleEnabled) {
			pEntityRenderers.accept(BlueEntityRegistry.EXAMPLE.get(), ExampleRender::new);
		}
	}

	public static class PlatformHelper {
		@NotNull
		public static final IPlatformClient ITEM_RENDERING = BlueLibConstants.load(IPlatformClient.class);
	}
}
