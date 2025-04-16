package software.bluelib.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import software.bluelib.config.bluelib.MarkdownConfig;

public final class ConfigHolder {

    // Type of Configs

    // Markdown
    public static final ModConfigSpec MARKDOWN_SPEC;
    public static final MarkdownConfig MARKDOWN;

    static {
        {
            Pair<MarkdownConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(MarkdownConfig::new);
            MARKDOWN = specPair.getLeft();
            MARKDOWN_SPEC = specPair.getRight();
        }
    }
}
