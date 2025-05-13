package software.bluelib.api.registry.datagen.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;

public class BddBlockModelGenerator {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void generateBlockModel(String modId, String name, BddBlockModelTemplates blockModelTemplate) {
        generateBlockModel(modId, name, blockModelTemplate, Collections.emptyMap());
    }

    public static void generateBlockModel(String modId, String name, BddBlockModelTemplates blockModelTemplate, Map<String, String> properties) {
        Map<String, JsonObject> blockModelJsons = blockModelTemplate.generateBlockModel(modId, name, properties);

        for (Map.Entry<String, JsonObject> entry : blockModelJsons.entrySet()) {
            String modelName = entry.getKey();
            JsonObject blockModelJson = entry.getValue();
            Path blockModelPath = findProjectRoot().resolve(modId + "/models/block/" + modelName + ".json");

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

    public static Path findProjectRoot() {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            Path resources = findResourcesPath(current);
            if (resources != null) return resources;
            current = current.getParent();
        }
        throw new IllegalStateException("Could not locate project root");
    }

    private static Path findResourcesPath(Path current) {
        String[] potentialPaths = {
                "src/main/resources/assets",
                "common/src/main/resources/assets"
        };

        for (String path : potentialPaths) {
            Path resources = current.resolve(path);
            if (Files.exists(resources) && Files.isDirectory(resources)) {
                return resources;
            }
        }

        String currentDirName = current.getFileName() != null ? current.getFileName().toString() : "";
        if (currentDirName.matches("fabric|forge|neoforge|quilt")) {
            for (String path : potentialPaths) {
                Path parentResources = current.getParent().resolve(path);
                if (Files.exists(parentResources) && Files.isDirectory(parentResources)) {
                    return parentResources;
                }
            }
        }

        return null;
    }
}
