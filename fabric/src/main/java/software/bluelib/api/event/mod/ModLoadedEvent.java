// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.mod;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ModLoadedEvent {

    public static final Event<ModLoadedEventListener> EVENT = EventFactory.createArrayBacked(ModLoadedEventListener.class,
            (listeners) -> (pModData) -> {
                for (ModLoadedEventListener listener : listeners) {
                    listener.onModLoaded(pModData);
                }
            });

    @FunctionalInterface
    public interface ModLoadedEventListener {

        void onModLoaded(ModMeta pModData);
    }
}
