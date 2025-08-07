/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.context;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.ModelCache;

public sealed interface IRenderContext<T extends BlueAnimatable>
		permits BaseRenderContext, FullRenderContext {

	PoseStack poseStack();

	T animatable();

	ModelCache model();

	MultiBufferSource bufferSource();

	boolean isReRender();

	float partialTick();

	int packedLight();

	int packedOverlay();

	int color();
}
