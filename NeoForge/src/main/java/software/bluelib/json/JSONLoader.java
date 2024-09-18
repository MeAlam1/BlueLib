// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.utils.logging.BaseLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * A {@code class} responsible for loading and parsing JSON data from
 * resources defined by {@link ResourceLocation} within a Minecraft mod environment. <br>
 * It uses the {@link Gson} library to convert JSON strings into {@link JsonObject} instances.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #loadJson(ResourceLocation, ResourceManager)} - Loads a JSON resource from the specified location.</li>
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
     * @param pResourceManager {@link ResourceManager} - The {@link ResourceManager} used to load the resource.
     * @return The loaded {@link JsonObject}. Returns an empty {@link JsonObject} if the resource is not found.
     * @throws RuntimeException if there is an error reading the resource.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public JsonObject loadJson(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
        BaseLogger.bluelibLogInfo("Attempting to load JSON resource: " + pResourceLocation);

        try {
            Optional<Resource> resource = pResourceManager.getResource(pResourceLocation);

            if (resource.isEmpty()) {
                BaseLogger.logWarning("Resource not found: " + pResourceLocation);
                return new JsonObject();
            }

            try (InputStream inputStream = resource.get().open();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                BaseLogger.bluelibLogSuccess("Successfully loaded JSON resource: " + pResourceLocation);
                return jsonObject;
            }
        } catch (IOException pException) {
            RuntimeException exception = new RuntimeException("Failed to load JSON resource: " + pResourceLocation, pException);
            BaseLogger.logError("Failed to load JSON resource: " + pResourceLocation, exception);
            throw exception;
        }
    }
}
