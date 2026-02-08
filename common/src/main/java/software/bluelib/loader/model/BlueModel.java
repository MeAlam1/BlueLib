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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.utils.loader.LoaderUtils;
import software.bluelib.client.utils.RenderUtils;
import software.bluelib.loader.animatable.base.AnimatableManager;
import software.bluelib.loader.animatable.base.BlueAnimatable;
import software.bluelib.loader.animatable.entity.BlueReplacedEntity;
import software.bluelib.loader.animation.AnimationProcessor;
import software.bluelib.loader.animation.AnimationState;
import software.bluelib.loader.cache.ResourceCache;
import software.bluelib.loader.cache.animations.AnimationCache;
import software.bluelib.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.loader.cache.model.BoneCache;
import software.bluelib.loader.cache.model.ModelCache;
import software.bluelib.loader.geckolib.constant.DataTickets;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;
import software.bluelib.loader.renderer.base.BlueRenderer;

public abstract class BlueModel<T extends BlueAnimatable> {

	@NotNull
	private final AnimationProcessor<T> processor = new AnimationProcessor<>(this);

	@Nullable
	private ModelCache currentModel = null;
	private double animTime;
	private double lastGameTickTime;
	private long lastRenderedInstance = -1;

	@Nullable
	private final ResourceLocation basePath;

	protected BlueModel() {
		this.basePath = null;
	}

	protected BlueModel(@Nullable ResourceLocation pBasePath) {
		this.basePath = pBasePath;
	}

	protected @Nullable String subtype() {
		return null;
	}

	@NotNull
	public ResourceLocation getModelResource(@NotNull T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		if (basePath == null) {
			throw new IllegalStateException("No basePath specified for model resource.");
		}
		String type = subtype();
		if (type == null) return basePath;
		return basePath.withPath("geo/" + type + "/" + basePath.getPath() + ".geo.json");
	}

	@NotNull
	public ResourceLocation getTextureResource(@NotNull T pAnimatable, @Nullable BlueRenderer<T> pRenderer) {
		if (basePath == null) {
			throw new IllegalStateException("No basePath specified for texture resource.");
		}
		String type = subtype();
		if (type == null) return basePath;
		return basePath.withPath("textures/" + type + "/" + basePath.getPath() + ".png");
	}

	@NotNull
	public ResourceLocation getAnimationResource(@NotNull T pAnimatable) {
		if (basePath == null) {
			throw new IllegalStateException("No basePath specified for animation resource.");
		}
		String type = subtype();
		if (type == null) return basePath;
		return basePath.withPath("animations/" + type + "/" + basePath.getPath() + ".animation.json");
	}

	@NotNull
	public ResourceLocation[] getAnimationResourceFallbacks(@NotNull T pAnimatable) {
		return new ResourceLocation[0];
	}

	public boolean crashIfBoneMissing() {
		return false;
	}

	@NotNull
	public RenderType getRenderType(@NotNull T pAnimatable, @NotNull ResourceLocation pTexture) {
		return RenderType.entityCutoutNoCull(pTexture);
	}

	@NotNull
	public ModelCache getBakedModel(@NotNull ResourceLocation pLocation) {
		ResourceLocation[] attempts = new ResourceLocation[] {
				pLocation,
				LoaderUtils.stripSuffix(".json", pLocation),
				LoaderUtils.stripSuffix(".geo.json", pLocation)
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

		if (!pLocation.getPath().contains("model/"))
			throw new RuntimeException("Invalid model resource path provided - BlueLib models must be placed in assets/<modid>/model/");

		throw new RuntimeException("Unable to find model file: " + pLocation);
	}

	@NotNull
	public Optional<BoneCache> getBone(@NotNull String pName) {
		return Optional.of(getAnimationProcessor().getBone(pName));
	}

	@Nullable
	public AnimationCache getAnimation(@NotNull T pAnimatable, @NotNull String pName) {
		ResourceLocation location = getAnimationResource(pAnimatable);
		ResourceLocation[] attempts = new ResourceLocation[] {
				location,
				LoaderUtils.stripSuffix(".json", location),
				LoaderUtils.stripSuffix(".animation.json", location)
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

		if (!location.getPath().contains("animation/"))
			throw new RuntimeException("Invalid animation resource path provided - BlueLib animations must be placed in assets/<modid>/animations/");

		throw new RuntimeException("Unable to find animation file: " + location);
	}

	@NotNull
	public AnimationProcessor<T> getAnimationProcessor() {
		return this.processor;
	}

	public void addAdditionalStateData(@NotNull T pAnimatable, long pInstanceId, @NotNull BiConsumer<DataTicket<T>, T> pDataConsumer) {}

	@ApiStatus.Internal
	public void handleAnimations(@NotNull T pAnimatable, long pInstanceId, @NotNull AnimationState<T> pAnimationState, float pPartialTick) {
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

		if (!mc.isPaused() || pAnimatable.playWhilePaused()) {
			animatableManager.updatedAt(currentFrameTime);

			double lastUpdateTime = animatableManager.getLastUpdateTime();
			this.animTime += lastUpdateTime - this.lastGameTickTime;
			this.lastGameTickTime = lastUpdateTime;
		}

		pAnimationState.setAnimationTick(this.animTime);
		this.lastRenderedInstance = pInstanceId;
		AnimationProcessor<T> processor = getAnimationProcessor();

		processor.preAnimationSetup(pAnimationState, this.animTime);

		if (!processor.getRegisteredBones().isEmpty())
			processor.tickAnimation(pAnimatable, this, animatableManager, this.animTime, pAnimationState, crashIfBoneMissing());

		setCustomAnimations(pAnimatable, pInstanceId, pAnimationState);
	}

	public void setCustomAnimations(@NotNull T pAnimatable, long pInstanceId, @NotNull AnimationState<T> pAnimationState) {}

	public void applyMolangQueries(@NotNull AnimationState<T> pAnimationState, double pAnimTime) {
		this.animTime = pAnimTime;
	}
}
