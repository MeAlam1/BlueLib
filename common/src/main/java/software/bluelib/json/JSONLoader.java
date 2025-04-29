// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

public class JSONLoader {

    private static final Gson gson = new Gson();

    public JsonObject loadJson(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
        try {
            Optional<Resource> resource = pResourceManager.getResource(pResourceLocation);

            if (resource.isEmpty()) {
                BaseLogger.log(BaseLogLevel.ERROR, "Resource not found: " + pResourceLocation, true);
                return new JsonObject();
            }

            try (InputStream inputStream = resource.get().open();
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                BaseLogger.log(BaseLogLevel.SUCCESS, "Successfully loaded JSON resource: " + pResourceLocation, true);
                return jsonObject;
            }
        } catch (IOException pException) {
            RuntimeException exception = new RuntimeException("Failed to load JSON resource: " + pResourceLocation, pException);
            BaseLogger.log(BaseLogLevel.ERROR, "Failed to load JSON resource: " + pResourceLocation, exception, true);
            throw exception;
        }
    }
}
