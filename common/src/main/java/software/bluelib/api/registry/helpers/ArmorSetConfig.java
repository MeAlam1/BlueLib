/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.helpers;

import java.util.function.Consumer;
import net.minecraft.world.item.Item;

public class ArmorSetConfig {

	public Consumer<Item.Properties> helmetProperties;
	public Consumer<Item.Properties> chestplateProperties;
	public Consumer<Item.Properties> leggingsProperties;
	public Consumer<Item.Properties> bootsProperties;

	public ArmorSetConfig helmet(Consumer<Item.Properties> properties) {
		this.helmetProperties = properties;
		return this;
	}

	public ArmorSetConfig chestplate(Consumer<Item.Properties> properties) {
		this.chestplateProperties = properties;
		return this;
	}

	public ArmorSetConfig leggings(Consumer<Item.Properties> properties) {
		this.leggingsProperties = properties;
		return this;
	}

	public ArmorSetConfig boots(Consumer<Item.Properties> properties) {
		this.bootsProperties = properties;
		return this;
	}
}
