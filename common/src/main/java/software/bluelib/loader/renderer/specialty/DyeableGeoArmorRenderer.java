/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer.specialty;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Collection;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.GeoItem;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.GeoArmorRenderer;
import software.bluelib.loader.util.Color;

public abstract class DyeableGeoArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    protected final Set<GeoBone> dyeableBones = new ObjectArraySet<>();
    protected BakedGeoModel lastModel = null;

    public DyeableGeoArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void preRender(PoseStack pPoseStack, T pAnimatable, BakedGeoModel model, @Nullable MultiBufferSource pBufferSource, @Nullable VertexConsumer buffer, boolean pIsReRender, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        super.preRender(pPoseStack, pAnimatable, model, pBufferSource, buffer, pIsReRender, pPartialTick, pPackedLight, pPackedOverlay, colour);

        if (!pIsReRender)
            checkBoneDyeCache(pAnimatable, model, pPartialTick, pPackedLight, pPackedOverlay, colour);
    }

    @Override
    public void renderCubesOfBone(PoseStack pPoseStack, GeoBone bone, VertexConsumer buffer, int pPackedLight, int pPackedOverlay, int colour) {
        if (this.dyeableBones.contains(bone)) {
            final Color color = getColorForBone(bone);

            colour = FastColor.ARGB32.multiply(colour, color.argbInt());
        }

        super.renderCubesOfBone(pPoseStack, bone, buffer, pPackedLight, pPackedOverlay, colour);
    }

    protected abstract boolean isBoneDyeable(GeoBone bone);

    @NotNull
    protected abstract Color getColorForBone(GeoBone bone);

    protected void checkBoneDyeCache(T animatable, BakedGeoModel model, float pPartialTick, int pPackedLight, int pPackedOverlay, int colour) {
        if (model != this.lastModel) {
            this.dyeableBones.clear();
            this.lastModel = model;
            collectDyeableBones(model.topLevelBones());
        }
    }

    protected void collectDyeableBones(Collection<GeoBone> bones) {
        for (GeoBone bone : bones) {
            if (isBoneDyeable(bone))
                this.dyeableBones.add(bone);

            collectDyeableBones(bone.getChildBones());
        }
    }
}
