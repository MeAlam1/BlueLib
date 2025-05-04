// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.mod;

import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class AllModsLoadedEvent {

    public static final Event<AllModsLoadedEventListener> EVENT = EventFactory.createArrayBacked(AllModsLoadedEventListener.class,
            (listeners) -> (pModData) -> {
                for (AllModsLoadedEventListener listener : listeners) {
                    listener.onAllModsLoaded(pModData);
                }
            });

    @FunctionalInterface
    public interface AllModsLoadedEventListener {

        void onAllModsLoaded(List<ModMeta> pModData);
    }
}
