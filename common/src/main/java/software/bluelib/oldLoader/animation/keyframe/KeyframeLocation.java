/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.oldLoader.animation.keyframe;

import software.bluelib.loader.cache.animations.keyframe.KeyframeCache;

public record KeyframeLocation<T extends KeyframeCache<?>>(T keyframe, double startTick) {}
