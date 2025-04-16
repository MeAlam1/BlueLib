package software.bluelib.config;

import java.nio.file.Path;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.storage.LevelResource;
import software.bluelib.config.bluelib.MarkdownConfig;

public class ConfigLoader {

    private static ConfigHolder<software.bluelib.config.bluelib.MarkdownConfig> markdownConfigHolder;

    public static void createConfigs(MinecraftServer pServer) {
        Path MARKDOWN_CONFIG = pServer.getWorldPath(LevelResource.ROOT).resolve("serverConfig/bluelib-markdown.json");
        markdownConfigHolder = new ConfigHolder<>(MARKDOWN_CONFIG, MarkdownConfig.class);
        MarkdownConfig defaultMarkdownConfig = new MarkdownConfig();
        markdownConfigHolder.createIfAbsent(defaultMarkdownConfig);
        markdownConfigHolder.load();
        BlueLibConfig.bakeMarkdown(markdownConfigHolder.getConfig());
    }

    public static void reloadConfigs(MinecraftServer pServer, CloseableResourceManager pCloseableResourceManager, boolean pBoolean) {
        markdownConfigHolder.load();
        BlueLibConfig.bakeMarkdown(markdownConfigHolder.getConfig());
    }
}
