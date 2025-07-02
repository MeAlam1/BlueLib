/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import software.bluelib.config.BlueLibConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigBuilder<T extends BlueLibConfig> {

	@NotNull
	private final Path configPath;
	@NotNull
	private final Class<T> configClass;
	@NotNull
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@NotNull
	private T config;

	public ConfigBuilder(@NotNull Path pConfigPath, @NotNull Class<T> pConfigClass, @NotNull T pDefaultConfig) {
		this.configPath = pConfigPath;
		this.configClass = pConfigClass;
		this.config = pDefaultConfig;
	}

	public void createIfAbsent(@NotNull T pDefaultConfig) {
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

			if (config == null) {
				config = loadedConfig;
			} else {
				for (var field : configClass.getDeclaredFields()) {
					field.setAccessible(true);
					Object loadedValue = field.get(loadedConfig);
					if (loadedValue != null) {
						field.set(config, loadedValue);
					}
				}
			}
		} catch (IOException | IllegalAccessException pException) {
			throw new RuntimeException("Failed to load config", pException);
		}
	}

	public void save() {
		try {
			Files.createDirectories(configPath.getParent());
			String json = gson.toJson(config);
			Files.writeString(configPath, json);
		} catch (IOException pIoException) {
			throw new RuntimeException("Failed to save config", pIoException);
		}
	}

	@NotNull
	public T getConfig() {
		return config;
	}
}
