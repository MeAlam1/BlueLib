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

public class DefaultedItemBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	public DefaultedItemBlueModel(ResourceLocation pAssetSubpath) {
		super(pAssetSubpath);
	}

	@Override
	protected String subtype() {
		return "item";
	}

	@Override
	public DefaultedItemBlueModel<T> withAltModel(ResourceLocation pAltPath) {
		return (DefaultedItemBlueModel<T>) super.withAltModel(pAltPath);
	}

	@Override
	public DefaultedItemBlueModel<T> withAltAnimations(ResourceLocation pAltPath) {
		return (DefaultedItemBlueModel<T>) super.withAltAnimations(pAltPath);
	}

	@Override
	public DefaultedItemBlueModel<T> withAltTexture(ResourceLocation pAltPath) {
		return (DefaultedItemBlueModel<T>) super.withAltTexture(pAltPath);
	}
}
