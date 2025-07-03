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
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.config.BlueLibConfig;
import software.bluelib.config.ConfigHolder;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class CommonProxy {

	@SubscribeEvent
	public static void onModConfigEvent(@NotNull final ModConfigEvent.Reloading pEvent) {
		final ModConfig config = pEvent.getConfig();
		if (config.getSpec() == ConfigHolder.MARKDOWN_SPEC) {
			BlueLibConfig.bakeMarkdown(config);
		}
		if (config.getSpec() == ConfigHolder.LOGGER_SPEC) {
			BlueLibConfig.bakeLogger(config);
		}
	}
}
