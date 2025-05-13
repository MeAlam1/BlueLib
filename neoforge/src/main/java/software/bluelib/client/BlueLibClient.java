// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.helpers.entity.RenderHelper;

@Mod(value = BlueLibConstants.MOD_ID, dist = Dist.CLIENT)
public class BlueLibClient {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers pEvent) {
        RenderHelper.registerRenderers(pEvent::registerEntityRenderer, pEvent::registerBlockEntityRenderer);
    }

    public BlueLibClient(IEventBus pModEventBus, ModContainer pModContainer) {
        pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
