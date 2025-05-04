// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.event;

import java.util.List;
import software.bluelib.api.event.mod.ModMeta;

public interface IEventProxy {

    void onModLoaded(ModMeta pModData);

    void onAllModsLoaded(List<ModMeta> pModData);

    boolean variantLoadedPre(String pEntityName, String pVariant);

    void variantLoadedPost(String pEntityName, String pVariant);

    boolean allVariantsLoadedPre(String pEntityName);

    void allVariantsLoadedPost(String pEntityName);
}
