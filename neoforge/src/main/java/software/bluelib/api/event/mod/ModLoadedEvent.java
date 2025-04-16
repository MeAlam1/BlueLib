package software.bluelib.api.event.mod;

import java.util.Optional;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ModLoadedEvent extends Event implements IModBusEvent {

    ModMeta modData;

    public ModLoadedEvent(@NotNull ModMeta pModData) {
        super();
        this.modData = pModData;
    }

    @NotNull
    public ModMeta getModData() {
        return modData;
    }

    @NotNull
    public String getModId() {
        return modData.modId();
    }

    @NotNull
    public String getDisplayName() {
        return modData.displayName();
    }

    @NotNull
    public String getVersion() {
        return modData.version();
    }

    public String getDescription() {
        return modData.description();
    }

    @Nullable
    public Optional<String> getLogoFile() {
        return modData.logoFile();
    }
}
