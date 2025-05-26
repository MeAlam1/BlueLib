/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.cache;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class AnimatableIdCache extends SavedData {

    private static final Factory<AnimatableIdCache> FACTORY = new Factory<>(AnimatableIdCache::new, AnimatableIdCache::new, null);
    private static final String DATA_KEY = "geckolib_id_cache";
    private long lastId;

    private AnimatableIdCache() {}

    private AnimatableIdCache(CompoundTag tag, HolderLookup.Provider registryLookup) {
        this.lastId = tag.getLong("last_id");
    }

    public static long getFreeId(ServerLevel level) {
        return getCache(level).getNextId();
    }

    private long getNextId() {
        setDirty();

        return ++this.lastId;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registryLookup) {
        tag.putLong("last_id", this.lastId);

        return tag;
    }

    private static AnimatableIdCache getCache(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_KEY);
    }
}
