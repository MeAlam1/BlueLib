/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.animations.keyframe;

import software.bluelib.loader.animation.keyframe.event.data.CustomInstructionKeyframeData;
import software.bluelib.loader.animation.keyframe.event.data.ParticleKeyframeData;
import software.bluelib.loader.animation.keyframe.event.data.SoundKeyframeData;

public record KeyframeLibraryCache(SoundKeyframeData[] sounds, ParticleKeyframeData[] particles,
        CustomInstructionKeyframeData[] customInstructions) {}
