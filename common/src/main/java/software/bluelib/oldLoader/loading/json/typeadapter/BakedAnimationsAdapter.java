/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.loading.json.typeadapter;

import com.google.gson.*;
import it.unimi.dsi.fastutil.doubles.DoubleObjectPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.math.NumberUtils;
import software.bluelib.api.exception.CompoundException;
import software.bluelib.api.utils.JsonUtils;
import software.bluelib.client.loader.cache.animations.AnimationCache;
import software.bluelib.client.loader.cache.animations.AnimationLibraryCache;
import software.bluelib.client.loader.cache.animations.keyframe.BoneAnimationCache;
import software.bluelib.client.loader.cache.animations.keyframe.KeyframeCache;
import software.bluelib.client.loader.cache.animations.keyframe.KeyframeLibraryCache;
import software.bluelib.client.loader.cache.animations.keyframe.KeyframeStackCache;
import software.bluelib.oldLoader.animation.EasingType;
import software.bluelib.oldLoader.loading.math.MathParser;
import software.bluelib.oldLoader.loading.math.MathValue;
import software.bluelib.oldLoader.loading.math.value.Constant;

public class BakedAnimationsAdapter implements JsonDeserializer<AnimationLibraryCache> {

	public static ConcurrentMap<Double, Constant> COMPRESSION_CACHE = null;

	@Override
	public AnimationLibraryCache deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws RuntimeException {
		JsonObject obj = json.getAsJsonObject();
		Map<String, AnimationCache> animations = new Object2ObjectOpenHashMap<>(obj.size());

		for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
			try {
				animations.put(entry.getKey(), bakeAnimation(entry.getKey(), entry.getValue().getAsJsonObject(), context));
			} catch (Exception ex) {
				if (ex instanceof CompoundException compoundEx) {
					//BlueLibConstants.LOGGER.error(compoundEx.withMessage("Unable to parse animation: " + entry.getKey()).getLocalizedMessage());
				} else {
					//BlueLibConstants.LOGGER.error("Unable to parse animation: " + entry.getKey());
				}

				ex.printStackTrace();
			}
		}

		return new AnimationLibraryCache(animations);
	}

	private AnimationCache bakeAnimation(String name, JsonObject animationObj, JsonDeserializationContext context) throws CompoundException {
		double length = animationObj.has("animation_length") ? GsonHelper.getAsDouble(animationObj, "animation_length") * 20d : -1;
		AnimationCache.LoopType loopType = AnimationCache.LoopType.fromJson(animationObj.get("loop"));
		BoneAnimationCache[] boneAnimationCaches = bakeBoneAnimations(GsonHelper.getAsJsonObject(animationObj, "bones", new JsonObject()));
		KeyframeLibraryCache keyframes = context.deserialize(animationObj, KeyframeLibraryCache.class);

		if (length == -1)
			length = calculateAnimationLength(boneAnimationCaches);

		return new AnimationCache(name, length, loopType, boneAnimationCaches, keyframes);
	}

	private BoneAnimationCache[] bakeBoneAnimations(JsonObject bonesObj) throws CompoundException {
		BoneAnimationCache[] animations = new BoneAnimationCache[bonesObj.size()];
		int index = 0;

		for (Map.Entry<String, JsonElement> entry : bonesObj.entrySet()) {
			JsonObject entryObj = entry.getValue().getAsJsonObject();
			KeyframeStackCache<KeyframeCache<MathValue>> scaleFrames = buildKeyframeStack(getKeyframes(entryObj.get("scale")), false);
			KeyframeStackCache<KeyframeCache<MathValue>> positionFrames = buildKeyframeStack(getKeyframes(entryObj.get("position")), false);
			KeyframeStackCache<KeyframeCache<MathValue>> rotationFrames = buildKeyframeStack(getKeyframes(entryObj.get("rotation")), true);

			animations[index] = new BoneAnimationCache(entry.getKey(), rotationFrames, positionFrames, scaleFrames);
			index++;
		}

		return animations;
	}

	private static List<DoubleObjectPair<JsonElement>> getKeyframes(JsonElement element) {
		if (element == null)
			return List.of();

		if (element instanceof JsonPrimitive primitive) {
			JsonArray array = new JsonArray(3);

			array.add(primitive);
			array.add(primitive);
			array.add(primitive);

			element = array;
		}

		if (element instanceof JsonArray array)
			return ObjectArrayList.of(DoubleObjectPair.of(0, array));

		if (element instanceof JsonObject obj) {
			if (obj.has("vector"))
				return ObjectArrayList.of(DoubleObjectPair.of(0, obj));

			List<DoubleObjectPair<JsonElement>> list = new ObjectArrayList<>();

			for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
				double timestamp = readTimestamp(entry.getKey());

				if (timestamp == 0 && !list.isEmpty())
					throw new JsonParseException("Invalid keyframe data - multiple starting keyframes?" + entry.getKey());

				if (entry.getValue() instanceof JsonObject entryObj && !entryObj.has("vector")) {
					addBedrockKeyframes(timestamp, entryObj, list);

					continue;
				}

				list.add(DoubleObjectPair.of(timestamp, entry.getValue()));
			}

			return list;
		}

		throw new JsonParseException("Invalid object type provided to getTripletObj, got: " + element);
	}

	private static void addBedrockKeyframes(double timestamp, JsonObject keyframe, List<DoubleObjectPair<JsonElement>> keyframes) {
		boolean addedFrame = false;

		if (keyframe.has("pre")) {
			JsonElement pre = keyframe.get("pre");
			addedFrame = true;

			keyframes.add(DoubleObjectPair.of(timestamp == 0 ? timestamp : timestamp - 0.001d, pre.isJsonArray() ? pre.getAsJsonArray() : GsonHelper.getAsJsonArray(pre.getAsJsonObject(), "vector")));
		}

		if (keyframe.has("post")) {
			JsonElement post = keyframe.get("post");
			JsonArray values = post.isJsonArray() ? post.getAsJsonArray() : GsonHelper.getAsJsonArray(post.getAsJsonObject(), "vector");

			if (keyframe.has("lerp_mode")) {
				JsonObject keyframeObj = new JsonObject();

				keyframeObj.add("vector", values);
				keyframeObj.add("easing", keyframe.get("lerp_mode"));

				keyframes.add(DoubleObjectPair.of(timestamp, keyframeObj));
			} else {
				keyframes.add(DoubleObjectPair.of(timestamp, values));
			}

			return;
		}

		if (!addedFrame)
			throw new JsonParseException("Invalid keyframe data - expected array, found " + keyframe);
	}

	private KeyframeStackCache<KeyframeCache<MathValue>> buildKeyframeStack(List<DoubleObjectPair<JsonElement>> entries, boolean isForRotation) throws CompoundException {
		if (entries.isEmpty())
			return new KeyframeStackCache<>();

		List<KeyframeCache<MathValue>> xFrames = new ObjectArrayList<>();
		List<KeyframeCache<MathValue>> yFrames = new ObjectArrayList<>();
		List<KeyframeCache<MathValue>> zFrames = new ObjectArrayList<>();

		MathValue xPrev = null;
		MathValue yPrev = null;
		MathValue zPrev = null;
		DoubleObjectPair<JsonElement> prevEntry = null;

		for (DoubleObjectPair<JsonElement> entry : entries) {
			JsonElement element = entry.right();

			double prevTime = prevEntry != null ? prevEntry.leftDouble() : 0;
			double curTime = entry.leftDouble();
			double timeDelta = curTime - prevTime;

			JsonArray keyFrameVector = element instanceof JsonArray array ? array : GsonHelper.getAsJsonArray(element.getAsJsonObject(), "vector");
			// TODO: Found Keyframe handler for molang
			MathValue rawXValue = MathParser.parseJson(keyFrameVector.get(0));
			MathValue rawYValue = MathParser.parseJson(keyFrameVector.get(1));
			MathValue rawZValue = MathParser.parseJson(keyFrameVector.get(2));
			MathValue xValue = compressMathValue(isForRotation && rawXValue instanceof Constant ? new Constant(Math.toRadians(-rawXValue.get())) : rawXValue);
			MathValue yValue = compressMathValue(isForRotation && rawYValue instanceof Constant ? new Constant(Math.toRadians(-rawYValue.get())) : rawYValue);
			MathValue zValue = compressMathValue(isForRotation && rawZValue instanceof Constant ? new Constant(Math.toRadians(rawZValue.get())) : rawZValue);

			JsonObject entryObj = element instanceof JsonObject obj ? obj : null;
			EasingType easingType = entryObj != null && entryObj.has("easing") ? EasingType.fromJson(entryObj.get("easing")) : EasingType.LINEAR;
			List<MathValue> easingArgs = entryObj != null && entryObj.has("easingArgs") ? JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(entryObj, "easingArgs"), ele -> new Constant(ele.getAsDouble())) : new ObjectArrayList<>();

			xFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? xValue : xPrev, xValue, easingType, easingArgs));
			yFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? yValue : yPrev, yValue, easingType, easingArgs));
			zFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? zValue : zPrev, zValue, easingType, easingArgs));

			xPrev = xValue;
			yPrev = yValue;
			zPrev = zValue;
			prevEntry = entry;
		}

		return new KeyframeStackCache<>(addSplineArgs(xFrames), addSplineArgs(yFrames), addSplineArgs(zFrames));
	}

	private List<KeyframeCache<MathValue>> addSplineArgs(List<KeyframeCache<MathValue>> frames) {
		if (frames.size() == 1) {
			KeyframeCache<MathValue> frame = frames.getFirst();

			if (frame.easingType() != EasingType.LINEAR) {
				frames.set(0, new KeyframeCache<>(frame.length(), frame.startValue(), frame.endValue()));

				return frames;
			}
		}

		for (int i = 0; i < frames.size(); i++) {
			KeyframeCache<MathValue> frame = frames.get(i);

			if (frame.easingType() == EasingType.CATMULLROM) {
				frames.set(i, new KeyframeCache<>(frame.length(), frame.startValue(), frame.endValue(), frame.easingType(), ObjectArrayList.of(
						i == 0 ? frame.startValue() : frames.get(i - 1).endValue(),
						i + 1 >= frames.size() ? frame.endValue() : frames.get(i + 1).endValue())));
			}
		}

		return frames;
	}

	private MathValue compressMathValue(MathValue input) {
		if (COMPRESSION_CACHE == null || input.isMutable())
			return input;

		return COMPRESSION_CACHE.computeIfAbsent(input.get(), Constant::new);
	}

	private static double calculateAnimationLength(BoneAnimationCache[] boneAnimationCaches) {
		double length = 0;

		for (BoneAnimationCache animation : boneAnimationCaches) {
			length = Math.max(length, animation.rotationKeyFrames().getLastKeyframeTime());
			length = Math.max(length, animation.positionKeyFrames().getLastKeyframeTime());
			length = Math.max(length, animation.scaleKeyFrames().getLastKeyframeTime());
		}

		return length == 0 ? Double.MAX_VALUE : length;
	}

	private static double readTimestamp(String timestamp) {
		return NumberUtils.isCreatable(timestamp) ? Double.parseDouble(timestamp) : 0;
	}
}
