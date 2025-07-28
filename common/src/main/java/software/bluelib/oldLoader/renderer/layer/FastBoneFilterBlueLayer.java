/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

import java.util.List;
import java.util.function.Supplier;
import org.apache.logging.log4j.util.TriConsumer;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

public class FastBoneFilterBlueLayer<T extends BlueAnimatable> extends BoneFilterBlueLayer<T> {

	protected final Supplier<List<String>> boneSupplier;

	public FastBoneFilterBlueLayer(BlueRenderer<T> renderer) {
		this(renderer, List::of);
	}

	public FastBoneFilterBlueLayer(BlueRenderer<T> renderer, Supplier<List<String>> boneSupplier) {
		this(renderer, boneSupplier, (bone, animatable, pPartialTick) -> {});
	}

	public FastBoneFilterBlueLayer(BlueRenderer<T> renderer, Supplier<List<String>> boneSupplier, TriConsumer<BoneCache, T, Float> checkAndApply) {
		super(renderer, checkAndApply);

		this.boneSupplier = boneSupplier;
	}

	protected List<String> getAffectedBones() {
		return boneSupplier.get();
	}

	;

	@Override
	public void preRender(IRenderContext<T> pContext) {
		for (String boneName : getAffectedBones()) {
			this.renderer.getBlueModel().getBone(boneName).ifPresent(bone -> checkAndApply(bone, pContext.animatable(), pContext.partialTick()));
		}
	}
}
