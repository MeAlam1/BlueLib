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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoRenderer;

public abstract class GeoRenderLayer<T extends GeoAnimatable> {

    protected final GeoRenderer<T> renderer;

    public GeoRenderLayer(GeoRenderer<T> entityRendererIn) {
        this.renderer = entityRendererIn;
    }

    public GeoModel<T> getGeoModel() {
        return this.renderer.getGeoModel();
    }

    public BakedGeoModel getDefaultBakedModel(T animatable) {
        return getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable, getRenderer()));
    }

    public GeoRenderer<T> getRenderer() {
        return this.renderer;
    }

    protected ResourceLocation getTextureResource(T animatable) {
        return getRenderer().getTextureLocation(animatable);
    }

    public void preRender(PoseStack pPoseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType pRenderType,
            MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick,
            int pPackedLight, int pPackedOverlay) {}

    public void render(PoseStack pPoseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType pRenderType,
            MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, float pPartialTick,
            int pPackedLight, int pPackedOverlay) {}

    public void renderForBone(PoseStack pPoseStack, T animatable, GeoBone bone, RenderType pRenderType,
            MultiBufferSource pBufferSource, VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {}
}
