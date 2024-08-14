package software.bluetest;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import software.bluetest.event.ClientEvents;
import software.bluetest.init.ModEntities;

import java.io.IOException;
import java.util.Optional;

@Mod(BlueTest.MODID)
public class BlueTest
{
    public static final String MODID = "bluetest";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BlueTest(IEventBus pModEventBus)
    {
        //NeoForge.EVENT_BUS.register(this);
        ModEntities.REGISTRY.register(pModEventBus);

        pModEventBus.addListener(ClientEvents::registerAttributes);
        pModEventBus.addListener(ClientEvents::registerRenderers);
    }
    // This method will be used to check the existence of the file
    public static void checkFileExists(MinecraftServer server) {
        ResourceManager resourceManager = server.getResourceManager();
        ResourceLocation resourceLocation = new ResourceLocation(MODID, "variant/entity/dragon.json");

        Optional<Resource> resourceOptional = resourceManager.getResource(resourceLocation);

        if (resourceOptional.isPresent()) {
            LOGGER.info("Resource 'bluetest:variant/entity/dragon.json' exists. GOGO2 1");
        } else {
            LOGGER.info("Resource 'bluetest:variant/entity/dragon.json' does not exist. GOGO2 1");
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventSubscriber {
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            checkFileExists(event.getServer());
        }
    }
}
