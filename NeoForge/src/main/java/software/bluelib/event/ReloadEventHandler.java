// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import software.bluelib.entity.variant.VariantLoader;

/**
 * A {@link ReloadEventHandler} responsible for handling events related to reloading entity variants.
 * This class includes methods for registering entity variants when the server starts.
 * @author MeAlam
 */
public class ReloadEventHandler {

    /**
     * A {@code void} method that registers entity variants from specified locations during the server starting event. <br>
     * This method will attempt to load variants from both mod and pack locations, printing out status information <br>
     * and catching any exceptions that occur during the loading process.
     *
     * @param modID {@link String} - The mod ID used to locate the entity variant resources. (Use your Mods ID)
     * @param event {@link ServerStartingEvent} - The event triggered when the server starts, used to access the server instance.
     * @param modPathLocation {@link String} - The path location within the mod where variants are stored.
     * @param packPathLocation {@link String} - The path location within the resource pack where variants are stored.
     * @author MeAlam
     */
    protected static void registerEntityVariants(String modID, ServerStartingEvent event, String modPathLocation, String packPathLocation) {
        ResourceLocation modLocation = new ResourceLocation(modID, modPathLocation);
        ResourceLocation packLocation = new ResourceLocation(modID, packPathLocation);

        System.out.println("Attempting to load variants from:");
        System.out.println("Mod Location: " + modLocation);
        System.out.println("Pack Location: " + packLocation);

        try {
            VariantLoader.loadVariants(modLocation, packLocation, event.getServer());
            System.out.println("Successfully loaded variants from " + modLocation + " and " + packLocation);
            System.out.println("Current number of variants: " + VariantLoader.getVariants().size());
        } catch (Exception e) {
            System.err.println("Failed to load variants from " + modLocation + " and " + packLocation);
        }
    }
}
