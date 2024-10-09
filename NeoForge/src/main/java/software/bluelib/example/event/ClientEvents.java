// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import software.bluelib.example.entity.dragon.DragonEntity;
import software.bluelib.example.entity.dragon.DragonRender;
import software.bluelib.example.entity.rex.RexEntity;
import software.bluelib.example.entity.rex.RexRender;
import software.bluelib.example.init.ModEntities;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers pEvent) {
        // Register the renderer for all the Entities
        pEvent.registerEntityRenderer(ModEntities.DRAGON.get(), DragonRender::new);
        pEvent.registerEntityRenderer(ModEntities.REX.get(), RexRender::new);
        BaseLogger.log(BaseLogLevel.INFO, "Registered Renderers for Entities", true);
    }

    // Register the Attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent pEvent) {
        pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
        pEvent.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        BaseLogger.log(BaseLogLevel.INFO, "Registered Attributes for Entities", true);
    }
}
