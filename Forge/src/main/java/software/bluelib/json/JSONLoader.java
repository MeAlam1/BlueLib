// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.exception.CouldNotLoadJSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * The {@code JSONLoader} class is responsible for loading and parsing JSON data from
 * resources defined by {@link ResourceLocation} within a Minecraft mod environment. <br>
 * It uses the {@link Gson} library to convert JSON strings into {@link JsonObject} instances.
 * <p>
 * Key methods:
 * <ul>
 *   <li>{@link #loadJson(ResourceLocation, ResourceManager)} - Loads a JSON resource.</li>
 * </ul>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class JSONLoader {

    /**
     * A {@link Gson} instance for parsing JSON data.
     * @Co-author MeAlam, Dan
     */
    private static final Gson gson = new Gson();

    /**
     * A {@link JsonObject} that loads JSON data from a {@link ResourceLocation}. <br>
     * This method is typically used to load configuration files or other JSON-based resources
     * in a Minecraft mod environment.
     * <p>
     * @param pResourceLocation {@link ResourceLocation} - The {@link ResourceLocation} of the JSON resource.
     * @param pResourceManager {@link ResourceManager} - The {@link ResourceManager} to load the resource.
     * @return The loaded {@link JsonObject}.
     * @throws CouldNotLoadJSON If the JSON could not be loaded.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public JsonObject loadJson(ResourceLocation pResourceLocation, ResourceManager pResourceManager) throws CouldNotLoadJSON {
        try {
            Optional<Resource> resource = pResourceManager.getResource(pResourceLocation);

            if (resource.isEmpty()) {
                return new JsonObject();
            }

            try (InputStream inputStream = resource.get().open();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                return gson.fromJson(reader, JsonObject.class);
            }
        } catch (IOException pException) {
            throw new CouldNotLoadJSON("Failed to load JSON from resource: " + pResourceLocation + ". Error: " + pException.getMessage(), pResourceLocation.toString());
        }
    }
}
