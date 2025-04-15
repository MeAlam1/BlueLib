package software.bluelib.api.event.entity;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VariantLoadedEvent extends Event implements IModBusEvent {

    String entityName;
    String variant;

    public VariantLoadedEvent(@NotNull String pEntityName, @NotNull String pVariant) {
        super();
        this.entityName = pEntityName;
        this.variant = pVariant;
    }

    @NotNull
    public String getEntity() {
        return entityName;
    }

    @NotNull
    public String getVariant() {
        return variant;
    }
    
    

}
