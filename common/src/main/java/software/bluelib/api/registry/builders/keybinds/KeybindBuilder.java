/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.builders.keybinds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import software.bluelib.BlueLibConstants;

public class KeybindBuilder {

	public static final List<KeybindBuilder> REGISTERED_BUILDERS = new ArrayList<>();
	protected final String modId;
	private final String name;
	private final int keyCode;
	private String category;
	private Supplier<KeyMapping> keyMappingSupplier;

	private KeybindBuilder(String name, int keyCode, String pModId) {
		this.name = name;
		this.keyCode = keyCode;
		this.modId = pModId;
		this.category = "key.categories." + pModId;
	}

	public static KeybindBuilder keybind(String name, int keyCode, String pModId) {
		return new KeybindBuilder(name, keyCode, pModId);
	}

	public KeybindBuilder category(String category) {
		this.category = category;
		return this;
	}

	public Supplier<KeyMapping> register() {
		keyMappingSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerKeybind(name, () -> new KeyMapping(
				"key." + modId + "." + name,
				keyCode,
				category));

		REGISTERED_BUILDERS.add(this);
		return keyMappingSupplier;
	}

	public String getName() {
		return name;
	}

	public Supplier<KeyMapping> getKeyMapping() {
		return keyMappingSupplier;
	}
}
