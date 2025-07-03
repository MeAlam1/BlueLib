/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animations;

import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record AnimationLibraryCache(Map<String, AnimationCache> animations) {

	@Nullable
	public AnimationCache getAnimation(String pName) {
		return animations.get(pName);
	}
}
