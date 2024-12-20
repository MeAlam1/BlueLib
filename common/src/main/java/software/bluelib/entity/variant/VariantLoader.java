// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.entity.variant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import software.bluelib.json.JSONParser;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;
import software.bluelib.utils.variant.ParameterUtils;

/**
 * A class for loading and managing entity variants.
 * <p>
 * Purpose: This class handles the loading and parsing of entity variants from JSON files.<br>
 * When: The variants are loaded when the {@link #loadVariants(String, MinecraftServer, String)} method is called.<br>
 * Where: The variants are stored in a static map and can be accessed globally.<br>
 * Additional Info: This class extends {@link JSONParser} to utilize its JSON parsing capabilities.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #loadVariants(String, MinecraftServer, String)} - Loads variants from a specified folder path.</li>
 * <li>{@link #parseVariants(String, JsonObject)} - Parses the loaded JSON data and stores the variants.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @see JSONParser
 * @see ParameterUtils
 * @see BaseLogger
 * @since 1.0.0
 */
public class VariantLoader extends JSONParser {

    /**
     * A map to store all loaded variants.
     * <p>
     * Purpose: This map holds all the variants loaded from JSON files, keyed by entity name.<br>
     * When: Populated when the {@link #loadVariants(String, MinecraftServer, String)} method is called.<br>
     * Where: Used globally to access the loaded variants.<br>
     * Additional Info: The map is static to allow global access.
     * </p>
     *
     * @see #loadVariants(String, MinecraftServer, String)
     * @see #parseVariants(String, JsonObject)
     * @since 1.3.0
     */
    public static final Map<String, JsonObject> AllVariants = new HashMap<>();

    /**
     * A singleton instance of the VariantLoader.
     * <p>
     * Purpose: This instance is used to load and parse JSON data.<br>
     * When: Initialized when the class is loaded.<br>
     * Where: Used internally within the class methods.<br>
     * Additional Info: Ensures only one instance of VariantLoader is used.
     * </p>
     *
     * @see #loadVariants(String, MinecraftServer, String)
     * @since 1.7.0
     */
    private static final VariantLoader LOADER = new VariantLoader();

    /**
     * Loads variants from the specified folder path.
     * <p>
     * Purpose: This method loads JSON data from the specified folder path and parses the variants.<br>
     * When: Called when variants need to be loaded for a specific entity.<br>
     * Where: Typically invoked during server initialization or entity configuration.<br>
     * Additional Info: The loaded variants are stored in the {@link #AllVariants} map.
     * </p>
     *
     * @param pFolderPath The folder path to load JSON data from.
     * @param pServer     The Minecraft server instance.
     * @param pEntityName The name of the entity to load variants for.
     * @author MeAlam
     * @see #AllVariants
     * @see #parseVariants(String, JsonObject)
     * @see JSONParser#loadData(String, MinecraftServer)
     * @see JSONParser#getDataMap()
     * @since 1.0.0
     */
    public static void loadVariants(String pFolderPath, MinecraftServer pServer, String pEntityName) {
        LOADER.loadData(pFolderPath, pServer);
        AllVariants.putAll(LOADER.getDataMap());
        parseVariants(pEntityName, LOADER.getMergedJsonObject());
        BaseLogger.log(BaseLogLevel.INFO, "All data of Variants: " + AllVariants, true);
    }

    /**
     * Parses the loaded JSON data and stores the variants.
     * <p>
     * Purpose: This method parses the JSON data and stores the variants in the {@link #AllVariants} map.<br>
     * When: Called internally after loading the JSON data.<br>
     * Where: Invoked within the {@link #loadVariants(String, MinecraftServer, String)} method.<br>
     * Additional Info: The parsed variants are logged for debugging purposes.
     * </p>
     *
     * @param pEntityName The name of the entity to parse variants for.
     * @param pJsonObject The JSON object containing the variant data.
     * @author MeAlam
     * @see #AllVariants
     * @see #loadVariants(String, MinecraftServer, String)
     * @see ParameterUtils#getAllEntities()
     * @see ParameterUtils#getVariantsOfEntity(String)
     * @since 1.0.0
     */
    private static void parseVariants(String pEntityName, JsonObject pJsonObject) {
        for (Map.Entry<String, JsonElement> ignored : pJsonObject.entrySet()) {
            AllVariants.putIfAbsent(pEntityName, pJsonObject);
        }
        BaseLogger.log(BaseLogLevel.INFO, "All Entities: " + ParameterUtils.getAllEntities(), true);
        BaseLogger.log(BaseLogLevel.INFO, "Variants of " + pEntityName + ": " + ParameterUtils.getVariantsOfEntity(pEntityName), true);
    }
}
