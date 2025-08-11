/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderLayer;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class BoneFilterBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	@NotNull
	protected final TriConsumer<BoneCache, T, Float> checkAndApply;

	public BoneFilterBlueLayer(@NotNull BlueRenderer<T> pRenderer) {
		this(pRenderer, (bone, animatable, pPartialTick) -> {});
	}

	public BoneFilterBlueLayer(@NotNull BlueRenderer<T> pRenderer, @NotNull TriConsumer<BoneCache, T, Float> pCheckAndApply) {
		super(pRenderer);

		this.checkAndApply = pCheckAndApply;
	}

	protected void checkAndApply(@NotNull BoneCache pBone, @NotNull T pAnimatable, float pPartialTick) {
		this.checkAndApply.accept(pBone, pAnimatable, pPartialTick);
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		for (BoneCache bone : pContext.model().topLevelBones()) {
			checkChildBones(bone, pContext.animatable(), pContext.partialTick());
		}
	}

	private void checkChildBones(@NotNull BoneCache pParentBone, @NotNull T pAnimatable, float pPartialTick) {
		checkAndApply(pParentBone, pAnimatable, pPartialTick);

		for (BoneCache bone : pParentBone.getChildBones()) {
			checkChildBones(bone, pAnimatable, pPartialTick);
		}
	}
}
