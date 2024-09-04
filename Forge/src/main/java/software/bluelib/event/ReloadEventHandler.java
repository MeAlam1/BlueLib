// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import com.google.gson.JsonParseException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import software.bluelib.entity.variant.VariantLoader;

/**
 * A {@code ReloadEventHandler} class responsible for handling events related to reloading entity variants.
 * <p>
 * This class includes methods for registering entity variants when the server starts.
 * </p>
 * <p>
 * Key Features:
 * <ul>
 *   <li>{@link #registerEntityVariants(MinecraftServer, String, String, String, String)} - Registers entity variants from specified locations.</li>
 * </ul>
 * </p>
 * @see VariantLoader
 * @see MinecraftServer
 * @see ResourceLocation
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class ReloadEventHandler {

    /**
     * A {@code void} method that registers entity variants from specified locations.
     * <p>
     * This method attempts to load variants from both mod and datapack locations, providing status information
     * and handling any exceptions that occur during the loading process.
     * </p>
     * <p>
     * Parameters:
     * <ul>
     *   <li>{@code pServer} - The server instance of the current world.</li>
     *   <li>{@code pEntityName} - The entity name to load.</li>
     *   <li>{@code pModID} - The mod ID used to locate the entity variant resources. (Use your Mod's ID)</li>
     *   <li>{@code pModPathLocation} - The path location within the mod where variants are stored.</li>
     *   <li>{@code pDataPathLocation} - The path location within the resource pack where variants are stored.</li>
     * </ul>
     * </p>
     * <p>
     * Exception Handling:
     * <ul>
     *   <li>{@code JsonParseException} - Thrown when there is an error parsing the JSON files.</li>
     *   <li>{@code RuntimeException} - Thrown for unexpected errors during the registration process.</li>
     * </ul>
     * </p>
     * @param pServer {@link MinecraftServer} - The server instance of the current world.
     * @param pEntityName {@link String} - The entity name to load.
     * @param pModID {@link String} - The mod ID used to locate the entity variant resources. (Use your Mod's ID)
     * @param pModPathLocation {@link String} - The path location within the mod where variants are stored.
     * @param pDataPathLocation {@link String} - The path location within the datapack where variants are stored.
     * @throws JsonParseException if there is an error parsing the JSON files.
     * @throws RuntimeException if an unexpected error occurs during the registration process.
     * @see MinecraftServer
     * @see ResourceLocation
     * @see VariantLoader
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    protected static void registerEntityVariants(MinecraftServer pServer, String pEntityName, String pModID, String pModPathLocation, String pDataPathLocation) {
        ResourceLocation modLocation = new ResourceLocation(pModID, pModPathLocation);
        ResourceLocation dataLocation = new ResourceLocation(pModID, pDataPathLocation);
        try {
            VariantLoader.loadVariants(modLocation, dataLocation, pServer, pEntityName);
        } catch (JsonParseException pException) {
            throw new RuntimeException("Failed to parse JSON(s) while registering entity variants for " + pEntityName + " from Mod with ModID: " + pModID, pException);
        } catch (Exception pException) {
            throw new RuntimeException("Unexpected error occurred while registering entity variants for " + pEntityName + " from Mod with ModID: " + pModID, pException);
        }
    }
}
