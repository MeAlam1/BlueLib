/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

public final class ModLoadedEvent {

	@NotNull
	public static final Event<ModLoadedEventListener> EVENT = EventFactory.createArrayBacked(ModLoadedEventListener.class,
			(listeners) -> (pModData) -> {
				for (ModLoadedEventListener listener : listeners) {
					listener.onModLoaded(pModData);
				}
			});

	@FunctionalInterface
	public interface ModLoadedEventListener {

		void onModLoaded(@NotNull ModMeta pModData);
	}
}
