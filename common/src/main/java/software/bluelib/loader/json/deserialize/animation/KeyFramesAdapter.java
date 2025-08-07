/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.reflect.Type;
import java.util.Map;
import net.minecraft.util.GsonHelper;
import software.bluelib.loader.BlueLoader;
import software.bluelib.loader.cache.animations.keyframe.KeyframeLibraryCache;
import software.bluelib.oldLoader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.SoundKeyframeData;

public class KeyFramesAdapter implements JsonDeserializer<KeyframeLibraryCache> {

	@Override
	public KeyframeLibraryCache deserialize(JsonElement pJson, Type pType, JsonDeserializationContext pContext) throws JsonParseException {
		JsonObject obj = pJson.getAsJsonObject();
		SoundKeyframeData[] sounds = buildSoundFrameData(obj);
		ParticleKeyframeData[] particles = buildParticleFrameData(obj);
		CustomInstructionKeyframeData[] customInstructions = buildCustomFrameData(obj);

		return new KeyframeLibraryCache(sounds, particles, customInstructions);
	}

	private static SoundKeyframeData[] buildSoundFrameData(JsonObject pRootObj) {
		JsonObject soundsObj = GsonHelper.getAsJsonObject(pRootObj, "sound_effects", new JsonObject());
		SoundKeyframeData[] sounds = new SoundKeyframeData[soundsObj.size()];
		int index = 0;

		for (Map.Entry<String, JsonElement> entry : soundsObj.entrySet()) {
			sounds[index] = new SoundKeyframeData(Double.parseDouble(entry.getKey()) * 20d, GsonHelper.getAsString(entry.getValue().getAsJsonObject(), "effect"));
			index++;
		}

		return sounds;
	}

	private static ParticleKeyframeData[] buildParticleFrameData(JsonObject pRootObj) {
		JsonObject particlesObj = GsonHelper.getAsJsonObject(pRootObj, "particle_effects", new JsonObject());
		ParticleKeyframeData[] particles = new ParticleKeyframeData[particlesObj.size()];
		int index = 0;

		for (Map.Entry<String, JsonElement> entry : particlesObj.entrySet()) {
			JsonObject obj = entry.getValue().getAsJsonObject();
			String effect = GsonHelper.getAsString(obj, "effect", "");
			String locator = GsonHelper.getAsString(obj, "locator", "");
			String script = GsonHelper.getAsString(obj, "pre_effect_script", "");

			particles[index] = new ParticleKeyframeData(Double.parseDouble(entry.getKey()) * 20d, effect, locator, script);
			index++;
		}

		return particles;
	}

	private static CustomInstructionKeyframeData[] buildCustomFrameData(JsonObject pRootObj) {
		JsonObject customInstructionsObj = GsonHelper.getAsJsonObject(pRootObj, "timeline", new JsonObject());
		CustomInstructionKeyframeData[] customInstructions = new CustomInstructionKeyframeData[customInstructionsObj.size()];
		int index = 0;

		for (Map.Entry<String, JsonElement> entry : customInstructionsObj.entrySet()) {
			String instructions = "";

			if (entry.getValue() instanceof JsonArray array) {
				instructions = BlueLoader.ANIMATION_GSON.fromJson(array, ObjectArrayList.class).toString();
			} else if (entry.getValue() instanceof JsonPrimitive primitive) {
				instructions = primitive.getAsString();
			}

			customInstructions[index] = new CustomInstructionKeyframeData(Double.parseDouble(entry.getKey()) * 20d, instructions);
			index++;
		}

		return customInstructions;
	}
}
