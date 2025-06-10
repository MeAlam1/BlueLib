/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.loader.animatable.BlueAnimatable;

public abstract class DefaultedBlueModel<T extends BlueAnimatable> extends BlueModel<T> {

    private ResourceLocation modelPath;
    private ResourceLocation texturePath;
    private ResourceLocation animationsPath;

    public DefaultedBlueModel(ResourceLocation assetSubpath) {
        this.modelPath = buildFormattedModelPath(assetSubpath);
        this.texturePath = buildFormattedTexturePath(assetSubpath);
        this.animationsPath = buildFormattedAnimationPath(assetSubpath);
    }

    public DefaultedBlueModel<T> withAltModel(ResourceLocation altPath) {
        this.modelPath = buildFormattedModelPath(altPath);

        return this;
    }

    public DefaultedBlueModel<T> withAltAnimations(ResourceLocation altPath) {
        this.animationsPath = buildFormattedAnimationPath(altPath);

        return this;
    }

    public DefaultedBlueModel<T> withAltTexture(ResourceLocation altPath) {
        this.texturePath = buildFormattedTexturePath(altPath);

        return this;
    }

    public ResourceLocation buildFormattedModelPath(ResourceLocation basePath) {
        return basePath.withPath("geo/" + subtype() + "/" + basePath.getPath() + ".geo.json");
    }

    public ResourceLocation buildFormattedAnimationPath(ResourceLocation basePath) {
        return basePath.withPath("animations/" + subtype() + "/" + basePath.getPath() + ".animation.json");
    }

    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath) {
        return basePath.withPath("textures/" + subtype() + "/" + basePath.getPath() + ".png");
    }

    protected abstract String subtype();

    @Override
    public ResourceLocation getModelResource(T pAnimatable) {
        return this.modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(T pAnimatable) {
        return this.texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(T pAnimatable) {
        return this.animationsPath;
    }
}
