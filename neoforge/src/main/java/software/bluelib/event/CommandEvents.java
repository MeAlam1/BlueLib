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
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibConstants;
import software.bluelib.internal.registry.BlueCommandRegistry;

@EventBusSubscriber(modid = BlueLibConstants.MOD_ID)
public class CommandEvents {

	@SubscribeEvent
	public static void onRegisterCommands(@NotNull RegisterCommandsEvent pEvent) {
		BlueCommandRegistry.registerCommands(pEvent.getDispatcher());
	}
}
