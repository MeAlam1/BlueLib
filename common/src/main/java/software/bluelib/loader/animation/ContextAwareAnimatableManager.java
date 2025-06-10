/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.animation;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import software.bluelib.loader.animatable.BlueAnimatable;
import software.bluelib.loader.animation.state.BoneSnapshot;
import software.bluelib.loader.constant.dataticket.DataTicket;

public abstract class ContextAwareAnimatableManager<T extends BlueAnimatable, C> extends AnimatableManager<T> {

    private final Map<C, AnimatableManager<T>> managers;

    public ContextAwareAnimatableManager(BlueAnimatable animatable) {
        super(animatable);

        this.managers = buildContextOptions(animatable);
    }

    protected abstract Map<C, AnimatableManager<T>> buildContextOptions(BlueAnimatable animatable);

    public abstract C getCurrentContext();

    public AnimatableManager<T> getManagerForContext(C context) {
        return this.managers.get(context);
    }

    public void addController(AnimationController controller) {
        getManagerForContext(getCurrentContext()).addController(controller);
    }

    public void removeController(String name) {
        getManagerForContext(getCurrentContext()).removeController(name);
    }

    public Map<String, AnimationController<T>> getAnimationControllers() {
        return getManagerForContext(getCurrentContext()).getAnimationControllers();
    }

    public Map<String, BoneSnapshot> getBoneSnapshotCollection() {
        return getManagerForContext(getCurrentContext()).getBoneSnapshotCollection();
    }

    public void clearSnapshotCache() {
        getManagerForContext(getCurrentContext()).clearSnapshotCache();
    }

    public double getLastUpdateTime() {
        return getManagerForContext(getCurrentContext()).getLastUpdateTime();
    }

    public void updatedAt(double updateTime) {
        getManagerForContext(getCurrentContext()).updatedAt(updateTime);
    }

    public double getFirstTickTime() {
        return getManagerForContext(getCurrentContext()).getFirstTickTime();
    }

    public void startedAt(double time) {
        getManagerForContext(getCurrentContext()).startedAt(time);
    }

    public boolean isFirstTick() {
        return getManagerForContext(getCurrentContext()).isFirstTick();
    }

    protected void finishFirstTick() {
        getManagerForContext(getCurrentContext()).finishFirstTick();
    }

    public void tryTriggerAnimation(String animName) {
        for (AnimatableManager<T> manager : this.managers.values()) {
            manager.tryTriggerAnimation(animName);
        }
    }

    public void tryTriggerAnimation(String controllerName, String animName) {
        for (AnimatableManager<T> manager : this.managers.values()) {
            manager.tryTriggerAnimation(controllerName, animName);
        }
    }

    public void stopTriggeredAnimation(@Nullable String animName) {
        for (AnimatableManager<T> manager : this.managers.values()) {
            manager.stopTriggeredAnimation(animName);
        }
    }

    public void stopTriggeredAnimation(String controllerName, @Nullable String animName) {
        for (AnimatableManager<T> manager : this.managers.values()) {
            manager.stopTriggeredAnimation(controllerName, animName);
        }
    }

    public <D> void setData(DataTicket<D> dataTicket, D data) {
        super.setData(dataTicket, data);
    }

    public <D> D getData(DataTicket<D> dataTicket) {
        return super.getData(dataTicket);
    }
}
