/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.model;

import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.AnimationProcessor;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.GeckoLibCache;
import software.bluelib.loader.cache.object.BakedGeoModel;
import software.bluelib.loader.cache.object.GeoBone;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.constant.dataticket.DataTicket;
import software.bluelib.loader.loading.object.BakedAnimations;
import software.bluelib.loader.renderer.GeoRenderer;
import software.bluelib.loader.util.RenderUtil;

public abstract class GeoModel<T extends GeoAnimatable> {

    private final AnimationProcessor<T> processor = new AnimationProcessor<>(this);

    private BakedGeoModel currentModel = null;
    private double animTime;
    private double lastGameTickTime;
    private long lastRenderedInstance = -1;

    public ResourceLocation getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return getModelResource(animatable);
    }

    @Deprecated
    public abstract ResourceLocation getModelResource(T animatable);

    public ResourceLocation getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return getTextureResource(animatable);
    }

    @Deprecated
    public abstract ResourceLocation getTextureResource(T animatable);

    public abstract ResourceLocation getAnimationResource(T animatable);

    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return new ResourceLocation[0];
    }

    public boolean crashIfBoneMissing() {
        return false;
    }

    @Nullable
    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

    public BakedGeoModel getBakedModel(ResourceLocation location) {
        BakedGeoModel model = GeckoLibCache.getBakedModels().get(location);

        if (model == null) {
            if (!location.getPath().contains("geo/"))
                throw GeckoLibConstants.exception(location, "Invalid model resource path provided - GeckoLib models must be placed in assets/<modid>/geo/");

            throw GeckoLibConstants.exception(location, "Unable to find model");
        }

        if (model != this.currentModel) {
            this.processor.setActiveModel(model);
            this.currentModel = model;
        }

        return this.currentModel;
    }

    public Optional<GeoBone> getBone(String name) {
        return Optional.ofNullable(getAnimationProcessor().getBone(name));
    }

    @Nullable
    public Animation getAnimation(T animatable, String name) {
        ResourceLocation location = getAnimationResource(animatable);
        BakedAnimations bakedAnimations = GeckoLibCache.getBakedAnimations().get(location);
        Animation animation = bakedAnimations != null ? bakedAnimations.getAnimation(name) : null;

        if (animation != null)
            return animation;

        for (ResourceLocation fallbackLocation : getAnimationResourceFallbacks(animatable)) {
            bakedAnimations = GeckoLibCache.getBakedAnimations().get(location = fallbackLocation);
            animation = bakedAnimations != null ? bakedAnimations.getAnimation(name) : null;

            if (animation != null)
                return animation;
        }

        if (bakedAnimations == null) {
            if (!location.getPath().contains("animations/"))
                throw GeckoLibConstants.exception(location, "Invalid animation resource path provided - GeckoLib animations must be placed in assets/<modid>/animations/");

            throw GeckoLibConstants.exception(location, "Unable to find animation file.");
        }

        return null;
    }

    public AnimationProcessor<T> getAnimationProcessor() {
        return this.processor;
    }

    public void addAdditionalStateData(T animatable, long instanceId, BiConsumer<DataTicket<T>, T> dataConsumer) {}

    @ApiStatus.Internal
    public void handleAnimations(T animatable, long instanceId, AnimationState<T> animationState, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        AnimatableManager<T> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
        Double currentTick = animationState.getData(DataTickets.TICK);

        if (currentTick == null)
            currentTick = animatable instanceof Entity entity ? (double) entity.tickCount : RenderUtil.getCurrentTick();

        if (animatableManager.getFirstTickTime() == -1)
            animatableManager.startedAt(currentTick + pPartialTick);

        double currentFrameTime = animatable instanceof Entity || animatable instanceof GeoReplacedEntity ? currentTick + pPartialTick : currentTick - animatableManager.getFirstTickTime();
        boolean pIsReRender = !animatableManager.isFirstTick() && currentFrameTime == animatableManager.getLastUpdateTime();

        if (pIsReRender && instanceId == this.lastRenderedInstance)
            return;

        if (!mc.isPaused() || animatable.shouldPlayAnimsWhileGamePaused()) {
            animatableManager.updatedAt(currentFrameTime);

            double lastUpdateTime = animatableManager.getLastUpdateTime();
            this.animTime += lastUpdateTime - this.lastGameTickTime;
            this.lastGameTickTime = lastUpdateTime;
        }

        animationState.animationTick = this.animTime;
        this.lastRenderedInstance = instanceId;
        AnimationProcessor<T> processor = getAnimationProcessor();

        processor.preAnimationSetup(animationState, this.animTime);

        if (!processor.getRegisteredBones().isEmpty())
            processor.tickAnimation(animatable, this, animatableManager, this.animTime, animationState, crashIfBoneMissing());

        setCustomAnimations(animatable, instanceId, animationState);
    }

    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {}

    public void applyMolangQueries(AnimationState<T> animationState, double animTime) {}
}
