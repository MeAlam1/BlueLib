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

public class DefaultedItemGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    public DefaultedItemGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "item";
    }

    @Override
    public DefaultedItemGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltModel(altPath);
    }

    @Override
    public DefaultedItemGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltAnimations(altPath);
    }

    @Override
    public DefaultedItemGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltTexture(altPath);
    }
}
