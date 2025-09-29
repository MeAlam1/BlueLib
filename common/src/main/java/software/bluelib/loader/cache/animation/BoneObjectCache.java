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
import software.bluelib.loader.geckolib.animations.KeyframeCache;

public record BoneObjectCache(
		@NotNull String boneName,// TODO: Since the Bones are stored in a Map by their names, This field is redundant and can be removed later.
		@Nullable KeyframeCache rotation,
		@Nullable KeyframeCache position,
		@Nullable KeyframeCache scale) implements BoneAnimationCache {}
