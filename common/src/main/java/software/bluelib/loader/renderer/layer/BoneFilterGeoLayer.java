/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.renderer.GeoRenderer;

public class BoneFilterGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    protected final TriConsumer<GeoBone, T, Float> checkAndApply;

    public BoneFilterGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, (bone, animatable, pPartialTick) -> {});
    }

    public BoneFilterGeoLayer(GeoRenderer<T> renderer, TriConsumer<GeoBone, T, Float> checkAndApply) {
        super(renderer);

        this.checkAndApply = checkAndApply;
    }

    protected void checkAndApply(GeoBone bone, T animatable, float pPartialTick) {
        this.checkAndApply.accept(bone, animatable, pPartialTick);
    }

    @Override
    public void preRender(PoseStack pPoseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        for (GeoBone bone : bakedModel.topLevelBones()) {
            checkChildBones(bone, animatable, pPartialTick);
        }
    }

    private void checkChildBones(GeoBone parentBone, T animatable, float pPartialTick) {
        checkAndApply(parentBone, animatable, pPartialTick);

        for (GeoBone bone : parentBone.getChildBones()) {
            checkChildBones(bone, animatable, pPartialTick);
        }
    }
}
