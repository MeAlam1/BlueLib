package software.bluelib.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import software.bluelib.BlueLibConstants;
import software.bluelib.config.BlueLibConfig;
import software.bluelib.config.ConfigHolder;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent.Loading event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigHolder.MARKDOWN_SPEC) {
            BlueLibConfig.bakeMarkdown(config);
        }
    }
}
