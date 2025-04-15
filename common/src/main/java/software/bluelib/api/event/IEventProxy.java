package software.bluelib.api.event;

import software.bluelib.api.event.mod.ModMeta;

import java.util.List;

public interface IEventProxy {
    void onModLoaded(ModMeta pModData);

    void onAllModsLoaded(List<ModMeta> pModData);

    void onVariantLoaded(String pEntityName, String pVariant);

    void onAllVariantsLoaded(String pEntityName);
}
