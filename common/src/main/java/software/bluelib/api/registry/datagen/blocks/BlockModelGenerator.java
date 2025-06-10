package software.bluelib.api.registry.datagen.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import software.bluelib.BlueLibConstants;

public class BlockModelGenerator {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping() //Prevent escaping of =, <, >, etc.
            .create();

    public static void generateBlockModel(String modId, String name, BlockModelTemplates blockModelTemplate) {
        generateBlockModel(modId, name, blockModelTemplate, Collections.emptyMap());
    }

    public static void generateBlockModel(String modId, String name, BlockModelTemplates blockModelTemplate, Map<String, String> properties) {
        Map<String, JsonObject> blockModelJsons = blockModelTemplate.generateBlockModel(modId, name, properties);

        for (Map.Entry<String, JsonObject> entry : blockModelJsons.entrySet()) {
            String modelName = entry.getKey();
            JsonObject blockModelJson = entry.getValue();
            Path blockModelPath = Path.of(BlueLibConstants.PlatformHelper.PLATFORM.getAssetsDir(true) + "/models/block/" + name + ".json");

            try {
                if (Files.exists(blockModelPath)) {
                    System.out.println("Block model for '" + modelName + "' already exists at: " + blockModelPath + ". Skipping creation.");
                    continue;
                }

                Files.createDirectories(blockModelPath.getParent());
                Files.write(blockModelPath, GSON.toJson(blockModelJson).getBytes(), StandardOpenOption.CREATE_NEW);
                System.out.println("Block model for '" + modelName + "' created at: " + blockModelPath);
                System.out.println("Generated JSON for '" + modId + ":models/block/" + modelName + "':\n" + GSON.toJson(blockModelJson));

            } catch (IOException e) {
                System.err.println("[ERROR]: Failed to create block model for '" + modelName + "' at " + blockModelPath + ": " + e.getMessage());
            }
        }
    }
}
