package software.bluelib.api.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.mod.ModMeta;

public final class VariantLoadedEvent {
    public static final Event<VariantLoadedEventListener> EVENT = EventFactory.createArrayBacked(VariantLoadedEventListener.class,
            (listeners) -> (pEntityName, pVariantName) -> {
                for (VariantLoadedEventListener listener : listeners) {
                    listener.onVariantLoaded(pEntityName, pVariantName);
                }
            });

    @FunctionalInterface
    public interface VariantLoadedEventListener {
        void onVariantLoaded(@NotNull String pEntityName, @NotNull String pVariant);
    }
}
