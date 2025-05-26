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

public class DefaultedBlockGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    public DefaultedBlockGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "block";
    }

    @Override
    public DefaultedBlockGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltModel(altPath);
    }

    @Override
    public DefaultedBlockGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltAnimations(altPath);
    }

    @Override
    public DefaultedBlockGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltTexture(altPath);
    }
}
