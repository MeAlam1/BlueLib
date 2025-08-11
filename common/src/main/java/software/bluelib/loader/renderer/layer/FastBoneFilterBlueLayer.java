/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import java.util.List;
import java.util.function.Supplier;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.IRenderContext;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class FastBoneFilterBlueLayer<T extends BlueAnimatable> extends BoneFilterBlueLayer<T> {

	@NotNull
	protected final Supplier<List<String>> boneSupplier;

	public FastBoneFilterBlueLayer(@NotNull BlueRenderer<T> pRenderer) {
		this(pRenderer, List::of);
	}

	public FastBoneFilterBlueLayer(@NotNull BlueRenderer<T> pRenderer, @NotNull Supplier<List<String>> pBoneSupplier) {
		this(pRenderer, pBoneSupplier, (bone, animatable, pPartialTick) -> {});
	}

	public FastBoneFilterBlueLayer(@NotNull BlueRenderer<T> pRenderer, @NotNull Supplier<List<String>> pBoneSupplier, @NotNull TriConsumer<BoneCache, T, Float> pCheckAndApply) {
		super(pRenderer, pCheckAndApply);

		this.boneSupplier = pBoneSupplier;
	}

	protected List<String> getAffectedBones() {
		return boneSupplier.get();
	}

	@Override
	public void preRender(@NotNull IRenderContext<T> pContext) {
		for (String boneName : getAffectedBones()) {
			this.renderer.getBlueModel().getBone(boneName).ifPresent(bone -> checkAndApply(bone, pContext.animatable(), pContext.partialTick()));
		}
	}
}
