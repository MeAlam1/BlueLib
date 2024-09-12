// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bluelib.BlueLib;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexRender;
import software.bluelib.example.init.ModEntities;

@Mod.EventBusSubscriber(modid = BlueLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static void registerRenderers() {
        if (BlueLib.EditorMode) {
            // Register the renderer for all the Entities
            EntityRenderers.register(ModEntities.DRAGON.get(), DragonRender::new);
            EntityRenderers.register(ModEntities.REX.get(), RexRender::new);
        }
    }
}
