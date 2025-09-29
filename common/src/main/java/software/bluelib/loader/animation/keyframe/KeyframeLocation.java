/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.geckolib.animations.KeyframeCache;

public record KeyframeLocation<T extends KeyframeCache<?>>(@NotNull T keyframe, double startTick) {}
