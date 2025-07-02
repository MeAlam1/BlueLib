/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

public final class AllModsLoadedEvent {

	@NotNull
	public static final Event<AllModsLoadedEventListener> EVENT = EventFactory.createArrayBacked(AllModsLoadedEventListener.class,
			(listeners) -> (pModData) -> {
				for (AllModsLoadedEventListener listener : listeners) {
					listener.onAllModsLoaded(pModData);
				}
			});

	@FunctionalInterface
	public interface AllModsLoadedEventListener {

		void onAllModsLoaded(@NotNull List<ModMeta> pModData);
	}
}
