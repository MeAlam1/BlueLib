/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
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
