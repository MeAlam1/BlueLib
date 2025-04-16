package software.bluelib.config;

import java.nio.file.Path;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.storage.LevelResource;
import software.bluelib.api.config.ConfigBuilder;
import software.bluelib.config.bluelib.MarkdownConfig;

public class ConfigLoader {

    private static ConfigBuilder<MarkdownConfig> markdownConfigBuilder;

    public static void createConfigs(MinecraftServer pServer) {
        Path MARKDOWN_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/bluelib-markdown.json");
        markdownConfigBuilder = new ConfigBuilder<>(MARKDOWN_CONFIG, MarkdownConfig.class);
        MarkdownConfig defaultMarkdownConfig = new MarkdownConfig();
        markdownConfigBuilder.createIfAbsent(defaultMarkdownConfig);
        markdownConfigBuilder.load();
        BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
    }

    public static void reloadConfigs(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
        markdownConfigBuilder.load();
        BlueLibConfig.bakeMarkdown(markdownConfigBuilder.getConfig());
    }
}
