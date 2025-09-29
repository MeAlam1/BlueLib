/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.json.deserialize.animation.keyframe;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public sealed interface KeyframeData<T> permits KeyframeData.KeyframeArray, KeyframeData.KeyframeObject {

	record KeyframeArray<T>(
			@NotNull List<T> keyframes) implements KeyframeData<T> {}

	record KeyframeObject<T>(
			@NotNull Map<String, KeyframeData<T>> keyframes) implements KeyframeData<T> {}
}
