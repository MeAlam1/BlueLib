/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.oldLoader.loading.math.MathParser;
import software.bluelib.oldLoader.loading.math.MoLangQueries;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow
	private int renderedEntities;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	public void BlueLib$captureRenderedEntities(@NotNull DeltaTracker pDeltaTracker, boolean pRenderBlockOutline, @NotNull Camera pCamera, @NotNull GameRenderer pGameRenderer, @NotNull LightTexture pLightTexture, @NotNull Matrix4f pFrustumMatrix, @NotNull Matrix4f pProjectionMatrix, @NotNull CallbackInfo pCi) {
		final int renderedEntityCount = this.renderedEntities;

		MathParser.setVariable(MoLangQueries.ACTOR_COUNT, () -> renderedEntityCount);
	}
}
