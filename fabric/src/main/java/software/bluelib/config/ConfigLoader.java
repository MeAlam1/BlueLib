/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import java.nio.file.Path;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.config.ConfigBuilder;
import software.bluelib.config.bluelib.LoggerConfig;
import software.bluelib.config.bluelib.MarkdownConfig;

@SuppressWarnings({ "unused" })
public class ConfigLoader {

	@Nullable
	private static ConfigBuilder<MarkdownConfig> markdownConfigBuilder;
	@Nullable
	private static ConfigBuilder<LoggerConfig> loggerConfigBuilder;

	public static void createConfigs(@NotNull MinecraftServer pServer) {
		createMarkdownConfig(pServer);
		createLoggerConfig(pServer);
	}

	public static void createMarkdownConfig(@NotNull MinecraftServer pServer) {
		Path MARKDOWN_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/" + BlueLibConstants.MOD_ID + "-markdown.json");
		MarkdownConfig defaultMarkdownConfig = new MarkdownConfig();
		markdownConfigBuilder = new ConfigBuilder<>(MARKDOWN_CONFIG, MarkdownConfig.class, defaultMarkdownConfig);
		markdownConfigBuilder.createIfAbsent(defaultMarkdownConfig);
		markdownConfigBuilder.load();
		BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
	}

	public static void createLoggerConfig(@NotNull MinecraftServer pServer) {
		Path LOGGER_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/" + BlueLibConstants.MOD_ID + "-logger.json");
		LoggerConfig defaultLoggerConfig = new LoggerConfig();
		loggerConfigBuilder = new ConfigBuilder<>(LOGGER_CONFIG, LoggerConfig.class, defaultLoggerConfig);
		loggerConfigBuilder.createIfAbsent(defaultLoggerConfig);
		loggerConfigBuilder.load();
		BlueLibConfig.bakeLogger(loggerConfigBuilder.getConfig());
	}

	public static void reloadConfigs(@NotNull MinecraftServer pServer, @NotNull CloseableResourceManager pCloseableResourceManager, @NotNull Boolean pBoolean) {
		if (markdownConfigBuilder == null || loggerConfigBuilder == null) {
			createConfigs(pServer);
			return;
		}
		markdownConfigBuilder.load();
		BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
		BlueLibConfig.bakeLogger(loggerConfigBuilder.getConfig());
	}
}
