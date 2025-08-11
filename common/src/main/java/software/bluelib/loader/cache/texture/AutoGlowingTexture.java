/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.client.utils.TextureUtils;

// TODO: Clean This Up
public class AutoGlowingTexture extends BlueAbstractTexture {

	@NotNull
	private static final RenderStateShard.ShaderStateShard SHADER_STATE = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityTranslucentEmissiveShader);
	@NotNull
	private static final RenderStateShard.TransparencyStateShard TRANSPARENCY_STATE = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	@NotNull
	private static final RenderStateShard.WriteMaskStateShard WRITE_MASK = new RenderStateShard.WriteMaskStateShard(true, true);
	@NotNull
	private static final BiFunction<ResourceLocation, Boolean, RenderType> GLOWING_RENDER_TYPE = Util.memoize((texture, isGlowing) -> {
		RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(texture, false, false);

		return RenderType.create("glowing_layer", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true,
				RenderType.CompositeState.builder()
						.setShaderState(SHADER_STATE)
						.setTextureState(textureState)
						.setTransparencyState(TRANSPARENCY_STATE)
						.setOverlayState(new RenderStateShard.OverlayStateShard(true))
						.setWriteMaskState(WRITE_MASK).createCompositeState(isGlowing));
	});

	@NotNull
	private static final String APPENDIX = "_glowmask";

	public static boolean PRINT_DEBUG_IMAGES = false;

	@NotNull
	protected final ResourceLocation textureBase;
	@NotNull
	protected final ResourceLocation glowLayer;

	public AutoGlowingTexture(@NotNull ResourceLocation pOriginalLocation, @NotNull ResourceLocation pLocation) {
		this.textureBase = pOriginalLocation;
		this.glowLayer = pLocation;
	}

	@NotNull
	public static ResourceLocation getEmissiveResource(@NotNull ResourceLocation pBaseResource) {
		ResourceLocation path = appendToPath(pBaseResource, APPENDIX);

		generateTexture(path, textureManager -> textureManager.register(path, new AutoGlowingTexture(pBaseResource, path)));

		return path;
	}

	@Nullable
	@Override
	protected RenderCall loadTexture(@NotNull ResourceManager pResourceManager) throws IOException {
		AbstractTexture originalTexture;

		try {
			originalTexture = Minecraft.getInstance().submit(() -> TextureUtils.getTexture(this.textureBase)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new IOException("Failed to load original texture: " + this.textureBase, e);
		}

		Resource textureBaseResource = pResourceManager.getResource(this.textureBase).get();
		NativeImage baseImage = originalTexture instanceof DynamicTexture dynamicTexture ? dynamicTexture.getPixels() : NativeImage.read(textureBaseResource.open());
		NativeImage glowImage = null;
		Optional<TextureMetadataSection> textureBaseMeta = textureBaseResource.metadata().getSection(TextureMetadataSection.SERIALIZER);
		boolean blur = textureBaseMeta.isPresent() && textureBaseMeta.get().isBlur();
		boolean clamp = textureBaseMeta.isPresent() && textureBaseMeta.get().isClamp();

		try {
			Optional<Resource> glowLayerResource = pResourceManager.getResource(this.glowLayer);
			GlowingTextureMeta glowLayerMeta = null;

			if (glowLayerResource.isPresent()) {
				glowImage = NativeImage.read(glowLayerResource.get().open());
				glowLayerMeta = GlowingTextureMeta.fromExistingImage(glowImage);
			} else {
				Optional<GlowingTextureMeta> meta = textureBaseResource.metadata().getSection(GlowingTextureMeta.DESERIALIZER);

				if (meta.isPresent()) {
					glowLayerMeta = meta.get();
					glowImage = new NativeImage(baseImage.getWidth(), baseImage.getHeight(), true);
				}
			}

			if (glowLayerMeta != null) {
				glowLayerMeta.createImageMask(baseImage, glowImage);

				if (PRINT_DEBUG_IMAGES && BlueLibConstants.PlatformHelper.PLATFORM.isDevelopmentEnvironment()) {
					printDebugImageToDisk(this.textureBase, baseImage);
					printDebugImageToDisk(this.glowLayer, glowImage);
				}
			}
		} catch (IOException e) {
			BaseLogger.log(BaseLogLevel.WARNING, "Resource failed to open for glowlayer meta: " + this.glowLayer, e);
		}

		NativeImage mask = glowImage;

		if (mask == null)
			return null;

		boolean animated = originalTexture instanceof AnimatableTexture animatableTexture && animatableTexture.isAnimated();

		if (animated) {
			((AnimatableTexture) originalTexture).animationContents.animatedTexture.setGlowMaskTexture(this, baseImage, mask);
		}

		return () -> {
			if (!animated)
				uploadSimple(getId(), mask, blur, clamp);

			if (originalTexture instanceof DynamicTexture dynamicTexture) {
				dynamicTexture.upload();
			} else {
				uploadSimple(originalTexture.getId(), baseImage, blur, clamp);
			}
		};
	}

	@NotNull
	public static RenderType getRenderType(@NotNull ResourceLocation pTexture) {
		return GLOWING_RENDER_TYPE.apply(getEmissiveResource(pTexture), false);
	}

	@NotNull
	public static RenderType getOutlineRenderType(@NotNull ResourceLocation pTexture) {
		return GLOWING_RENDER_TYPE.apply(getEmissiveResource(pTexture), true);
	}
}
