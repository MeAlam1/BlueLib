package software.bluelib.api.event.mod;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.List;

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
