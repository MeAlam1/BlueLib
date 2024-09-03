// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLib;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexRender;
import software.bluelib.example.init.ModEntities;

@Mod.EventBusSubscriber(modid = BlueLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static void registerRenderers() {
        // Register the renderer for all the Entities
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DRAGON.get(), DragonRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.REX.get(), RexRender::new);
    }
}
