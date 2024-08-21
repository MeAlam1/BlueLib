package software.bluetest.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.BlueTest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    private static final String BASE_PATH = "variant/entity/";
    private static final List<String> ENTITY_NAMES = Arrays.asList("dragon", "rex");

    @SubscribeEvent
    public static void onDataPackReload(ServerStartingEvent pEvent) {
        System.out.println("Server is being Started!");
        ReloadHandler.LoadEntityVariants(pEvent.getServer());
    }

    @SubscribeEvent
    public static void onCommandExecuted(CommandEvent pEvent) {
        String commandName = pEvent.getParseResults().getReader().getString();
        System.out.println("Command " + commandName + " is being executed");
        if (Objects.equals(commandName, "reload")) {
            MinecraftServer server = pEvent.getParseResults().getContext().getSource().getServer();
            ReloadHandler.LoadEntityVariants(server);
        }
    }

    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : ENTITY_NAMES) {
            String jsonPath = BASE_PATH + entityName + ".json";
            String dataPath = BASE_PATH + entityName + "data.json";
            ReloadEventHandler.registerEntityVariants(BlueTest.MODID, pServer, jsonPath, dataPath, entityName);
        }
    }
}
