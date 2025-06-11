package software.bluelib.api.registry.datagen.blockstates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.datagen.DataGenUtils;

public class BlockstateGenerator extends DataGenUtils {
    public static void generateBlockstate(String modId, String name, BlockstateTemplates blockstateTemplate) {
        generateBlockstate(modId, name, blockstateTemplate, Collections.emptyMap());
    }

    public static void generateBlockstate(String modId, String name, BlockstateTemplates blockstateTemplate, Map<String, String> properties) {
        Path blockstatePath = Path.of(BlueLibConstants.PlatformHelper.PLATFORM.getAssetsDir(true) + "/blockstates/" + name + ".json");

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

    private static JsonElement generateBlockstateJson(String modId, String name, BlockstateTemplates blockstateTemplate, Map<String, String> properties) {
        JsonObject blockstateJson = blockstateTemplate.generateBlockstate(modId, name, properties);
        System.out.println("Generated JSON for '" + modId + ":blockstates/" + name + "':\n" + GSON.toJson(blockstateJson));
        return blockstateJson;
    }
}
