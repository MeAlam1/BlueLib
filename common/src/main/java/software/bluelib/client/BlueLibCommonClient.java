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
import software.bluelib.example.render.entity.ExampleRender;
import software.bluelib.registry.BlueEntityRegistry;

public class BlueLibCommonClient {

    public static void registerRenderers(BiConsumer<EntityType<? extends Entity>, EntityRendererProvider> pEntityRenderers,
            BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> pBlockEntityRenderers) {
        pEntityRenderers.accept(BlueEntityRegistry.EXAMPLE.get(), ExampleRender::new);
    }
}
