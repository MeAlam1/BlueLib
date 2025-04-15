package software.bluelib.api.event;

import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

import java.util.List;

public class BlueLibEventProxy implements IEventProxy {
    @Override
    public void onModLoaded(ModMeta pModData) {
        ModLoadedEvent.EVENT.invoker().onModLoaded(pModData);
    }

    @Override
    public void onAllModsLoaded(List<ModMeta> pModData) {
        AllModsLoadedEvent.EVENT.invoker().onAllModsLoaded(pModData);
    }

    @Override
    public void onVariantLoaded(String pEntityName, String pVariant) {
        VariantLoadedEvent.EVENT.invoker().onVariantLoaded(pEntityName, pVariant);
    }

    @Override
    public void onAllVariantsLoaded(String pEntityName) {
        AllVariantsLoadedEvent.EVENT.invoker().onAllVariantsLoaded(pEntityName);
    }


}