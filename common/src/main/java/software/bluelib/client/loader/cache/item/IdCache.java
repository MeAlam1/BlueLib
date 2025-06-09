/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.loader.cache.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public final class IdCache extends SavedData {

    private static final Factory<IdCache> FACTORY = new Factory<>(IdCache::new, IdCache::new, null);
    private static final String DATA_KEY = "bluelib_id_cache";
    private long lastId;

    private IdCache() {}

    private IdCache(CompoundTag pTag, HolderLookup.Provider pRegistryLookup) {
        this.lastId = pTag.getLong("last_id");
    }

    public static long getFreeId(ServerLevel pLevel) {
        return getCache(pLevel).getNextId();
    }

    private long getNextId() {
        setDirty();

        return ++this.lastId;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag pTag, HolderLookup.@NotNull Provider pRegistryLookup) {
        pTag.putLong("last_id", this.lastId);

        return pTag;
    }

    private static IdCache getCache(ServerLevel pLevel) {
        return pLevel.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_KEY);
    }
}
