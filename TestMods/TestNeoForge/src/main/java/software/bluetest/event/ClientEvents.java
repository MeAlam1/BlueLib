package software.bluetest.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import software.bluetest.entity.dragon.DragonEntity;
import software.bluetest.entity.dragon.DragonRender;
import software.bluetest.init.ModEntities;

public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers pEvent) {
        // Register the renderer for all the Entities
        pEvent.registerEntityRenderer(ModEntities.DRAGON.get(), DragonRender::new);
    }

    // Register the Attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent pEvent) {
        pEvent.put(ModEntities.DRAGON.get(), DragonEntity.createAttributes().build());
    }
}
