/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.model.data.EntityModelData;

public class DefaultedEntityGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    protected final boolean turnsHead;

    public DefaultedEntityGeoModel(ResourceLocation assetSubpath) {
        this(assetSubpath, false);
    }

    public DefaultedEntityGeoModel(ResourceLocation assetSubpath, boolean turnsHead) {
        super(assetSubpath);

        this.turnsHead = turnsHead;
    }

    @Override
    protected String subtype() {
        return "entity";
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (!this.turnsHead)
            return;

        BoneCache head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

    @Override
    public DefaultedEntityGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltModel(altPath);
    }

    @Override
    public DefaultedEntityGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltAnimations(altPath);
    }

    @Override
    public DefaultedEntityGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltTexture(altPath);
    }
}
