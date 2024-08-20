package software.bluelib.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.entity.variant.VariantLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link ReloadEventHandler} that handles the reloading of entity variants when data packs are reloaded. <br>
 * This class listens for the {@link ServerStartingEvent} and loads all registered entity variants using custom paths. <br>
 * It uses a {@link Map} to store the variant data for different mods and entities. <br> <br>
 *
 * The entity variant data is registered using the {@code registerEntityVariants} method, <br>
 * which associates each entity name with its corresponding JSON paths. <br> <br>
 *
 * The data is then processed during the {@link ServerStartingEvent}, where all registered variants are loaded. <br>
 * This class also includes a helper record {@link EntityVariantPaths} to encapsulate the paths. <br>
 *
 * @author MeAlam
 */
@Mod.EventBusSubscriber
public class ReloadEventHandler {

    /**
     * A {@link Map} that stores the entity variant data, categorized by mod ID and entity name. <br>
     * The outer map's key is the mod ID, and the inner map's key is the entity name. <br>
     * Each entity name is associated with an {@link EntityVariantPaths} object that holds the JSON paths.
     */
    private static final Map<String, Map<String, EntityVariantPaths>> entityVariantData = new HashMap<>();

    /**
     * A {@code void} that registers entity variants for a specific mod, <br>
     * associating the entity name with custom paths for the variant JSON files. <br>
     * This method allows mods to specify where their variant data is located, <br>
     * which will be used during the reload event.
     *
     * @param pModId {@link String} - The ID of the mod registering the entity variants.
     * @param pEntityName {@link String} - The name of the entity whose variants are being registered.
     * @param pModPath {@link String} - The path to the mod-specific JSON file that defines the variants.
     * @param pPackPath {@link String} - The path to the data pack JSON file that provides additional data.
     * @author MeAlam
     */
    public static void registerEntityVariants(String pModId, String pEntityName, String pModPath, String pPackPath) {
        entityVariantData
                .computeIfAbsent(pModId, k -> new HashMap<>())
                .put(pEntityName, new EntityVariantPaths(pModPath, pPackPath));
    }

    /**
     * A {@code void} that is triggered when the server is starting and data packs are being reloaded. <br>
     * This method processes all registered entity variants by calling {@code loadAllEntityVariants}.
     *
     * @param pEvent {@link ServerStartingEvent} - The event that indicates the server is starting.
     * @author MeAlam
     */
    @SubscribeEvent
    public static void onDataPackReload(ServerStartingEvent pEvent) {
        loadAllEntityVariants(pEvent);
    }

    /**
     * A {@code void} that iterates through all registered entity variants and loads them. <br>
     * It delegates the actual loading of each entity variant to the {@code loadEntityVariant} method.
     *
     * @param pEvent {@link ServerStartingEvent} - The event that indicates the server is starting.
     * @author MeAlam
     */
    private static void loadAllEntityVariants(ServerStartingEvent pEvent) {
        entityVariantData.forEach((modId, entities) -> {
            entities.forEach((entityName, paths) -> {
                loadEntityVariant(pEvent, modId, entityName, paths);
            });
        });
    }

    /**
     * A {@code void} that loads the variants for a specific entity using the provided paths.
     * This method constructs the {@link ResourceLocation} for both the mod and pack paths
     * and then loads the variants using {@link VariantLoader}.
     *
     * @param pEvent {@link ServerStartingEvent} - The event that indicates the server is starting.
     * @param pModId {@link String} - The ID of the mod to which the entity belongs.
     * @param pEntityName {@link String} - The name of the entity whose variants are being loaded.
     * @param pPaths {@link EntityVariantPaths} - The paths to the JSON files for the entity's variants.
     * @author MeAlam
     */
    private static void loadEntityVariant(ServerStartingEvent pEvent, String pModId, String pEntityName, EntityVariantPaths pPaths) {
        ResourceLocation modLocation = new ResourceLocation(pModId, pPaths.pModPath());
        ResourceLocation packLocation = new ResourceLocation(pModId, pPaths.pPackPath());
        VariantLoader.loadVariants(modLocation, packLocation, pEvent.getServer());
    }

    /**
     * A {@link EntityVariantPaths} record that encapsulates the paths to the mod and pack JSON files associated with an entity's variants. <br>
     * The paths are stored as {@link String}s and are used during the variant loading process.
     *
     * @param pModPath {@link String} - The path to the mod-specific JSON file that defines the variants.
     * @param pPackPath {@link String} - The path to the data pack JSON file that provides additional data.
     * @author MeAlam
     */
    private record EntityVariantPaths(String pModPath, String pPackPath) {
    }
}
