// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import software.bluelib.entity.variant.VariantLoader;

import java.io.IOException;

/**
 * A {@link ReloadEventHandler} responsible for handling events related to reloading entity variants.
 * This class includes methods for registering entity variants when the server starts.
 * @author MeAlam
 * @Co-author Dan
 */
public class ReloadEventHandler {

    /**
     * A {@code void} method that registers entity variants from specified locations. <br>
     * This method will attempt to load variants from both mod and pack locations, printing out status information <br>
     * and catching any exceptions that occur during the loading process.
     *
     * @param pServer {@link MinecraftServer} - The server instance of the current world.
     * @param pEntityName {@link String} - The entity name you wish to load.
     * @param pModID {@link String} - The mod ID used to locate the entity variant resources. (Use your Mods ID)
     * @param pModPathLocation {@link String} - The path location within the mod where variants are stored.
     * @param pDataPathLocation {@link String} - The path location within the resource pack where variants are stored.
     * TODO: Add Exceptions in Comment
     * @author MeAlam
     * @Co-author Dan
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
