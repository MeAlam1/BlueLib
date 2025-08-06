/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.renderer.base.BlueRenderer;

public abstract class DefaultedBlueModel<T extends BlueAnimatable> extends BlueModel<T> {

	private ResourceLocation modelPath;
	private ResourceLocation texturePath;
	private ResourceLocation animationsPath;

	public DefaultedBlueModel(ResourceLocation pAssetSubpath) {
		this.modelPath = buildFormattedModelPath(pAssetSubpath);
		this.texturePath = buildFormattedTexturePath(pAssetSubpath);
		this.animationsPath = buildFormattedAnimationPath(pAssetSubpath);
	}

	public DefaultedBlueModel<T> withAltModel(ResourceLocation pAltPath) {
		this.modelPath = buildFormattedModelPath(pAltPath);

		return this;
	}

	public DefaultedBlueModel<T> withAltAnimations(ResourceLocation pAltPath) {
		this.animationsPath = buildFormattedAnimationPath(pAltPath);

		return this;
	}

	public DefaultedBlueModel<T> withAltTexture(ResourceLocation pAltPath) {
		this.texturePath = buildFormattedTexturePath(pAltPath);

		return this;
	}

	public ResourceLocation buildFormattedModelPath(ResourceLocation pBasePath) {
		return pBasePath.withPath("geo/" + subtype() + "/" + pBasePath.getPath() + ".geo.json");
	}

	public ResourceLocation buildFormattedAnimationPath(ResourceLocation pBasePath) {
		return pBasePath.withPath("animations/" + subtype() + "/" + pBasePath.getPath() + ".animation.json");
	}

	public ResourceLocation buildFormattedTexturePath(ResourceLocation pBasePath) {
		return pBasePath.withPath("textures/" + subtype() + "/" + pBasePath.getPath() + ".png");
	}

	protected abstract String subtype();

	@Override
	public ResourceLocation getModelResource(T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		return modelPath;
	}

	@Override
	public ResourceLocation getTextureResource(T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		return texturePath;
	}

	@Override
	public ResourceLocation getAnimationResource(T pAnimatable) {
		return this.animationsPath;
	}
}
