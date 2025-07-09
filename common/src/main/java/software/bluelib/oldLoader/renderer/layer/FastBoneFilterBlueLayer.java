/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.renderer.BlueRenderer;

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
	};

	@Override
	public void preRender(PoseStack pPoseStack, T animatable, ModelCache bakedModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource,
			@Nullable VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		for (String boneName : getAffectedBones()) {
			this.renderer.getBlueModel().getBone(boneName).ifPresent(bone -> checkAndApply(bone, animatable, pPartialTick));
		}
	}
}
