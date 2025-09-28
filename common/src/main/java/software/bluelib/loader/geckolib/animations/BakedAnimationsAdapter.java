/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.exception.CompoundException;
import software.bluelib.api.utils.loader.JsonUtils;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.cache.animation.AnimationCache;
import software.bluelib.loader.cache.animation.AnimationsCache;
import software.bluelib.loader.cache.animation.BoneAnimationCache;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;
import software.bluelib.loader.cache.animation.keyframe.KeyframeStackCache;
import software.bluelib.loader.geckolib.math.MathParser;
import software.bluelib.loader.geckolib.math.MathValue;
import software.bluelib.loader.geckolib.math.value.Constant;

public class BakedAnimationsAdapter implements JsonDeserializer<AnimationsCache> {

	@Nullable
	public static ConcurrentMap<Double, Constant> COMPRESSION_CACHE = null;

	@Override
	@NotNull
	public AnimationsCache deserialize(@NotNull JsonElement pJson, @NotNull Type pType, @NotNull JsonDeserializationContext pContext) throws RuntimeException {
		JsonObject obj = pJson.getAsJsonObject();
		Map<String, AnimationCache> animations = new Object2ObjectOpenHashMap<>(obj.size());

		for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
			try {
				//animations.put(entry.getKey(), bakeAnimation(entry.getKey(), entry.getValue().getAsJsonObject(), pContext));
			} catch (Exception pException) {
				if (pException instanceof CompoundException compoundEx) {
					BaseLogger.log(BaseLogLevel.ERROR, compoundEx.withMessage("Unable to parse animation: " + entry.getKey()).getLocalizedMessage(), compoundEx);
				}
				throw new JsonParseException("Failed to parse animations", pException);
			}
		}

		return null;
	}

	/*@NotNull
	private AnimationCache bakeAnimation(@NotNull String pName, @NotNull JsonObject pAnimationObj, @NotNull JsonDeserializationContext pContext) throws CompoundException {
		double length = pAnimationObj.has("animation_length") ? GsonHelper.getAsDouble(pAnimationObj, "animation_length") * 20d : -1;
		LoopType loopType = LoopType.fromJson(pAnimationObj.get("loop"));
		BoneAnimationCache[] boneAnimationCaches = bakeBoneAnimations(GsonHelper.getAsJsonObject(pAnimationObj, "bones", new JsonObject()));
		KeyframeLibraryCache keyframes = pContext.deserialize(pAnimationObj, KeyframeLibraryCache.class);

		if (length == -1)
			length = calculateAnimationLength(boneAnimationCaches);

		return new AnimationCache(pName, length, loopType, boneAnimationCaches, keyframes);
	}

	@NotNull
	private BoneAnimationCache[] bakeBoneAnimations(@NotNull JsonObject pBonesObj) throws CompoundException {
		BoneAnimationCache[] animations = new BoneAnimationCache[pBonesObj.size()];
		int index = 0;

		for (Map.Entry<String, JsonElement> entry : pBonesObj.entrySet()) {
			JsonObject entryObj = entry.getValue().getAsJsonObject();
			KeyframeStackCache<KeyframeCache<MathValue>> scaleFrames = buildKeyframeStack(getKeyframes(entryObj.get("scale")), false);
			KeyframeStackCache<KeyframeCache<MathValue>> positionFrames = buildKeyframeStack(getKeyframes(entryObj.get("position")), false);
			KeyframeStackCache<KeyframeCache<MathValue>> rotationFrames = buildKeyframeStack(getKeyframes(entryObj.get("rotation")), true);

			animations[index] = new BoneAnimationCache(entry.getKey(), rotationFrames, positionFrames, scaleFrames);
			index++;
		}

		return animations;
	}

	@NotNull
	private static List<DoubleObjectPair<JsonElement>> getKeyframes(@Nullable JsonElement pElement) {
		if (pElement == null)
			return List.of();

		if (pElement instanceof JsonPrimitive primitive) {
			JsonArray array = new JsonArray(3);

			array.add(primitive);
			array.add(primitive);
			array.add(primitive);

			pElement = array;
		}

		if (pElement instanceof JsonArray array)
			return ObjectArrayList.of(DoubleObjectPair.of(0, array));

		if (pElement instanceof JsonObject obj) {
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

		throw new JsonParseException("Invalid object type provided to getTripletObj, got: " + pElement);
	}

	private static void addBedrockKeyframes(double pTimestamp, @NotNull JsonObject pKeyframe, @NotNull List<DoubleObjectPair<JsonElement>> pKeyframes) {
		boolean addedFrame = false;

		if (pKeyframe.has("pre")) {
			JsonElement pre = pKeyframe.get("pre");
			addedFrame = true;

			pKeyframes.add(DoubleObjectPair.of(pTimestamp == 0 ? pTimestamp : pTimestamp - 0.001d, pre.isJsonArray() ? pre.getAsJsonArray() : GsonHelper.getAsJsonArray(pre.getAsJsonObject(), "vector")));
		}

		if (pKeyframe.has("post")) {
			JsonElement post = pKeyframe.get("post");
			JsonArray values = post.isJsonArray() ? post.getAsJsonArray() : GsonHelper.getAsJsonArray(post.getAsJsonObject(), "vector");

			if (pKeyframe.has("lerp_mode")) {
				JsonObject keyframeObj = new JsonObject();

				keyframeObj.add("vector", values);
				keyframeObj.add("easing", pKeyframe.get("lerp_mode"));

				pKeyframes.add(DoubleObjectPair.of(pTimestamp, keyframeObj));
			} else {
				pKeyframes.add(DoubleObjectPair.of(pTimestamp, values));
			}

			return;
		}

		if (!addedFrame)
			throw new JsonParseException("Invalid keyframe data - expected array, found " + pKeyframe);
	}

	@NotNull
	private KeyframeStackCache<KeyframeCache<MathValue>> buildKeyframeStack(@NotNull List<DoubleObjectPair<JsonElement>> pEntries, boolean pIsForRotation) throws CompoundException {
		if (pEntries.isEmpty())
			return new KeyframeStackCache<>();

		List<KeyframeCache<MathValue>> xFrames = new ObjectArrayList<>();
		List<KeyframeCache<MathValue>> yFrames = new ObjectArrayList<>();
		List<KeyframeCache<MathValue>> zFrames = new ObjectArrayList<>();

		MathValue xPrev = null;
		MathValue yPrev = null;
		MathValue zPrev = null;
		DoubleObjectPair<JsonElement> prevEntry = null;

		for (DoubleObjectPair<JsonElement> entry : pEntries) {
			JsonElement element = entry.right();

			double prevTime = prevEntry != null ? prevEntry.leftDouble() : 0;
			double curTime = entry.leftDouble();
			double timeDelta = curTime - prevTime;

			JsonArray keyFrameVector = element instanceof JsonArray array ? array : GsonHelper.getAsJsonArray(element.getAsJsonObject(), "vector");
			// TODO: Found Keyframe handler for molang
			MathValue rawXValue = MathParser.parseJson(keyFrameVector.get(0));
			MathValue rawYValue = MathParser.parseJson(keyFrameVector.get(1));
			MathValue rawZValue = MathParser.parseJson(keyFrameVector.get(2));
			MathValue xValue = compressMathValue(pIsForRotation && rawXValue instanceof Constant ? new Constant(Math.toRadians(-rawXValue.get())) : rawXValue);
			MathValue yValue = compressMathValue(pIsForRotation && rawYValue instanceof Constant ? new Constant(Math.toRadians(-rawYValue.get())) : rawYValue);
			MathValue zValue = compressMathValue(pIsForRotation && rawZValue instanceof Constant ? new Constant(Math.toRadians(rawZValue.get())) : rawZValue);

			JsonObject entryObj = element instanceof JsonObject obj ? obj : null;
			Easing easing = entryObj != null && entryObj.has("easing") ? Easing.fromJson(entryObj.get("easing")) : Easing.LINEAR;
			List<MathValue> easingArgs = entryObj != null && entryObj.has("easingArgs") ? JsonUtils.jsonArrayToList(GsonHelper.getAsJsonArray(entryObj, "easingArgs"), ele -> new Constant(ele.getAsDouble())) : new ObjectArrayList<>();

			xFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? xValue : xPrev, xValue, easing, easingArgs));
			yFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? yValue : yPrev, yValue, easing, easingArgs));
			zFrames.add(new KeyframeCache<>(timeDelta * 20, prevEntry == null ? zValue : zPrev, zValue, easing, easingArgs));

			xPrev = xValue;
			yPrev = yValue;
			zPrev = zValue;
			prevEntry = entry;
		}

		return new KeyframeStackCache<>(addSplineArgs(xFrames), addSplineArgs(yFrames), addSplineArgs(zFrames));
	}

	@NotNull
	private List<KeyframeCache<MathValue>> addSplineArgs(@NotNull List<KeyframeCache<MathValue>> pFrames) {
		if (pFrames.size() == 1) {
			KeyframeCache<MathValue> frame = pFrames.getFirst();

			if (frame.easing() != Easing.LINEAR) {
				pFrames.set(0, new KeyframeCache<>(frame.length(), frame.startValue(), frame.endValue()));

				return pFrames;
			}
		}

		for (int i = 0; i < pFrames.size(); i++) {
			KeyframeCache<MathValue> frame = pFrames.get(i);

			if (frame.easing() == Easing.CATMULLROM) {
				pFrames.set(i, new KeyframeCache<>(frame.length(), frame.startValue(), frame.endValue(), frame.easing(), ObjectArrayList.of(
						i == 0 ? frame.startValue() : pFrames.get(i - 1).endValue(),
						i + 1 >= pFrames.size() ? frame.endValue() : pFrames.get(i + 1).endValue())));
			}
		}

		return pFrames;
	}

	@NotNull
	private MathValue compressMathValue(@NotNull MathValue pInput) {
		if (COMPRESSION_CACHE == null || pInput.isMutable())
			return pInput;

		return COMPRESSION_CACHE.computeIfAbsent(pInput.get(), Constant::new);
	}

	private static double calculateAnimationLength(@NotNull BoneAnimationCache[] pBoneAnimationCaches) {
		double length = 0;

		for (BoneAnimationCache animation : pBoneAnimationCaches) {
			length = Math.max(length, animation.rotationKeyFrames().getLastKeyframeTime());
			length = Math.max(length, animation.positionKeyFrames().getLastKeyframeTime());
			length = Math.max(length, animation.scaleKeyFrames().getLastKeyframeTime());
		}

		return length == 0 ? Double.MAX_VALUE : length;
	}

	private static double readTimestamp(@NotNull String pTimestamp) {
		return NumberUtils.isCreatable(pTimestamp) ? Double.parseDouble(pTimestamp) : 0;
	}*/
}
