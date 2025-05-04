// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

public final class VariantLoadedEvent {

    public static final Event<VariantLoadedPre> ALLOW_VARIANT_TO_LOAD = EventFactory.createArrayBacked(VariantLoadedPre.class,
            (listeners) -> (pEntityName, pVariantName) -> {
                for (VariantLoadedPre listener : listeners) {
                    if (!listener.allowVariantToLoad(pEntityName, pVariantName)) {
                        return false;
                    }
                }
                return true;
            });

    @FunctionalInterface
    public interface VariantLoadedPre {

        boolean allowVariantToLoad(@NotNull String pEntityName, @NotNull String pVariant);
    }

    public static final Event<VariantLoadedPost> POST = EventFactory.createArrayBacked(VariantLoadedPost.class,
            (listeners) -> (pEntityName, pVariantName) -> {
                for (VariantLoadedPost listener : listeners) {
                    listener.onVariantLoaded(pEntityName, pVariantName);
                }
            });

    @FunctionalInterface
    public interface VariantLoadedPost {

        void onVariantLoaded(@NotNull String pEntityName, @NotNull String pVariant);
    }
}
