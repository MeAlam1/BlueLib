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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.model.BlueModel;
import software.bluelib.oldLoader.renderer.BlueRenderer;

public abstract class BlueRenderLayer<T extends BlueAnimatable> {

	protected final BlueRenderer<T> renderer;

	public BlueRenderLayer(BlueRenderer<T> entityRendererIn) {
		this.renderer = entityRendererIn;
	}

	public BlueModel<T> getBlueModel() {
		return this.renderer.getBlueModel();
	}

	public ModelCache getDefaultBakedModel(T animatable) {
		return getBlueModel().getBakedModel(getBlueModel().getModelResource(animatable, getRenderer()));
	}

	public BlueRenderer<T> getRenderer() {
		return this.renderer;
	}

	protected ResourceLocation getTextureResource(T animatable) {
		return getRenderer().getTextureLocation(animatable);
	}

	public void preRender(PoseStack pPoseStack, T animatable, ModelCache bakedModel, @Nullable RenderType pRenderType,
			MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick,
			int pPackedLight, int pPackedOverlay) {}

	public void render(PoseStack pPoseStack, T animatable, ModelCache bakedModel, @Nullable RenderType pRenderType,
			MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick,
			int pPackedLight, int pPackedOverlay) {}

	public void renderForBone(PoseStack pPoseStack, T animatable, BoneCache bone, RenderType pRenderType,
			MultiBufferSource pBufferSource, VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {}
}
