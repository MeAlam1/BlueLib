/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.renderer.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.PlayerUtils;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.cache.texture.AutoGlowingTexture;
import software.bluelib.loader.renderer.base.BlueRenderer;
import software.bluelib.loader.renderer.context.FullRenderContext;
import software.bluelib.loader.renderer.context.IRenderContext;

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
	public void render(IRenderContext<T> pContext) {
		if (pContext instanceof FullRenderContext<T> full) {
			if (full.renderType() != null) {
				getRenderer().reRender(full);
			}
		} else if (pContext instanceof IRenderContext<T> base) {
			RenderType renderType = getRenderType(base.animatable(), base.bufferSource());
			FullRenderContext<T> full = new FullRenderContext<>(
					pContext.poseStack(),
					pContext.animatable(),
					pContext.model(),
					renderType,
					pContext.bufferSource(),
					null,
					pContext.isReRender(),
					pContext.partialTick(),
					pContext.packedLight(),
					pContext.packedOverlay(),
					pContext.color());
			render(full);
		}
	}
}
