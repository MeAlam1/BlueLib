/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.geckolib.constant.DataTickets;
import software.bluelib.loader.geckolib.data.EntityModelData;
import software.bluelib.oldLoader.animation.AnimationState;

public class DefaultedEntityBlueModel<T extends BlueAnimatable> extends DefaultedBlueModel<T> {

	protected final boolean turnsHead;

	public DefaultedEntityBlueModel(ResourceLocation pAssetSubpath) {
		this(pAssetSubpath, false);
	}

	public DefaultedEntityBlueModel(ResourceLocation pAssetSubpath, boolean pTurnsHead) {
		super(pAssetSubpath);

		this.turnsHead = pTurnsHead;
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
	public DefaultedEntityBlueModel<T> withAltModel(ResourceLocation pAltPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltModel(pAltPath);
	}

	@Override
	public DefaultedEntityBlueModel<T> withAltAnimations(ResourceLocation pAltPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltAnimations(pAltPath);
	}

	@Override
	public DefaultedEntityBlueModel<T> withAltTexture(ResourceLocation pAltPath) {
		return (DefaultedEntityBlueModel<T>) super.withAltTexture(pAltPath);
	}
}
