/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
    public static void onModConfigEvent(final ModConfigEvent.Reloading event) {
        final ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigHolder.MARKDOWN_SPEC) {
            BlueLibConfig.bakeMarkdown(config);
        }
        if (config.getSpec() == ConfigHolder.LOGGER_SPEC) {
            BlueLibConfig.bakeLogger(config);
        }
    }
}
