// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event;

import java.util.List;
import net.neoforged.fml.ModLoader;
import software.bluelib.api.event.entity.AllVariantsLoadedEvent;
import software.bluelib.api.event.entity.VariantLoadedEvent;
import software.bluelib.api.event.mod.AllModsLoadedEvent;
import software.bluelib.api.event.mod.ModLoadedEvent;
import software.bluelib.api.event.mod.ModMeta;

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
    public boolean variantLoadedPre(String pEntityName, String pVariant) {
        VariantLoadedEvent.Pre event = new VariantLoadedEvent.Pre(pEntityName, pVariant);
        return ModLoader.postEventWithReturn(event).isCanceled();
    }

    @Override
    public void variantLoadedPost(String pEntityName, String pVariant) {
        ModLoader.postEvent(new VariantLoadedEvent.Post(pEntityName, pVariant));
    }

    @Override
    public boolean allVariantsLoadedPre(String pEntityName) {
        AllVariantsLoadedEvent.Pre event = new AllVariantsLoadedEvent.Pre(pEntityName);
        return ModLoader.postEventWithReturn(event).isCanceled();
    }

    @Override
    public void allVariantsLoadedPost(String pEntityName) {
        ModLoader.postEvent(new AllVariantsLoadedEvent.Post(pEntityName));
    }
}
