// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import software.bluelib.entity.variant.VariantLoader;

/**
 * A {@link ReloadEventHandler} responsible for handling events related to reloading entity variants.
 * This class includes methods for registering entity variants when the server starts.
 * @author MeAlam
 */
public class ReloadEventHandler {

    /**
     * A {@code void} method that registers entity variants from specified locations. <br>
     * This method will attempt to load variants from both mod and pack locations, printing out status information <br>
     * and catching any exceptions that occur during the loading process.
     *
     * @param pServer {@link MinecraftServer} - The server instance of the current world.
     * @param pEntityName {@link String} - The entity name you wish to load.
     * @param modID {@link String} - The mod ID used to locate the entity variant resources. (Use your Mods ID)
     * @param modPathLocation {@link String} - The path location within the mod where variants are stored.
     * @param packPathLocation {@link String} - The path location within the resource pack where variants are stored.
     * @author MeAlam
     */
    protected static void registerEntityVariants(MinecraftServer pServer, String pEntityName, String modID, String modPathLocation, String packPathLocation) {
        ResourceLocation modLocation = new ResourceLocation(modID, modPathLocation);
        ResourceLocation packLocation = new ResourceLocation(modID, packPathLocation);

        System.out.println("Attempting to load variants from:");
        System.out.println("Mod Location: " + modLocation);
        System.out.println("Pack Location: " + packLocation);

        try {
            VariantLoader.loadVariants(modLocation, packLocation, pServer, pEntityName);
            System.out.println("Successfully loaded variants from " + modLocation + " and " + packLocation);
        } catch (Exception e) {
            System.err.println("Failed to load variants from " + modLocation + " and " + packLocation);
        }
    }
}
