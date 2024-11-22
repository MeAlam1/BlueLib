// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.BlueLibConstants;
import software.bluelib.event.ReloadEventHandler;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@code ReloadHandler} class that handles server start and reload events related to entity variants.
 * <p>
 * This class extends {@link ReloadEventHandler} and implements event handling for server starting and reloading,
 * ensuring that entity variant data is properly loaded and refreshed.
 * </p>
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #onServerStart(ServerStartingEvent)} - Handles server starting events to initialize entity variants.</li>
 *   <li>{@link #onReload(AddReloadListenerEvent)} - Handles reload events to refresh entity variants.</li>
 *   <li>{@link #LoadEntityVariants(MinecraftServer)} - Loads entity variants from JSON files into the server.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.3.0
 * @since 1.0.0
 */
@EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    /**
     * The {@link MinecraftServer} instance for the server handling the events.
     * <p>
     * This is initialized when the server starts and used to load entity variants.
     * </p>
     *
     * @since 1.0.0
     */
    private static MinecraftServer server;

    /**
     * Handles the server starting event to initialize the {@link MinecraftServer} instance
     * and load entity variants.
     *
     * @param pEvent {@link ServerStartingEvent} - The event triggered when the server starts.
     * @author MeAlam
     * @since 1.0.0
     */
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(server);
        BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded.", true);
    }

    /**
     * Handles the reload event by scheduling a task to reload entity variants.
     * <p>
     * This method schedules the {@code LoadEntityVariants} method to run after a short delay.
     * </p>
     *
     * @param pEvent {@link AddReloadListenerEvent} - The event triggered when a reload occurs.
     * @author MeAlam
     * @since 1.0.0
     */
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent pEvent) {
        if (server != null) {
            BlueLibConstants.SCHEDULER.schedule(() -> {
                server.execute(() -> {
                    ReloadHandler.LoadEntityVariants(server);
                    BaseLogger.log(BaseLogLevel.INFO, "Entity variants reloaded.", true);
                });
            }, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * The base path for entity variant JSON files.
     * <p>
     * This path is used to locate the files that contain variant data for entities.
     * </p>
     *
     * @since 1.0.0
     */
    private static final String basePath = "variant/entity/";


    /**
     * The entities. This list contains the names of the entities for which variants are loaded.
     *
     * @since 1.0.0
     */
    private static final List<String> ENTITY_NAMES = Arrays.asList("dragon", "rex");

    /**
     * Loads entity variants from JSON files into the {@link MinecraftServer}.
     * <p>
     * This method iterates through the list of entity names, constructs file paths, and registers
     * entity variants using the {@link ReloadEventHandler}.
     * </p>
     *
     * @param pServer {@link MinecraftServer} - The server on which the entity variants will be loaded.
     * @since 1.0.0
     * @author MeAlam
     */
    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : ENTITY_NAMES) {
            String folderPath = basePath + entityName;
            ReloadEventHandler.registerEntityVariants(folderPath, pServer, BlueLibConstants.MOD_ID, entityName);
            BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded for " + entityName + ".", true);
        }
    }

}
