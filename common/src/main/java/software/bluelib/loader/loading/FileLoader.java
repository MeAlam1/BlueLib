/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.loading;

import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.IOUtils;
import software.bluelib.loader.GeckoLibConstants;
import software.bluelib.loader.loading.json.raw.Model;
import software.bluelib.loader.loading.json.typeadapter.KeyFramesAdapter;
import software.bluelib.loader.loading.object.BakedAnimations;

public final class FileLoader {

    public static BakedAnimations loadAnimationsFile(ResourceLocation location, ResourceManager manager) {
        if (location.getPath().endsWith(".geo.json"))
            throw new IllegalArgumentException("Geo model file found in animations folder!");

        if (!location.getPath().endsWith(".animation.json"))
            GeckoLibConstants.LOGGER.warn("Found animation file with improper file name format; animation files should end in .animation.json: '" + location + "'");

        return KeyFramesAdapter.GEO_GSON.fromJson(GsonHelper.getAsJsonObject(loadFile(location, manager), "animations"), BakedAnimations.class);
    }

    public static Model loadModelFile(ResourceLocation location, ResourceManager manager) {
        if (location.getPath().endsWith(".animation.json"))
            throw new IllegalArgumentException("Animation file found in geo models folder!");

        if (!location.getPath().endsWith(".geo.json"))
            GeckoLibConstants.LOGGER.warn("Found geo model file with improper file name format; geo model files should end in .geo.json: '" + location + "'");

        return KeyFramesAdapter.GEO_GSON.fromJson(loadFile(location, manager), Model.class);
    }

    public static JsonObject loadFile(ResourceLocation location, ResourceManager manager) {
        return GsonHelper.fromJson(KeyFramesAdapter.GEO_GSON, getFileContents(location, manager), JsonObject.class);
    }

    public static String getFileContents(ResourceLocation location, ResourceManager manager) {
        try (InputStream inputStream = manager.getResourceOrThrow(location).open()) {
            return IOUtils.toString(inputStream, Charset.defaultCharset());
        } catch (Exception e) {
            GeckoLibConstants.LOGGER.error("Couldn't load " + location, e);

            throw new RuntimeException(new FileNotFoundException(location.toString()));
        }
    }
}
