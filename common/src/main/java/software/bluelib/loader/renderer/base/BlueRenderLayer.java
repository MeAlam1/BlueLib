/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.base;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.model.BlueModel;
import software.bluelib.loader.renderer.context.IRenderContext;

@SuppressWarnings({ "unused" })
public abstract class BlueRenderLayer<T extends BlueAnimatable> {

	@NotNull
	protected final BlueRenderer<T> renderer;

	public BlueRenderLayer(@NotNull BlueRenderer<T> pEntityRenderer) {
		this.renderer = pEntityRenderer;
	}

	@NotNull
	public BlueModel<T> getBlueModel() {
		return this.renderer.getBlueModel();
	}

	@NotNull
	public ModelCache getDefaultBakedModel(@NotNull T pAnimatable) {
		return getBlueModel().getBakedModel(getBlueModel().getModelResource(pAnimatable, getRenderer()));
	}

	@NotNull
	public BlueRenderer<T> getRenderer() {
		return this.renderer;
	}

	@NotNull
	protected ResourceLocation getTextureResource(@NotNull T pAnimatable) {
		return getRenderer().getTextureLocation(pAnimatable);
	}

	public void preRender(@NotNull IRenderContext<T> pContext) {}

	public void render(@NotNull IRenderContext<T> pContext) {}

	public void renderForBone(@NotNull BoneCache pBone, @NotNull IRenderContext<T> pContext) {}
}
