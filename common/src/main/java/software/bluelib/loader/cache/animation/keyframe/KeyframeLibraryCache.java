/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache.animation.keyframe;

import org.jetbrains.annotations.NotNull;
import software.bluelib.loader.geckolib.animations.CustomInstructionKeyframeData;
import software.bluelib.loader.geckolib.animations.ParticleKeyframeData;
import software.bluelib.loader.geckolib.animations.SoundKeyframeData;

public record KeyframeLibraryCache(
		@NotNull SoundKeyframeData[] sounds,
		@NotNull ParticleKeyframeData[] particles,
		@NotNull CustomInstructionKeyframeData[] customInstructions) {}
