/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import software.bluelib.loader.animatable.GeoAnimatable;

public abstract class DefaultedGeoModel<T extends GeoAnimatable> extends GeoModel<T> {

    private ResourceLocation modelPath;
    private ResourceLocation texturePath;
    private ResourceLocation animationsPath;

    public DefaultedGeoModel(ResourceLocation assetSubpath) {
        this.modelPath = buildFormattedModelPath(assetSubpath);
        this.texturePath = buildFormattedTexturePath(assetSubpath);
        this.animationsPath = buildFormattedAnimationPath(assetSubpath);
    }

    public DefaultedGeoModel<T> withAltModel(ResourceLocation altPath) {
        this.modelPath = buildFormattedModelPath(altPath);

        return this;
    }

    public DefaultedGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        this.animationsPath = buildFormattedAnimationPath(altPath);

        return this;
    }

    public DefaultedGeoModel<T> withAltTexture(ResourceLocation altPath) {
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
    public ResourceLocation getModelResource(T animatable) {
        return this.modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return this.texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationsPath;
    }
}
