// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib_examples.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber
public class ReloadHandler {

    private static MinecraftServer server;

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        server = event.getServer();
        software.bluelib_examples.events.ReloadHandler.loadEntityVariants(server);
    }
}
