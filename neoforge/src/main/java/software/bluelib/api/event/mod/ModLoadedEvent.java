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

import java.util.Optional;

@SuppressWarnings("unused")
public class ModLoadedEvent extends Event implements IModBusEvent {

	@NotNull
	final ModMeta modData;

	public ModLoadedEvent(@NotNull ModMeta pModData) {
		super();
		this.modData = pModData;
	}

	@NotNull
	public ModMeta getModData() {
		return modData;
	}

	@NotNull
	public String getModId() {
		return modData.modId();
	}

	@NotNull
	public String getDisplayName() {
		return modData.displayName();
	}

	@NotNull
	public String getVersion() {
		return modData.version();
	}

	@NotNull
	public String getDescription() {
		return modData.description();
	}

	@Nullable
	public Optional<String> getLogoFile() {
		return modData.logoFile();
	}
}
