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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.cache.texture.AutoGlowingTexture;
import software.bluelib.loader.renderer.base.BlueRenderer;

public class AutoGlowingBlueLayer<T extends BlueAnimatable> extends BlueRenderLayer<T> {

	public AutoGlowingBlueLayer(BlueRenderer<T> renderer) {
		super(renderer);
	}

	@Nullable
	protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource pBufferSource) {
		if (!(animatable instanceof Entity entity))
			return AutoGlowingTexture.getRenderType(getTextureResource(animatable));

		boolean invisible = entity.isInvisible();
		ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));

		if (invisible && !entity.isInvisibleTo(PlayerUtils.getClientPlayer()))
			return RenderType.itemEntityTranslucentCull(texture);

		if (Minecraft.getInstance().shouldEntityAppearGlowing(entity)) {
			if (invisible)
				return RenderType.outline(texture);

			return AutoGlowingTexture.getOutlineRenderType(getTextureResource(animatable));
		}

		return invisible ? null : AutoGlowingTexture.getRenderType(getTextureResource(animatable));
	}

	@Override
	public void render(PoseStack pPoseStack, T animatable, ModelCache bakedModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
		pRenderType = getRenderType(animatable, pBufferSource);

		if (pRenderType != null) {
			getRenderer().reRender(bakedModel, pPoseStack, pBufferSource, animatable, pRenderType,
					pBufferSource.getBuffer(pRenderType), pPartialTick, LightTexture.FULL_SKY, pPackedOverlay,
					getRenderer().getRenderColor(animatable, pPartialTick, pPackedLight).argbInt());
		}
	}
}
