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
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.GeoItem;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.cache.object.GeoCube;
import software.bluelib.loader.renderer.GeoArmorRenderer;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.util.Color;
import software.bluelib.loader.util.RenderUtil;

public class ItemArmorGeoLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    protected static final HumanoidModel<LivingEntity> INNER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    protected static final HumanoidModel<LivingEntity> OUTER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

    @Nullable
    protected ItemStack mainHandStack;
    @Nullable
    protected ItemStack offhandStack;
    @Nullable
    protected ItemStack helmetStack;
    @Nullable
    protected ItemStack chestplateStack;
    @Nullable
    protected ItemStack leggingsStack;
    @Nullable
    protected ItemStack bootsStack;

    public ItemArmorGeoLayer(GeoRenderer<T> geoRenderer) {
        super(geoRenderer);
    }

    @NotNull
    protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                if (stack == animatable.getItemBySlot(slot))
                    return slot;
            }
        }

        return EquipmentSlot.CHEST;
    }

    @NotNull
    protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
        return baseModel.body;
    }

    @Nullable
    protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
        return null;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource,
            @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        this.mainHandStack = animatable.getItemBySlot(EquipmentSlot.MAINHAND);
        this.offhandStack = animatable.getItemBySlot(EquipmentSlot.OFFHAND);
        this.helmetStack = animatable.getItemBySlot(EquipmentSlot.HEAD);
        this.chestplateStack = animatable.getItemBySlot(EquipmentSlot.CHEST);
        this.leggingsStack = animatable.getItemBySlot(EquipmentSlot.LEGS);
        this.bootsStack = animatable.getItemBySlot(EquipmentSlot.FEET);
    }

    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource,
            VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack armorStack = getArmorItemForBone(bone, animatable);

        if (armorStack == null)
            return;

        if (armorStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
            renderSkullAsArmor(poseStack, bone, armorStack, skullBlock, bufferSource, packedLight);
        } else {
            EquipmentSlot slot = getEquipmentSlotForBone(bone, armorStack, animatable);
            HumanoidModel<?> model = getModelForItem(bone, slot, armorStack, animatable);
            ModelPart modelPart = getModelPartForBone(bone, slot, armorStack, animatable, model);

            if (!modelPart.cubes.isEmpty()) {
                poseStack.pushPose();
                poseStack.scale(-1, -1, 1);

                if (model instanceof GeoArmorRenderer<?> geoArmorRenderer) {
                    prepModelPartForRender(poseStack, bone, modelPart);
                    geoArmorRenderer.prepForRender(animatable, armorStack, slot, model);
                    geoArmorRenderer.applyBoneVisibilityByPart(slot, modelPart, model);
                    geoArmorRenderer.renderToBuffer(poseStack, null, packedLight, packedOverlay, Color.WHITE.argbInt());
                } else if (armorStack.getItem() instanceof ArmorItem) {
                    prepModelPartForRender(poseStack, bone, modelPart);
                    renderVanillaArmorPiece(poseStack, animatable, bone, slot, armorStack, modelPart, bufferSource, partialTick, packedLight, packedOverlay);
                }

                poseStack.popPose();
            }
        }
    }

    protected <I extends Item & GeoItem> void renderVanillaArmorPiece(PoseStack poseStack, T animatable, GeoBone bone, EquipmentSlot slot, ItemStack armorStack,
            ModelPart modelPart, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        Holder<ArmorMaterial> material = ((ArmorItem) armorStack.getItem()).getMaterial();

        for (ArmorMaterial.Layer layer : material.value().layers()) {
            int color = armorStack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(armorStack, -6265536) : -1;
            VertexConsumer buffer = getVanillaArmorBuffer(bufferSource, animatable, armorStack, slot, bone, layer, packedLight, packedOverlay, false);

            modelPart.render(poseStack, buffer, packedLight, packedOverlay, color);
        }

        ArmorTrim trim = armorStack.get(DataComponents.TRIM);

        if (trim != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(slot == EquipmentSlot.LEGS ? trim.innerTexture(material) : trim.outerTexture(material));
            VertexConsumer buffer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            modelPart.render(poseStack, buffer, packedLight, packedOverlay);
        }

        if (armorStack.hasFoil())
            modelPart.render(poseStack, getVanillaArmorBuffer(bufferSource, animatable, armorStack, slot, bone, null, packedLight, packedOverlay, true), packedLight, packedOverlay, Color.WHITE.argbInt());
    }

    protected VertexConsumer getVanillaArmorBuffer(MultiBufferSource bufferSource, T animatable, ItemStack stack, EquipmentSlot slot, GeoBone bone, @Nullable ArmorMaterial.Layer layer, int packedLight, int packedOverlay, boolean forGlint) {
        if (forGlint)
            return bufferSource.getBuffer(RenderType.armorEntityGlint());

        return bufferSource.getBuffer(RenderType.armorCutoutNoCull(layer.texture(slot == EquipmentSlot.LEGS)));
    }

    @NotNull
    protected HumanoidModel<?> getModelForItem(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable) {
        HumanoidModel<LivingEntity> defaultModel = slot == EquipmentSlot.LEGS ? INNER_ARMOR_MODEL : OUTER_ARMOR_MODEL;

        return BlueLibConstants.PlatformHelper.ITEM_RENDERING.getArmorModelForItem(animatable, stack, slot, defaultModel);
    }

    protected void renderSkullAsArmor(PoseStack poseStack, GeoBone bone, ItemStack stack, AbstractSkullBlock skullBlock, MultiBufferSource bufferSource, int packedLight) {
        SkullBlock.Type type = skullBlock.getType();
        SkullModelBase model = SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(type);
        RenderType renderType = SkullBlockRenderer.getRenderType(type, stack.get(DataComponents.PROFILE));

        poseStack.pushPose();
        RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);
        poseStack.scale(1.1875f, 1.1875f, 1.1875f);
        poseStack.translate(-0.5f, 0, -0.5f);
        SkullBlockRenderer.renderSkull(null, 0, 0, poseStack, bufferSource, packedLight, model, renderType);
        poseStack.popPose();
    }

    protected void prepModelPartForRender(PoseStack poseStack, GeoBone bone, ModelPart sourcePart) {
        final GeoCube firstCube = bone.getCubes().getFirst();
        final Cube armorCube = getReferenceCubeForModel(bone, sourcePart);
        final double armorBoneSizeX = firstCube.size().x();
        final double armorBoneSizeY = firstCube.size().y();
        final double armorBoneSizeZ = firstCube.size().z();
        final double actualArmorSizeX = Math.abs(armorCube.maxX - armorCube.minX);
        final double actualArmorSizeY = Math.abs(armorCube.maxY - armorCube.minY);
        final double actualArmorSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);
        float scaleX = (float) (armorBoneSizeX / actualArmorSizeX);
        float scaleY = (float) (armorBoneSizeY / actualArmorSizeY);
        float scaleZ = (float) (armorBoneSizeZ / actualArmorSizeZ);

        sourcePart.setPos(-(bone.getPivotX() - ((bone.getPivotX() * scaleX) - bone.getPivotX()) / scaleX),
                -(bone.getPivotY() - ((bone.getPivotY() * scaleY) - bone.getPivotY()) / scaleY),
                (bone.getPivotZ() - ((bone.getPivotZ() * scaleZ) - bone.getPivotZ()) / scaleZ));

        sourcePart.xRot = -bone.getRotX();
        sourcePart.yRot = -bone.getRotY();
        sourcePart.zRot = bone.getRotZ();

        poseStack.scale(scaleX, scaleY, scaleZ);
    }

    protected Cube getReferenceCubeForModel(GeoBone bone, ModelPart sourcePart) {
        return sourcePart.cubes.getFirst();
    }
}
