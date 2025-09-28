/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record AnimationFileCache(@NotNull String formatVersion, @NotNull AnimationsCache animations) {

	public @NotNull AnimationsCache getAllAnimations() {
		return animations;
	}

	@Nullable
	public AnimationCache getAnimation(@NotNull String pName) {
		return animations.getAnimation(pName);
	}
}
