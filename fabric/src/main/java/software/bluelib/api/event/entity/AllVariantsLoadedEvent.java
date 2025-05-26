/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

public final class AllVariantsLoadedEvent {

    public static final Event<AllVariantsLoadedPre> ALLOW_ALL_VARIANTS_TO_LOAD = EventFactory.createArrayBacked(AllVariantsLoadedPre.class,
            (listeners) -> (pEntityName) -> {
                for (AllVariantsLoadedPre listener : listeners) {
                    if (!listener.allowAllVariantsToLoad(pEntityName)) {
                        return false;
                    }
                }
                return true;
            });

    @FunctionalInterface
    public interface AllVariantsLoadedPre {

        boolean allowAllVariantsToLoad(@NotNull String pEntityName);
    }

    public static final Event<AllVariantsLoadedPost> POST = EventFactory.createArrayBacked(AllVariantsLoadedPost.class,
            (listeners) -> (pEntityName) -> {
                for (AllVariantsLoadedPost listener : listeners) {
                    listener.onAllVariantsLoaded(pEntityName);
                }
            });

    @FunctionalInterface
    public interface AllVariantsLoadedPost {

        void onAllVariantsLoaded(@NotNull String pEntityName);
    }
}
