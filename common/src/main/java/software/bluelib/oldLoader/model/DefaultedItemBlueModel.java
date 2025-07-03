/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.model;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.oldLoader.animatable.BlueAnimatable;

public class DefaultedItemBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	public DefaultedItemBlueModel(ResourceLocation assetSubpath) {
		super(assetSubpath);
	}

	@Override
	protected String subtype() {
		return "item";
	}

	@Override
	public DefaultedItemBlueModel<T> withAltModel(ResourceLocation altPath) {
		return (DefaultedItemBlueModel<T>) super.withAltModel(altPath);
	}

	@Override
	public DefaultedItemBlueModel<T> withAltAnimations(ResourceLocation altPath) {
		return (DefaultedItemBlueModel<T>) super.withAltAnimations(altPath);
	}

	@Override
	public DefaultedItemBlueModel<T> withAltTexture(ResourceLocation altPath) {
		return (DefaultedItemBlueModel<T>) super.withAltTexture(altPath);
	}
}
