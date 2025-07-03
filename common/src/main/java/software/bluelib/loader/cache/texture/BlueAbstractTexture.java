/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.BlueLibConstants;
import software.bluelib.client.utils.TextureUtils;

public abstract class BlueAbstractTexture extends AbstractTexture {

	protected static void generateTexture(ResourceLocation pTexturePath, Consumer<TextureManager> pTextureManagerConsumer) {
		if (!RenderSystem.isOnRenderThreadOrInit())
			throw new IllegalThreadStateException("Texture loading called outside of the render thread! This should DEFINITELY not be happening.");

		TextureManager textureManager = TextureUtils.getTextureManager();

		if (!(textureManager.getTexture(pTexturePath, MissingTextureAtlasSprite.getTexture()) instanceof BlueAbstractTexture))
			pTextureManagerConsumer.accept(textureManager);
	}

	@Override
	public final void load(@NotNull ResourceManager pResourceManager) throws IOException {
		RenderCall renderCall = loadTexture(pResourceManager);

		if (renderCall == null)
			return;

		if (!RenderSystem.isOnRenderThreadOrInit()) {
			RenderSystem.recordRenderCall(renderCall);
		} else {
			renderCall.execute();
		}
	}

	protected void printDebugImageToDisk(ResourceLocation pId, NativeImage pNewImage) {
		try {
			File file = new File(BlueLibConstants.PlatformHelper.PLATFORM.getGameDir().toFile(), "BlueTexture Debug Printouts");

			if (!file.exists()) {
				file.mkdirs();
			} else if (!file.isDirectory()) {
				file.delete();
				file.mkdirs();
			}

			file = new File(file, pId.getPath().replace('/', '.'));

			if (!file.exists())
				file.createNewFile();

			pNewImage.writeToFile(file);
		} catch (IOException pIoException) {
			pIoException.printStackTrace();
		}
	}

	@Nullable
	protected abstract RenderCall loadTexture(ResourceManager pResourceManager) throws IOException;

	public static void uploadSimple(int pTexture, NativeImage pImage, boolean pBlur, boolean pClamp) {
		TextureUtil.prepareImage(pTexture, 0, pImage.getWidth(), pImage.getHeight());
		pImage.upload(0, 0, 0, 0, 0, pImage.getWidth(), pImage.getHeight(), pBlur, pClamp, false, true);
	}

	public static ResourceLocation appendToPath(ResourceLocation pLocation, String pSuffix) {
		String path = pLocation.getPath();
		int i = path.lastIndexOf('.');

		return ResourceLocation.fromNamespaceAndPath(pLocation.getNamespace(), path.substring(0, i) + pSuffix + path.substring(i));
	}
}
