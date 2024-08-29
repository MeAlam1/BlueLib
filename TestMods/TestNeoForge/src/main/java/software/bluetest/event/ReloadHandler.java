// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluetest.event;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.event.ReloadEventHandler;
import software.bluetest.BlueTest;
import software.bluetest.init.ModEntities;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@code ReloadHandler} class that handles server start and reload events related to entity variants.
 * <p>
 * This class extends {@link ReloadEventHandler} and implements event handling for server starting and reloading,
 * ensuring that entity variant data is properly loaded and refreshed.
 * </p>
 *
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #onServerStart(ServerStartingEvent)} - Handles server starting events to initialize entity variants.</li>
 *   <li>{@link #onReload(AddReloadListenerEvent)} - Handles reload events to refresh entity variants.</li>
 *   <li>{@link #LoadEntityVariants(MinecraftServer)} - Loads entity variants from JSON files into the server.</li>
 * </ul>
 * </p>
 *
 * @since 1.0.0
 * @author MeAlam
 * @Co-author Dan
 */
@Mod.EventBusSubscriber
public class ReloadHandler extends ReloadEventHandler {

    /**
     * The {@link MinecraftServer} instance for the server handling the events.
     * <p>
     * This is initialized when the server starts and used to load entity variants.
     * </p>
     *
     * @since 1.0.0
     * @Co-author MeAlam, Dan
     */
    private static MinecraftServer server;

    /**
     * Handles the server starting event to initialize the {@link MinecraftServer} instance
     * and load entity variants.
     *
     * @param pEvent {@link ServerStartingEvent} - The event triggered when the server starts.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent pEvent) {
        server = pEvent.getServer();
        ReloadHandler.LoadEntityVariants(server);
    }

    /**
     * The {@link ScheduledExecutorService} used to schedule tasks for reloading entity variants.
     *
     * @since 1.0.0
     * @Co-author MeAlam, Dan
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Handles the reload event by scheduling a task to reload entity variants.
     * <p>
     * This method schedules the {@code LoadEntityVariants} method to run after a short delay.
     * </p>
     *
     * @param pEvent {@link AddReloadListenerEvent} - The event triggered when a reload occurs.
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent pEvent) {
        if (server != null) {
            scheduler.schedule(() -> {
                server.execute(() -> {
                    ReloadHandler.LoadEntityVariants(server);
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
     * @Co-author MeAlam, Dan
     */
    private static final String basePath = "variant/entity/";

    /**
     * A {@link List} of entity names for which variants will be loaded.
     * <p>
     * This list defines which entities will have their variants loaded from JSON files.
     * </p>
     *
     * @since 1.0.0
     * @Co-author MeAlam, Dan
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
     *
     * @since 1.0.0
     * @author MeAlam
     * @Co-author Dan
     */
    public static void LoadEntityVariants(MinecraftServer pServer) {
        for (String entityName : entityNames) {
            String modPath = basePath + entityName + ".json";
            String dataPath = basePath + entityName + "data.json";
            ReloadEventHandler.registerEntityVariants(pServer, entityName, BlueTest.MODID, modPath, dataPath);
        }
    }
}
