package software.bluelib.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import software.bluelib.config.BlueLibConfig;

public class ConfigBuilder<T extends BlueLibConfig> {

    private final Path configPath;
    private final Class<T> configClass;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private T config;

    public ConfigBuilder(Path pConfigPath, Class<T> pConfigClass) {
        this.configPath = pConfigPath;
        this.configClass = pConfigClass;
    }

    public void createIfAbsent(T pDefaultConfig) {
        if (!Files.exists(configPath)) {
            config = pDefaultConfig;
            save();
        }
    }

    public void load() {
        try {
            if (!Files.exists(configPath)) {
                throw new RuntimeException("Config file does not exist: " + configPath);
            }
            String json = Files.readString(configPath);
            T loadedConfig = gson.fromJson(json, configClass);

            for (var field : configClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object loadedValue = field.get(loadedConfig);
                if (loadedValue != null) {
                    field.set(config, loadedValue);
                }
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            String json = gson.toJson(config);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    public T getConfig() {
        return config;
    }
}
