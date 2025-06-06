/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.storage.LevelResource;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.config.ConfigBuilder;
import software.bluelib.config.bluelib.LoggerConfig;
import software.bluelib.config.bluelib.MarkdownConfig;

import java.nio.file.Path;

public class ConfigLoader {

	private static ConfigBuilder<MarkdownConfig> markdownConfigBuilder;
	private static ConfigBuilder<LoggerConfig> loggerConfigBuilder;

	public static void createConfigs(MinecraftServer pServer) {
		createMarkdownConfig(pServer);
		createLoggerConfig(pServer);
	}

	public static void createMarkdownConfig(MinecraftServer pServer) {
		Path MARKDOWN_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/" + BlueLibConstants.MOD_ID + "-markdown.json");
		markdownConfigBuilder = new ConfigBuilder<>(MARKDOWN_CONFIG, MarkdownConfig.class);
		MarkdownConfig defaultMarkdownConfig = new MarkdownConfig();
		markdownConfigBuilder.createIfAbsent(defaultMarkdownConfig);
		markdownConfigBuilder.load();
		BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
	}

	public static void createLoggerConfig(MinecraftServer pServer) {
		Path LOGGER_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/" + BlueLibConstants.MOD_ID + "-logger.json");
		loggerConfigBuilder = new ConfigBuilder<>(LOGGER_CONFIG, LoggerConfig.class);
		LoggerConfig defaultLoggerConfig = new LoggerConfig();
		loggerConfigBuilder.createIfAbsent(defaultLoggerConfig);
		loggerConfigBuilder.load();
		BlueLibConfig.bakeLogger(loggerConfigBuilder.getConfig());
	}

	public static void reloadConfigs(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
		markdownConfigBuilder.load();
		BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
		BlueLibConfig.bakeLogger(loggerConfigBuilder.getConfig());
	}
}
