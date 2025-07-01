/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.event.mod.ModMeta;

public interface IEventProxy {

    void onModLoaded(@NotNull ModMeta pModData);

    void onAllModsLoaded(@NotNull List<ModMeta> pModData);

    @NotNull
    Boolean variantLoadedPre(@NotNull String pEntityName, @NotNull String pVariant);

    void variantLoadedPost(@NotNull String pEntityName, @NotNull String pVariant);

    @NotNull
    Boolean allVariantsLoadedPre(@NotNull String pEntityName);

    void allVariantsLoadedPost(@NotNull String pEntityName);
}
