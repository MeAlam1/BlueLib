// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.entity;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class AllVariantsLoadedEvent extends Event implements IModBusEvent {

    String entityName;

    public AllVariantsLoadedEvent(@NotNull String pEntityName) {
        super();
        this.entityName = pEntityName;
    }

    @NotNull
    public String getEntity() {
        return entityName;
    }

    public static class Pre extends AllVariantsLoadedEvent implements ICancellableEvent {

        public Pre(@NotNull String pEntityName) {
            super(pEntityName);
        }
    }

    public static class Post extends AllVariantsLoadedEvent {

        public Post(@NotNull String pEntityName) {
            super(pEntityName);
        }
    }
}
