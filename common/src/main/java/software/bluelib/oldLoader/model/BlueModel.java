/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.model;

import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.animations.AnimationCache;
import software.bluelib.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.oldLoader.animatable.BlueAnimatable;
import software.bluelib.oldLoader.animatable.BlueReplacedEntity;
import software.bluelib.oldLoader.animation.AnimatableManager;
import software.bluelib.oldLoader.animation.AnimationProcessor;
import software.bluelib.oldLoader.animation.AnimationState;
import software.bluelib.oldLoader.constant.DataTickets;
import software.bluelib.oldLoader.constant.dataticket.DataTicket;
import software.bluelib.oldLoader.renderer.BlueRenderer;

public abstract class BlueModel<T extends BlueAnimatable> {

	private final AnimationProcessor<T> processor = new AnimationProcessor<>(this);

	private ModelCache currentModel = null;
	private double animTime;
	private double lastGameTickTime;
	private long lastRenderedInstance = -1;

	public ResourceLocation getModelResource(T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		return getModelResource(pAnimatable);
	}

	@Deprecated
	public abstract ResourceLocation getModelResource(T pAnimatable);

	public ResourceLocation getTextureResource(T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		return getTextureResource(pAnimatable);
	}

	@Deprecated
	public abstract ResourceLocation getTextureResource(T pAnimatable);

	public abstract ResourceLocation getAnimationResource(T pAnimatable);

	public ResourceLocation[] getAnimationResourceFallbacks(T pAnimatable) {
		return new ResourceLocation[0];
	}

	public boolean crashIfBoneMissing() {
		return false;
	}

	@Nullable
	public RenderType getRenderType(T pAnimatable, ResourceLocation pTexture) {
		return RenderType.entityCutoutNoCull(pTexture);
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

	public ModelCache getBakedModel(ResourceLocation pLocation) {
		ResourceLocation[] attempts = new ResourceLocation[] {
				pLocation,
				stripSuffix(".json", pLocation),
				stripSuffix(".geo.json", pLocation)
		};

		for (ResourceLocation loc : attempts) {
			ModelCache model = ResourceCache.Client.getBakedModels().get(loc);
			if (model != null) {
				if (model != this.currentModel) {
					this.processor.setActiveModel(model);
					this.currentModel = model;
				}
				return this.currentModel;
			}
		}

		if (!pLocation.getPath().contains("models/"))
			throw new RuntimeException("Invalid model resource path provided - BlueLib models must be placed in assets/<modid>/models/");

		throw new RuntimeException("Unable to find model file: " + pLocation);
	}

	public Optional<BoneCache> getBone(String pName) {
		return Optional.ofNullable(getAnimationProcessor().getBone(pName));
	}

	@Nullable
	public AnimationCache getAnimation(T pAnimatable, String pName) {
		ResourceLocation location = getAnimationResource(pAnimatable);
		ResourceLocation[] attempts = new ResourceLocation[] {
				location,
				stripSuffix(".json", location),
				stripSuffix(".animation.json", location)
		};

		for (ResourceLocation loc : attempts) {
			AnimationLibraryCache animationLibraryCache = ResourceCache.Client.getBakedAnimations().get(loc);
			AnimationCache animationCache = animationLibraryCache != null ? animationLibraryCache.getAnimation(pName) : null;
			if (animationCache != null)
				return animationCache;
		}

		for (ResourceLocation fallbackLocation : getAnimationResourceFallbacks(pAnimatable)) {
			AnimationLibraryCache animationLibraryCache = ResourceCache.Client.getBakedAnimations().get(fallbackLocation);
			AnimationCache animationCache = animationLibraryCache != null ? animationLibraryCache.getAnimation(pName) : null;
			if (animationCache != null)
				return animationCache;
		}

		if (!location.getPath().contains("animations/"))
			throw new RuntimeException("Invalid animation resource path provided - BlueLib animations must be placed in assets/<modid>/animations/");

		throw new RuntimeException("Unable to find animation file: " + location);
	}

	public AnimationProcessor<T> getAnimationProcessor() {
		return this.processor;
	}

	public void addAdditionalStateData(T pAnimatable, long pInstanceId, BiConsumer<DataTicket<T>, T> pDataConsumer) {}

	@ApiStatus.Internal
	public void handleAnimations(T pAnimatable, long pInstanceId, AnimationState<T> pAnimationState, float pPartialTick) {
		Minecraft mc = Minecraft.getInstance();
		AnimatableManager<T> animatableManager = pAnimatable.getAnimatableInstanceCache().getManagerForId(pInstanceId);
		Double currentTick = pAnimationState.getData(DataTickets.TICK);

		if (currentTick == null)
			currentTick = pAnimatable instanceof Entity entity ? (double) entity.tickCount : RenderUtils.getCurrentTick();

		if (animatableManager.getFirstTickTime() == -1)
			animatableManager.startedAt(currentTick + pPartialTick);

		double currentFrameTime = pAnimatable instanceof Entity || pAnimatable instanceof BlueReplacedEntity ? currentTick + pPartialTick : currentTick - animatableManager.getFirstTickTime();
		boolean pIsReRender = !animatableManager.isFirstTick() && currentFrameTime == animatableManager.getLastUpdateTime();

		if (pIsReRender && pInstanceId == this.lastRenderedInstance)
			return;

		if (!mc.isPaused() || pAnimatable.shouldPlayAnimsWhileGamePaused()) {
			animatableManager.updatedAt(currentFrameTime);

			double lastUpdateTime = animatableManager.getLastUpdateTime();
			this.animTime += lastUpdateTime - this.lastGameTickTime;
			this.lastGameTickTime = lastUpdateTime;
		}

		pAnimationState.animationTick = this.animTime;
		this.lastRenderedInstance = pInstanceId;
		AnimationProcessor<T> processor = getAnimationProcessor();

		processor.preAnimationSetup(pAnimationState, this.animTime);

		if (!processor.getRegisteredBones().isEmpty())
			processor.tickAnimation(pAnimatable, this, animatableManager, this.animTime, pAnimationState, crashIfBoneMissing());

		setCustomAnimations(pAnimatable, pInstanceId, pAnimationState);
	}

	public void setCustomAnimations(T pAnimatable, long pInstanceId, AnimationState<T> pAnimationState) {}

	public void applyMolangQueries(AnimationState<T> pAnimationState, double pAnimTime) {
		this.animTime = pAnimTime;
	}
}
