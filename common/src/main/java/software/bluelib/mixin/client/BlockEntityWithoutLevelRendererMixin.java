/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.loader.renderer.client.BlueRenderProvider;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

	@Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
	public void BlueLib$renderBlueLibItem(@NotNull ItemStack pStack, @NotNull ItemDisplayContext pDisplayContext, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, @NotNull CallbackInfo pCi) {
		final BlockEntityWithoutLevelRenderer BlueLibRenderer = BlueRenderProvider.of(pStack).getBlueItemRenderer();

		if (BlueLibRenderer != null) {
			BlueLibRenderer.renderByItem(pStack, pDisplayContext, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

			pCi.cancel();
		}
	}
}
