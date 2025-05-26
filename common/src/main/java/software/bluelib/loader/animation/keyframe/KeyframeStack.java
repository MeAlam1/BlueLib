/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

public record KeyframeStack<T extends Keyframe<?>>(List<T> xKeyframes, List<T> yKeyframes, List<T> zKeyframes) {

    public KeyframeStack() {
        this(new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>());
    }

    public static <F extends Keyframe<?>> KeyframeStack<F> from(KeyframeStack<F> otherStack) {
        return new KeyframeStack<>(otherStack.xKeyframes, otherStack.yKeyframes, otherStack.zKeyframes);
    }

    public double getLastKeyframeTime() {
        double xTime = 0;
        double yTime = 0;
        double zTime = 0;

        for (T frame : xKeyframes()) {
            xTime += frame.length();
        }

        for (T frame : yKeyframes()) {
            yTime += frame.length();
        }

        for (T frame : zKeyframes()) {
            zTime += frame.length();
        }

        return Math.max(xTime, Math.max(yTime, zTime));
    }
}
