/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.value.MoLangValue;

import java.util.List;

public record BoneAnimationCache(
		@NotNull String boneName,
		@Nullable List<MoLangValue> rotation,
		@Nullable List<MoLangValue> position,
		@Nullable List<MoLangValue> scale) {
}
