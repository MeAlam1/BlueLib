package software.bluelib.api.event;

import net.neoforged.fml.ModLoader;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

import java.util.List;

public class BlueLibEventProxy implements IEventProxy {
    @Override
    public void onModLoaded(ModMeta pModData) {
        ModLoader.postEvent(new ModLoadedEvent(pModData));
    }

    @Override
    public void onAllModsLoaded(List<ModMeta> pModData) {
        ModLoader.postEvent(new AllModsLoadedEvent(pModData));
    }

    @Override
    public void onVariantLoaded(String pEntityName, String pVariant) {
        ModLoader.postEvent(new VariantLoadedEvent(pEntityName, pVariant));
    }

    @Override
    public void onAllVariantsLoaded(String pEntityName) {
        ModLoader.postEvent(new AllVariantsLoadedEvent(pEntityName));
    }


}