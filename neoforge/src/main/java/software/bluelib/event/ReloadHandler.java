package software.bluelib.event;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.BlueLibCommon;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.entity.variant.IVariantProvider;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.entity.variant.VariantLoader;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class ReloadHandler {

    private static IVariantProvider provider;

    public static void setProvider(IVariantProvider pVariantProvider) {
        provider = pVariantProvider;
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        if (provider == null) return;

        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        BlueLibConstants.server = pEvent.getServer();
        loadEntityVariants(pEvent.getServer().getResourceManager());
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded"), true);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent pEvent) {
        if (provider == null) return;

        loadEntityVariants(pEvent.getPlayerList().getServer().getResourceManager());
        BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.reloaded"), true);
    }

    private static void loadEntityVariants(ResourceManager pResourceManager) {
        List<String> entityNames = provider.getEntityNames();
        String basePath = provider.getBasePath();

        for (String entityName : entityNames) {
            String folderPath = basePath + entityName;
            VariantLoader.loadVariants(folderPath, pResourceManager, entityName);
            BaseLogger.log(BaseLogLevel.INFO, BlueLibCommon.Translation.log("variants.loaded.entity", entityName), true);
        }
    }
}
