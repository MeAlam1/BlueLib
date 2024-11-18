// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.example.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import software.bluelib.BlueLibConstants;
import software.bluelib.event.ReloadEventHandler;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@code ReloadHandler} class that handles server start and reload events related to entity variants.
 * <p>
 * This class extends {@link ReloadEventHandler} and implements event handling for server starting and reloading,
 * ensuring that entity variant data is properly loaded and refreshed.
 * </p>
 * Key Methods:
 * <ul>
 *   <li>{@link #onServerStart(MinecraftServer)} - Handles server starting events to initialize entity variants.</li>
 *   <li>{@link #onReload()} - Handles reload events to refresh entity variants.</li>
 *   <li>{@link #LoadEntityVariants(MinecraftServer)} - Loads entity variants from JSON files into the server.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @since 1.0.0
 */
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
     * @param pServer {@link MinecraftServer} - The server triggered when the server starts.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void onServerStart(MinecraftServer pServer) {
        BlueLibConstants.SCHEDULER = new ScheduledThreadPoolExecutor(1);
        server = pServer;
        ReloadHandler.LoadEntityVariants(server);
        BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded.", true);
    }

    /**
     * Handles the reload event by scheduling a task to reload entity variants.
     * <p>
     * This method schedules the {@code LoadEntityVariants} method to run after a short delay.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    public static void onReload() {
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
     * A {@link List} of entity names for which variants will be loaded.
     * <p>
     * This list defines which entities will have their variants loaded from JSON files.
     * </p>
     *
     * @since 1.0.0
     */
    private static final List<String> entityNames = Arrays.asList("dragon", "rex");

    /**
     * Loads entity variants from JSON files into the {@link MinecraftServer}.
     * <p>
     * This method iterates through the list of entity names, constructs file paths, and registers
     * entity variants using the {@link ReloadEventHandler}.
     * </p>
     *
     * @param pServer {@link MinecraftServer} - The server on which the entity variants will be loaded.
     * @author MeAlam
     * @since 1.0.0
     */
    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : entityNames) {
            String folderPath = basePath + entityName;
            ReloadEventHandler.registerEntityVariants(folderPath, pServer, BlueLibConstants.MOD_ID, entityName);
            BaseLogger.log(BaseLogLevel.INFO, "Entity variants loaded for " + entityName + ".", true);
        }
    }
}
