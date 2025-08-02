/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import org.apache.logging.log4j.util.TriConsumer;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

public class BoneFilterBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	protected final TriConsumer<BoneCache, T, Float> checkAndApply;

	public BoneFilterBlueLayer(BlueRenderer<T> pRenderer) {
		this(pRenderer, (bone, animatable, pPartialTick) -> {});
	}

	public BoneFilterBlueLayer(BlueRenderer<T> pRenderer, TriConsumer<BoneCache, T, Float> pCheckAndApply) {
		super(pRenderer);

		this.checkAndApply = pCheckAndApply;
	}

	protected void checkAndApply(BoneCache pBone, T pAnimatable, float pPartialTick) {
		this.checkAndApply.accept(pBone, pAnimatable, pPartialTick);
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		for (BoneCache bone : pContext.model().topLevelBones()) {
			checkChildBones(bone, pContext.animatable(), pContext.partialTick());
		}
	}

	private void checkChildBones(BoneCache pParentBone, T pAnimatable, float pPartialTick) {
		checkAndApply(pParentBone, pAnimatable, pPartialTick);

		for (BoneCache bone : pParentBone.getChildBones()) {
			checkChildBones(bone, pAnimatable, pPartialTick);
		}
	}
}
