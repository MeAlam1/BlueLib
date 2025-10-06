/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation.keyframe;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;
import software.bluelib.loader.cache.animation.EasingCache;

public record KeyframeCacheData(
		@Nullable List<MoLangValue> arrayData,
		@Nullable List<MoLangValue> pre,
		@Nullable List<MoLangValue> post,
		@Nullable EasingCache easing,
		@Nullable List<MoLangValue> easingArgs) {}
