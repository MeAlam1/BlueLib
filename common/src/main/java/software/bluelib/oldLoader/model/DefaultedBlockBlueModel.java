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

public class DefaultedBlockBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	public DefaultedBlockBlueModel(ResourceLocation assetSubpath) {
		super(assetSubpath);
	}

	@Override
	protected String subtype() {
		return "block";
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltModel(ResourceLocation altPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltModel(altPath);
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltAnimations(ResourceLocation altPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltAnimations(altPath);
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltTexture(ResourceLocation altPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltTexture(altPath);
	}
}
