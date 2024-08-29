// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.event;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bluetest.BlueTest;
import software.bluetest.entity.dragon.DragonEntity;
import software.bluetest.entity.dragon.DragonRender;
import software.bluetest.entity.rex.RexEntity;
import software.bluetest.entity.rex.RexRender;
import software.bluetest.init.ModEntities;

@Mod.EventBusSubscriber(modid = BlueTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static void registerRenderers() {
        // Register the renderer for all the Entities
        EntityRenderers.register(ModEntities.DRAGON.get(), DragonRender::new);
        EntityRenderers.register(ModEntities.REX.get(), RexRender::new);
    }
}
