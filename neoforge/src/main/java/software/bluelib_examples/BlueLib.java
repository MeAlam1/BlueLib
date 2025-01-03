// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib_examples;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import software.bluelib_examples.client.render.entity.ExampleRender;
import software.bluelib_examples.entity.ExampleEntity;
import software.bluelib_examples.init.ModEntities;

@Mod(BlueLibConstants.MOD_ID)
public class BlueLib {

    public BlueLib(IEventBus pModEventBus, ModContainer pModContainer) {
        ModEntities.register(pModEventBus);
    }

    @EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.EXAMPLE.get(), ExampleRender::new);
        }

        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.EXAMPLE.get(), ExampleEntity.createAttributes().build());
        }
    }
}
