/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;

public record BoneArrayCache(
		@NotNull String boneName, // TODO: Since the Bones are stored in a Map by their names, This field is redundant and can be removed later.
		@Nullable List<MoLangValue> rotation,
		@Nullable List<MoLangValue> position,
		@Nullable List<MoLangValue> scale) implements BoneAnimationCache {}
