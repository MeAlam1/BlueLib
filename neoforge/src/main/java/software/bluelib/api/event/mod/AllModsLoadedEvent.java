// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event.mod;

import java.util.List;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class AllModsLoadedEvent extends Event implements IModBusEvent {

    List<ModMeta> modData;

    public AllModsLoadedEvent(@Nullable List<ModMeta> pModData) {
        super();
        this.modData = pModData;
    }

    @Nullable
    public List<ModMeta> getAllModsData() {
        return modData;
    }

    @Nullable
    public ModMeta getModMetaById(String pModId) {
        return modData.stream()
                .filter(modMeta -> modMeta.modId().equals(pModId))
                .findFirst()
                .orElse(null);
    }
}
