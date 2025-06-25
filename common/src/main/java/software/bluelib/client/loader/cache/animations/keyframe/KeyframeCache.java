/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.animations.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import software.bluelib.loader.animation.EasingType;
import software.bluelib.loader.loading.math.MathValue;

public record KeyframeCache<T extends MathValue>(double length, T startValue, T endValue, EasingType easingType, List<T> easingArgs) {

    public KeyframeCache(double pLength, T pStartValue, T pEndValue) {
        this(pLength, pStartValue, pEndValue, EasingType.LINEAR);
    }

    public KeyframeCache(double pLength, T pStartValue, T pEndValue, EasingType pEasingType) {
        this(pLength, pStartValue, pEndValue, pEasingType, new ObjectArrayList<>(0));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.length, this.startValue, this.endValue, this.easingType, this.easingArgs);
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj)
            return true;

        if (pObj == null || getClass() != pObj.getClass())
            return false;

        return hashCode() == pObj.hashCode();
    }
}
