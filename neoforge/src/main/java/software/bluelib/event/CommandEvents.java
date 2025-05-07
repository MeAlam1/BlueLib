package software.bluelib.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import software.bluelib.registry.CommandRegistry;

@EventBusSubscriber
public class CommandEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent pEvent) {
        CommandRegistry.registerCommands(pEvent.getDispatcher());
    }
}
