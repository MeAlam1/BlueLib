/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.json;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.IOUtils;
import software.bluelib.client.loader.json.model.deserialize.Model;
import software.bluelib.client.loader.model.ModelLoader;
import software.bluelib.loader.loading.object.BakedAnimations;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class FileLoader {

	public static BakedAnimations loadAnimationsFile(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
		if (pResourceLocation.getPath().endsWith(".geo.json"))
			throw new IllegalArgumentException("Geo model file found in animations folder!");

		if (!pResourceLocation.getPath().endsWith(".animation.json"))
			System.out.println("Found animation file with improper file name format; animation files should end in .animation.json: '" + pResourceLocation + "'");

		return ModelLoader.MODEL_GSON.fromJson(GsonHelper.getAsJsonObject(loadFile(pResourceLocation, pResourceManager), "animations"), BakedAnimations.class);
	}

	public static Model loadModelFile(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
		if (pResourceLocation.getPath().endsWith(".animation.json"))
			throw new IllegalArgumentException("Animation file found in geo models folder!");

		if (!pResourceLocation.getPath().endsWith(".geo.json"))
			System.out.println("Found geo model file with improper file name format; geo model files should end in .geo.json: '" + pResourceLocation + "'");

		return ModelLoader.MODEL_GSON.fromJson(loadFile(pResourceLocation, pResourceManager), Model.class);
	}

	public static JsonObject loadFile(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
		return GsonHelper.fromJson(ModelLoader.MODEL_GSON, getFileContents(pResourceLocation, pResourceManager), JsonObject.class);
	}

	public static String getFileContents(ResourceLocation pResourceLocation, ResourceManager pResourceManager) {
		try (InputStream inputStream = pResourceManager.getResourceOrThrow(pResourceLocation).open()) {
			return IOUtils.toString(inputStream, Charset.defaultCharset());
		} catch (Exception pException) {
			throw new RuntimeException(new FileNotFoundException(pResourceLocation.toString()));
		}
	}
}
