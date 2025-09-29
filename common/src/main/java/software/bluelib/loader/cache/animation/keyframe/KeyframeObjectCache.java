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

public record KeyframeObjectCache(
		@Nullable MoLangValue pre,
		@Nullable MoLangValue post,
		@Nullable String easing,
		@Nullable List<MoLangValue> easingArgs) implements KeyframeCacheData {}
