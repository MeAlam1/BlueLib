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
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.renderer.GeoRenderer;

public class FastBoneFilterGeoLayer<T extends GeoAnimatable> extends BoneFilterGeoLayer<T> {

    protected final Supplier<List<String>> boneSupplier;

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, List::of);
    }

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer, Supplier<List<String>> boneSupplier) {
        this(renderer, boneSupplier, (bone, animatable, pPartialTick) -> {});
    }

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer, Supplier<List<String>> boneSupplier, TriConsumer<GeoBone, T, Float> checkAndApply) {
        super(renderer, checkAndApply);

        this.boneSupplier = boneSupplier;
    }

    protected List<String> getAffectedBones() {
        return boneSupplier.get();
    };

    @Override
    public void preRender(PoseStack pPoseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType pRenderType, MultiBufferSource pBufferSource,
            @Nullable VertexConsumer buffer, float pPartialTick, int pPackedLight, int pPackedOverlay) {
        for (String boneName : getAffectedBones()) {
            this.renderer.getGeoModel().getBone(boneName).ifPresent(bone -> checkAndApply(bone, animatable, pPartialTick));
        }
    }
}
