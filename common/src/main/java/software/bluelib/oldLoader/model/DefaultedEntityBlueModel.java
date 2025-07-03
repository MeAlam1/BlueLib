/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.model.data.EntityModelData;

public class DefaultedEntityBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	protected final boolean turnsHead;

	public DefaultedEntityBlueModel(ResourceLocation assetSubpath) {
		this(assetSubpath, false);
	}

	public DefaultedEntityBlueModel(ResourceLocation assetSubpath, boolean turnsHead) {
		super(assetSubpath);

		this.turnsHead = turnsHead;
	}

	@Override
	protected String subtype() {
		return "entity";
	}

	@Override
	public void setCustomAnimations(T pAnimatable, long pInstanceId, AnimationState<T> pAnimationState) {
		if (!this.turnsHead)
			return;

		BoneCache head = getAnimationProcessor().getBone("head");

		if (head != null) {
			EntityModelData entityData = pAnimationState.getData(DataTickets.ENTITY_MODEL_DATA);

			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}
	}

	@Override
	public DefaultedEntityBlueModel<T> withAltModel(ResourceLocation altPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltModel(altPath);
	}

	@Override
	public DefaultedEntityBlueModel<T> withAltAnimations(ResourceLocation altPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltAnimations(altPath);
	}

	@Override
	public DefaultedEntityBlueModel<T> withAltTexture(ResourceLocation altPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltTexture(altPath);
	}
}
