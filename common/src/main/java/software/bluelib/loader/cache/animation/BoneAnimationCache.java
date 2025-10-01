/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.keyframe.KeyframeCache;
import software.bluelib.loader.json.deserialize.animation.keyframe.KeyframeDeserializer;

import java.util.List;

public record BoneAnimationCache(
		@Nullable List<MoLangValue> rotationArray,
		@Nullable KeyframeCache rotationObject,
		@Nullable List<MoLangValue> positionArray,
		@Nullable KeyframeDeserializer positionObject,
		@Nullable List<MoLangValue> scaleArray,
		@Nullable KeyframeDeserializer scaleObject) {
}
