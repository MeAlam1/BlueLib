/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public class AllModsLoadedEvent extends Event implements IModBusEvent {

	@NotNull
	final List<ModMeta> modData;

	public AllModsLoadedEvent(@NotNull List<ModMeta> pModData) {
		super();
		this.modData = pModData;
	}

	@NotNull
	public List<ModMeta> getAllModsData() {
		return modData;
	}

	@Nullable
	public ModMeta getModMetaById(@NotNull String pModId) {
		return modData.stream()
				.filter(modMeta -> modMeta.modId().equals(pModId))
				.findFirst()
				.orElse(null);
	}
}
