/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import software.bluelib.BlueLibCommon;

//@EventBusSubscriber(value = Dist.CLIENT, modid = BlueLibConstants.MOD_ID)
public class BlueLibClient {

	public static void init(@NotNull ModContainer pModContainer) {
		pModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		BlueLibCommon.doClientRegistration();
	}
}
