/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.base;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.context.IRenderContext;

public abstract class BlueRenderLayer<T extends BlueAnimatable> {

	protected final BlueRenderer<T> renderer;

	public BlueRenderLayer(BlueRenderer<T> pEntityRenderer) {
		this.renderer = pEntityRenderer;
	}

	public BlueModel<T> getBlueModel() {
		return this.renderer.getBlueModel();
	}

	public ModelCache getDefaultBakedModel(T pAnimatable) {
		return getBlueModel().getBakedModel(getBlueModel().getModelResource(pAnimatable, getRenderer()));
	}

	public BlueRenderer<T> getRenderer() {
		return this.renderer;
	}

	protected ResourceLocation getTextureResource(T pAnimatable) {
		return getRenderer().getTextureLocation(pAnimatable);
	}

	public void preRender(IRenderContext<T> pContext) {}

	public void render(IRenderContext<T> pContext) {}

	public void renderForBone(BoneCache pBone, IRenderContext<T> pContext) {}
}
