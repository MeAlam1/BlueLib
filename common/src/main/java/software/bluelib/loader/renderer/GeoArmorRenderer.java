/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bluelib.loader.GeckoLibServices;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.GeoItem;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.cache.texture.AnimatableTexture;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.model.GeoModel;
import software.bluelib.loader.renderer.layer.GeoRenderLayer;
import software.bluelib.loader.renderer.layer.GeoRenderLayersContainer;
import software.bluelib.loader.util.Color;
import software.bluelib.loader.util.RenderUtil;

public class GeoArmorRenderer<T extends Item & GeoItem> extends HumanoidModel implements GeoRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;

    protected T animatable;
    protected HumanoidModel<?> baseModel;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected BakedGeoModel lastModel = null;
    protected GeoBone head = null;
    protected GeoBone body = null;
    protected GeoBone rightArm = null;
    protected GeoBone leftArm = null;
    protected GeoBone rightLeg = null;
    protected GeoBone leftLeg = null;
    protected GeoBone rightBoot = null;
    protected GeoBone leftBoot = null;

    protected Entity currentEntity = null;
    protected ItemStack currentStack = null;
    protected EquipmentSlot currentSlot = null;
    protected MultiBufferSource bufferSource = null;
    protected float partialTick;
    protected float limbSwing;
    protected float limbSwingAmount;
    protected float netHeadYaw;
    protected float headPitch;

    public GeoArmorRenderer(GeoModel<T> model) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));

        this.model = model;
        this.young = false;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    public T getAnimatable() {
        return this.animatable;
    }

    public Entity getCurrentEntity() {
        return this.currentEntity;
    }

    public ItemStack getCurrentStack() {
        return this.currentStack;
    }

    public EquipmentSlot getCurrentSlot() {
        return this.currentSlot;
    }

    @Override
    public long getInstanceId(T animatable) {
        return -GeoItem.getId(this.currentStack);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoArmorRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);

        return this;
    }

    public GeoArmorRenderer<T> withScale(float scale) {
        return withScale(scale, scale);
    }

    public GeoArmorRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;

        return this;
    }

    @Nullable
    public GeoBone getHeadBone(GeoModel<T> model) {
        return model.getBone("armorHead").orElse(null);
    }

    @Nullable
    public GeoBone getBodyBone(GeoModel<T> model) {
        return model.getBone("armorBody").orElse(null);
    }

    @Nullable
    public GeoBone getRightArmBone(GeoModel<T> model) {
        return model.getBone("armorRightArm").orElse(null);
    }

    @Nullable
    public GeoBone getLeftArmBone(GeoModel<T> model) {
        return model.getBone("armorLeftArm").orElse(null);
    }

    @Nullable
    public GeoBone getRightLegBone(GeoModel<T> model) {
        return model.getBone("armorRightLeg").orElse(null);
    }

    @Nullable
    public GeoBone getLeftLegBone(GeoModel<T> model) {
        return model.getBone("armorLeftLeg").orElse(null);
    }

    @Nullable
    public GeoBone getRightBootBone(GeoModel<T> model) {
        return model.getBone("armorRightBoot").orElse(null);
    }

    @Nullable
    public GeoBone getLeftBootBone(GeoModel<T> model) {
        return model.getBone("armorLeftBoot").orElse(null);
    }

    @Override
    public Color getRenderColor(T animatable, float partialTick, int packedLight) {
        return this.currentStack.is(ItemTags.DYEABLE) ? Color.ofOpaque(DyedItemColor.getOrDefault(this.currentStack, -6265536)) : Color.WHITE;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource,
            @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
            int packedOverlay, int colour) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());

        applyBaseModel(this.baseModel);
        grabRelevantBones(model);
        applyBaseTransformations(this.baseModel);
        scaleModelForBaby(poseStack, animatable, partialTick, isReRender);
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);

        if (!(this.currentEntity instanceof GeoAnimatable))
            applyBoneVisibilityBySlot(this.currentSlot);
    }

    @Override
    @ApiStatus.Internal
    public void renderToBuffer(PoseStack poseStack, @Nullable VertexConsumer buffer, int packedLight,
            int packedOverlay, int colour) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource bufferSource = mc.levelRenderer.renderBuffers.bufferSource();

        if (mc.levelRenderer.shouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity))
            bufferSource = mc.levelRenderer.renderBuffers.outlineBufferSource();

        float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(true);
        RenderType renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
        buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, this.currentStack.hasFoil());

        defaultRender(poseStack, this.animatable, bufferSource, null, buffer,
                0, partialTick, packedLight);

        this.animatable = null;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
            MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
            int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        poseStack.translate(0, 24 / 16f, 0);
        poseStack.scale(-1, -1, 1);

        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);
            GeoModel<T> currentModel = getGeoModel();

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
            currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            currentModel.handleAnimations(animatable, instanceId, animationState, partialTick);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (buffer != null)
            GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                    packedLight, packedOverlay, colour);

        poseStack.popPose();
    }

    @Override
    public void doPostRenderCleanup() {
        this.baseModel = null;
        this.currentEntity = null;
        this.currentStack = null;
        this.animatable = null;
        this.currentSlot = null;
        this.bufferSource = null;
        this.partialTick = 0;
        this.limbSwing = 0;
        this.limbSwingAmount = 0;
        this.netHeadYaw = 0;
        this.headPitch = 0;
    }

    @Deprecated(forRemoval = true)
    public void doArmourPostRenderCleanup() {}

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
            int packedOverlay, int colour) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
        }

        GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel)
            return;

        GeoModel<T> model = getGeoModel();
        this.lastModel = bakedModel;
        this.head = getHeadBone(model);
        this.body = getBodyBone(model);
        this.rightArm = getRightArmBone(model);
        this.leftArm = getLeftArmBone(model);
        this.rightLeg = getRightLegBone(model);
        this.leftLeg = getLeftLegBone(model);
        this.rightBoot = getRightBootBone(model);
        this.leftBoot = getLeftBootBone(model);
    }

    @Deprecated(forRemoval = true)
    public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable EquipmentSlot slot, @Nullable HumanoidModel<?> baseModel) {
        if (entity == null || slot == null || baseModel == null)
            return;

        final Minecraft mc = Minecraft.getInstance();

        prepForRender(entity, stack, slot, baseModel, mc.levelRenderer.renderBuffers.bufferSource(), mc.getTimer().getGameTimeDeltaPartialTick(true), 0, 0, 0, 0);
    }

    public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel, MultiBufferSource bufferSource,
            float partialTick, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        this.baseModel = baseModel;
        this.currentEntity = entity;
        this.currentStack = stack;
        this.animatable = (T) stack.getItem();
        this.currentSlot = slot;
        this.bufferSource = bufferSource;
        this.partialTick = partialTick;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
    }

    protected void applyBaseModel(HumanoidModel<?> baseModel) {
        HumanoidModel<?> self = (HumanoidModel<?>) this;

        self.young = baseModel.young;
        self.crouching = baseModel.crouching;
        self.riding = baseModel.riding;
        self.rightArmPose = baseModel.rightArmPose;
        self.leftArmPose = baseModel.leftArmPose;
        self.head.visible = baseModel.head.visible;
        self.hat.visible = baseModel.hat.visible;
        self.body.visible = baseModel.body.visible;
        self.rightArm.visible = baseModel.rightArm.visible;
        self.leftArm.visible = baseModel.leftArm.visible;
        self.rightLeg.visible = baseModel.rightLeg.visible;
        self.leftLeg.visible = baseModel.leftLeg.visible;
    }

    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        setAllBonesVisible(false);
        HumanoidModel<?> model = this;

        switch (currentSlot) {
            case HEAD -> setBoneVisible(this.head, model.head.visible);
            case CHEST -> {
                setBoneVisible(this.body, model.body.visible);
                setBoneVisible(this.rightArm, model.rightArm.visible);
                setBoneVisible(this.leftArm, model.leftArm.visible);
            }
            case LEGS -> {
                setBoneVisible(this.rightLeg, model.rightLeg.visible);
                setBoneVisible(this.leftLeg, model.leftLeg.visible);
            }
            case FEET -> {
                setBoneVisible(this.rightBoot, model.rightLeg.visible);
                setBoneVisible(this.leftBoot, model.leftLeg.visible);
            }
            default -> {}
        }
    }

    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        setAllVisible(false);

        currentPart.visible = true;
        GeoBone bone = null;

        if (currentPart == model.hat || currentPart == model.head) {
            bone = this.head;
        } else if (currentPart == model.body) {
            bone = this.body;
        } else if (currentPart == model.leftArm) {
            bone = this.leftArm;
        } else if (currentPart == model.rightArm) {
            bone = this.rightArm;
        } else if (currentPart == model.leftLeg) {
            bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
        } else if (currentPart == model.rightLeg) {
            bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
        }

        if (bone != null)
            bone.setHidden(false);
    }

    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;

            RenderUtil.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtil.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtil.matchModelPartRot(rightArmPart, this.rightArm);
            this.rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtil.matchModelPartRot(leftArmPart, this.leftArm);
            this.leftArm.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtil.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);

            if (this.rightBoot != null) {
                RenderUtil.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;

            RenderUtil.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);

            if (this.leftBoot != null) {
                RenderUtil.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
            }
        }
    }

    @Override
    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        setAllBonesVisible(visible);
    }

    protected void setAllBonesVisible(boolean visible) {
        setBoneVisible(this.head, visible);
        setBoneVisible(this.body, visible);
        setBoneVisible(this.rightArm, visible);
        setBoneVisible(this.leftArm, visible);
        setBoneVisible(this.rightLeg, visible);
        setBoneVisible(this.leftLeg, visible);
        setBoneVisible(this.rightBoot, visible);
        setBoneVisible(this.leftBoot, visible);
    }

    public void scaleModelForBaby(PoseStack poseStack, T animatable, float partialTick, boolean isReRender) {
        if (!this.young || isReRender)
            return;

        if (this.currentSlot == EquipmentSlot.HEAD) {
            if (this.baseModel.scaleHead) {
                float headScale = 1.5f / this.baseModel.babyHeadScale;

                poseStack.scale(headScale, headScale, headScale);
            }

            poseStack.translate(0, this.baseModel.babyYHeadOffset / 16f, this.baseModel.babyZHeadOffset / 16f);
        } else {
            float bodyScale = 1 / this.baseModel.babyBodyScale;

            poseStack.scale(bodyScale, bodyScale, bodyScale);
            poseStack.translate(0, this.baseModel.bodyYOffset / 16f, 0);
        }
    }

    protected void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        if (bone == null)
            return;

        bone.setHidden(!visible);
    }

    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        if (this.currentEntity != null)
            AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        GeckoLibServices.Client.EVENTS.fireCompileArmorRenderLayers(this);
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return GeckoLibServices.Client.EVENTS.fireArmorPreRender(this, poseStack, model, bufferSource, partialTick, packedLight);
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        GeckoLibServices.Client.EVENTS.fireArmorPostRender(this, poseStack, model, bufferSource, partialTick, packedLight);
    }
}
