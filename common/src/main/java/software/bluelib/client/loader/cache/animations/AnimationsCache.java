/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.animations;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animation.Animation;

public record AnimationsCache(Map<String, Animation> animations) {

    @Nullable
    public Animation getAnimation(String pName) {
        return animations.get(pName);
    }
}
