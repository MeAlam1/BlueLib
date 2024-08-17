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
 * A {@code Class} that loads JSON data from a {@link ResourceLocation}.
 * @author MeAlam
 */
public class JSONLoader {

    /**
     * A {@link Gson} instance for parsing JSON data.
     */
    private static final Gson gson = new Gson();

    /**
     * A {@link JsonObject} that Loads JSON data from a {@link ResourceLocation}.
     *
     * @param pResourceLocation {@link ResourceLocation} - The {@link ResourceLocation} of the JSON resource.
     * @param pResourceManager {@link ResourceManager} - The {@link ResourceManager} to load the resource.
     * @return The loaded {@link JsonObject}.
     * @throws CouldNotLoadJSON If the JSON could not be loaded.
     * @author MeAlam
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
        } catch (IOException e) {
            throw new CouldNotLoadJSON("Failed to load JSON from resource: " + pResourceLocation + ". Error: " + e.getMessage(), pResourceLocation.toString());
        }
    }
}
