/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import software.bluelib.client.loader.cache.animations.AnimationCache;

public final class RawAnimation {

    private final List<Stage> animationList = new ObjectArrayList<>();

    // Private constructor to force usage of factory for logical operations
    private RawAnimation() {}

    public static RawAnimation begin() {
        return new RawAnimation();
    }

    public RawAnimation thenPlay(String animationName) {
        return then(animationName, AnimationCache.LoopType.DEFAULT);
    }

    public RawAnimation thenLoop(String animationName) {
        return then(animationName, AnimationCache.LoopType.LOOP);
    }

    public RawAnimation thenWait(int ticks) {
        this.animationList.add(new Stage(Stage.WAIT, AnimationCache.LoopType.PLAY_ONCE, ticks));

        return this;
    }

    public RawAnimation thenPlayAndHold(String animation) {
        return then(animation, AnimationCache.LoopType.HOLD_ON_LAST_FRAME);
    }

    public RawAnimation thenPlayXTimes(String animationName, int playCount) {
        for (int i = 0; i < playCount; i++) {
            then(animationName, i == playCount - 1 ? AnimationCache.LoopType.DEFAULT : AnimationCache.LoopType.PLAY_ONCE);
        }

        return this;
    }

    public RawAnimation then(String animationName, AnimationCache.LoopType loopType) {
        this.animationList.add(new Stage(animationName, loopType));

        return this;
    }

    public List<Stage> getAnimationStages() {
        return this.animationList;
    }

    public static RawAnimation copyOf(RawAnimation other) {
        RawAnimation newInstance = RawAnimation.begin();

        newInstance.animationList.addAll(other.animationList);

        return newInstance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.animationList);
    }

    public record Stage(String animationName, AnimationCache.LoopType loopType, int additionalTicks) {

        public static final String WAIT = "internal.wait";

        public Stage(String animationName, AnimationCache.LoopType loopType) {
            this(animationName, loopType, 0);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            return hashCode() == obj.hashCode();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.animationName, this.loopType);
        }
    }
}
