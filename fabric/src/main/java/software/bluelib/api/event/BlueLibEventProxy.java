// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event;

import java.util.List;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

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
    public boolean variantLoadedPre(String pEntityName, String pVariant) {
        return VariantLoadedEvent.ALLOW_VARIANT_TO_LOAD.invoker().allowVariantToLoad(pEntityName, pVariant);
    }

    @Override
    public void variantLoadedPost(String pEntityName, String pVariant) {
        VariantLoadedEvent.POST.invoker().onVariantLoaded(pEntityName, pVariant);
    }

    @Override
    public boolean allVariantsLoadedPre(String pEntityName) {
        return AllVariantsLoadedEvent.ALLOW_ALL_VARIANTS_TO_LOAD.invoker().allowAllVariantsToLoad(pEntityName);
    }

    @Override
    public void allVariantsLoadedPost(String pEntityName) {
        AllVariantsLoadedEvent.POST.invoker().onAllVariantsLoaded(pEntityName);
    }
}
