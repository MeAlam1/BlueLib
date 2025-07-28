/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

import org.apache.logging.log4j.util.TriConsumer;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

public class BoneFilterBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	protected final TriConsumer<BoneCache, T, Float> checkAndApply;

	public BoneFilterBlueLayer(BlueRenderer<T> renderer) {
		this(renderer, (bone, animatable, pPartialTick) -> {});
	}

	public BoneFilterBlueLayer(BlueRenderer<T> renderer, TriConsumer<BoneCache, T, Float> checkAndApply) {
		super(renderer);

		this.checkAndApply = checkAndApply;
	}

	protected void checkAndApply(BoneCache bone, T animatable, float pPartialTick) {
		this.checkAndApply.accept(bone, animatable, pPartialTick);
	}

	@Override
	public void preRender(IRenderContext<T> pContext) {
		for (BoneCache bone : pContext.model().topLevelBones()) {
			checkChildBones(bone, pContext.animatable(), pContext.partialTick());
		}
	}

	private void checkChildBones(BoneCache parentBone, T animatable, float pPartialTick) {
		checkAndApply(parentBone, animatable, pPartialTick);

		for (BoneCache bone : parentBone.getChildBones()) {
			checkChildBones(bone, animatable, pPartialTick);
		}
	}
}
