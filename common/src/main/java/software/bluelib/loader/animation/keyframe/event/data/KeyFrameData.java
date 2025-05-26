/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation.keyframe.event.data;

import java.util.Objects;

public abstract class KeyFrameData {

    private final double startTick;

    public KeyFrameData(double startTick) {
        this.startTick = startTick;
    }

    public double getStartTick() {
        return this.startTick;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.startTick);
    }
}
