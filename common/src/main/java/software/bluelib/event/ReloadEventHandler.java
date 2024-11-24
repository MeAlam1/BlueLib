// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import software.bluelib.entity.variant.VariantLoader;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

/**
 * A {@code class} responsible for handling events related to reloading entity variants.
 * <p>
 * This class provides functionality to register entity variants from specified locations when the server starts.
 * </p>
 * <p>
 * Key Features:
 * <ul>
 * <li>{@link #registerEntityVariants(String, MinecraftServer, String, String)} - Registers entity variants from specified locations.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.0.0
 * @see VariantLoader
 * @see MinecraftServer
 * @see ResourceLocation
 * @since 1.0.0
 */
public class ReloadEventHandler {

    /**
     * A {@code protected static void} that registers entity variants from specified locations.
     * <p>
     * This method attempts to load variants from both mod and datapack locations. It logs status information and
     * handles exceptions that occur during the loading process.
     * </p>
     * <p>
     * Parameters:
     * <ul>
     * <li>{@code pFolderPath} {@link String} - The folder path location within the mod or datapack where variants are stored.</li>
     * <li>{@code pServer} {@link MinecraftServer} - The server instance of the current world.</li>
     * <li>{@code pModID} {@link String} - The mod ID used to locate the entity variant resources. (Use your Mod's ID)</li>
     * <li>{@code pEntityName} {@link String} - The entity name to load.</li>
     * </ul>
     * <p>
     * Exception Handling:
     * <ul>
     * <li>{@link JsonParseException} - Thrown when there is an error parsing the JSON files.</li>
     * <li>{@link RuntimeException} - Thrown for unexpected errors during the registration process.</li>
     * </ul>
     *
     * @param pFolderPath {@link String} - The folder path location within the mod or datapack where variants are stored.
     * @param pServer     {@link MinecraftServer} - The server instance of the current world.
     * @param pModID      {@link String} - The mod ID used to locate the entity variant resources. (Use your Mod's ID)
     * @param pEntityName {@link String} - The entity name to load.
     * @throws JsonParseException if there is an error parsing the JSON files.
     * @throws RuntimeException   if an unexpected error occurs during the registration process.
     * @author MeAlam
     * @see MinecraftServer
     * @see ResourceLocation
     * @see VariantLoader
     * @since 1.0.0
     */
    protected static void registerEntityVariants(String pFolderPath, MinecraftServer pServer, String pModID, String pEntityName) {
        BaseLogger.log(BaseLogLevel.INFO, "Attempting to register entity variants for " + pEntityName + " with ModID: " + pModID, true);

        try {
            VariantLoader.loadVariants(pFolderPath, pServer, pEntityName);
            BaseLogger.log(BaseLogLevel.SUCCESS, "Successfully registered entity variants for " + pEntityName + " from ModID: " + pModID, true);
        } catch (JsonParseException pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Failed to parse JSON(s) while registering entity variants for " + pEntityName + " from ModID: " + pModID, pException, true);
            throw pException;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Unexpected error occurred while registering entity variants for " + pEntityName + " from ModID: " + pModID, pException, true);
            throw pException;
        }
    }
}
