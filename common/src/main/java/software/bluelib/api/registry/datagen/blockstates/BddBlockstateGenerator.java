package software.bluelib.api.registry.datagen.blockstates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;

public class BddBlockstateGenerator {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping() // Prevent escaping of =, <, >, etc.
            .create();

    public static void generateBlockstate(String modId, String name, BddBlockstateTemplates blockstateTemplate) {
        generateBlockstate(modId, name, blockstateTemplate, Collections.emptyMap());
    }

    public static void generateBlockstate(String modId, String name, BddBlockstateTemplates blockstateTemplate, Map<String, String> properties) {
        Path blockstatePath = findProjectRoot().resolve(modId + "/blockstates/" + name + ".json");

        try {
            if (Files.exists(blockstatePath)) {
                System.out.println("Blockstate for '" + name + "' already exists at: " + blockstatePath + ". Skipping creation.");
                return;
            }

            JsonElement blockstateJson = generateBlockstateJson(modId, name, blockstateTemplate, properties);

            Files.createDirectories(blockstatePath.getParent());
            Files.write(blockstatePath, GSON.toJson(blockstateJson).getBytes(), StandardOpenOption.CREATE_NEW);
            System.out.println("Blockstate for '" + name + "' created at: " + blockstatePath);

        } catch (IOException e) {
            System.err.println("[ERROR]: Failed to create blockstate for '" + name + "' at " + blockstatePath + ": " + e.getMessage());
        }
    }

    private static JsonElement generateBlockstateJson(String modId, String name, BddBlockstateTemplates blockstateTemplate, Map<String, String> properties) {
        JsonObject blockstateJson = blockstateTemplate.generateBlockstate(modId, name, properties);
        System.out.println("Generated JSON for '" + modId + ":blockstates/" + name + "':\n" + GSON.toJson(blockstateJson));
        return blockstateJson;
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
