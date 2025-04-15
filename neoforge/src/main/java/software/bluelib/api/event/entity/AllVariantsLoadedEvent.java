package software.bluelib.api.event.entity;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AllVariantsLoadedEvent extends Event implements IModBusEvent {

    String entityName;

    public AllVariantsLoadedEvent(@NotNull String pEntityName) {
        super();
        this.entityName = pEntityName;
    }

    @NotNull
    public String getEntity() {
        return entityName;
    }
}
