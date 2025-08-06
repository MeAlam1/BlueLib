/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.loader.animatable.BlueAnimatable;

public class DefaultedBlockBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	public DefaultedBlockBlueModel(ResourceLocation pAssetSubpath) {
		super(pAssetSubpath);
	}

	@Override
	protected String subtype() {
		return "block";
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltModel(ResourceLocation pAltPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltModel(pAltPath);
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltAnimations(ResourceLocation pAltPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltAnimations(pAltPath);
	}

	@Override
	public DefaultedBlockBlueModel<T> withAltTexture(ResourceLocation pAltPath) {
		return (DefaultedBlockBlueModel<T>) super.withAltTexture(pAltPath);
	}
}
