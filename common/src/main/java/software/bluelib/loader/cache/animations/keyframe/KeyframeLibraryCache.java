/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animations.keyframe;

import org.jetbrains.annotations.NotNull;
import software.bluelib.oldLoader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.oldLoader.animation.keyframe.event.data.SoundKeyframeData;

public record KeyframeLibraryCache(
		@NotNull SoundKeyframeData[] sounds,
		@NotNull ParticleKeyframeData[] particles,
		@NotNull CustomInstructionKeyframeData[] customInstructions) {}
