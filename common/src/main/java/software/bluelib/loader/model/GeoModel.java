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
import software.bluelib.client.loader.cache.ResourceCache;
import software.bluelib.client.loader.cache.animations.AnimationsCache;
import software.bluelib.client.loader.cache.model.BoneCache;
import software.bluelib.client.loader.cache.model.ModelCache;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.GeoAnimatable;
import software.bluelib.loader.animatable.GeoReplacedEntity;
import software.bluelib.loader.animation.AnimatableManager;
import software.bluelib.loader.animation.Animation;
import software.bluelib.loader.animation.AnimationProcessor;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.constant.DataTickets;
import software.bluelib.loader.constant.dataticket.DataTicket;
import software.bluelib.loader.renderer.GeoRenderer;

public abstract class GeoModel<T extends GeoAnimatable> {

    private final AnimationProcessor<T> processor = new AnimationProcessor<>(this);

    private ModelCache currentModel = null;
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

    public static ResourceLocation stripSuffix(String pSuffix, ResourceLocation pLocation) {
        String path = pLocation.getPath();
        if (path.endsWith(pSuffix)) {
            String newPath = path.substring(0, path.length() - pSuffix.length());
            return pLocation.withPath(newPath);
        } else {
            throw new RuntimeException("Invalid file type: expected a " + pSuffix + " file, got: " + path);
        }
    }

    public ModelCache getBakedModel(ResourceLocation location) {
        location = stripSuffix(".geo.json", location);
        ModelCache model = ResourceCache.getBakedModels().get(location);

        if (model == null) {
            if (!location.getPath().contains("models/"))
                throw new RuntimeException("Invalid model resource path provided - GeckoLib models must be placed in assets/<modid>/models/");

            throw new RuntimeException("Unable to find model file: " + location);
        }

        if (model != this.currentModel) {
            this.processor.setActiveModel(model);
            this.currentModel = model;
        }

        return this.currentModel;
    }

    public Optional<BoneCache> getBone(String name) {
        return Optional.ofNullable(getAnimationProcessor().getBone(name));
    }

    @Nullable
    public Animation getAnimation(T animatable, String name) {
        ResourceLocation location = getAnimationResource(animatable);
        location = stripSuffix(".animation.json", location);
        AnimationsCache animationsCache = ResourceCache.getBakedAnimations().get(location);
        Animation animation = animationsCache != null ? animationsCache.getAnimation(name) : null;

        if (animation != null)
            return animation;

        for (ResourceLocation fallbackLocation : getAnimationResourceFallbacks(animatable)) {
            animationsCache = ResourceCache.getBakedAnimations().get(location = fallbackLocation);
            animation = animationsCache != null ? animationsCache.getAnimation(name) : null;

            if (animation != null)
                return animation;
        }

        if (animationsCache == null) {
            if (!location.getPath().contains("animations/"))
                throw new RuntimeException("Invalid animation resource path provided - GeckoLib animations must be placed in assets/<modid>/animations/");

            throw new RuntimeException("Unable to find animation file: " + location);
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
            currentTick = animatable instanceof Entity entity ? (double) entity.tickCount : RenderUtils.getCurrentTick();

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
