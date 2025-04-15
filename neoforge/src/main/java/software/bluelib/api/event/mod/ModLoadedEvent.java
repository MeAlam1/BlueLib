package software.bluelib.api.event.mod;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

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

    @NotNull
    public String getNamespace() {
        return modData.namespace();
    }

    public Map<String, Object> getProperties() {
        return modData.properties();
    }
    
    @Nullable
    public Optional<URL> getUpdateURL() {
        return modData.updateURL();
    }
    
    @Nullable
    public Optional<URL> getModURL() {
        return modData.modURL();
    }

    @Nullable
    public Optional<String> getLogoFile() {
        return modData.logoFile();
    }
}
