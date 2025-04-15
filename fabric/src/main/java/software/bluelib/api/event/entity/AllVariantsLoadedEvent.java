package software.bluelib.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.mod.ModMeta;

public final class AllVariantsLoadedEvent {
    public static final Event<AllVariantsLoadedEventListener> EVENT = EventFactory.createArrayBacked(AllVariantsLoadedEventListener.class,
            (listeners) -> (pEntityName) -> {
                for (AllVariantsLoadedEventListener listener : listeners) {
                    listener.onAllVariantsLoaded(pEntityName);
                }
            });

    @FunctionalInterface
    public interface AllVariantsLoadedEventListener {
        void onAllVariantsLoaded(@NotNull String pEntityName);
    }
}
