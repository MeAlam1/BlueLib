/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.minecraft;

import java.util.Collection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import software.bluelib.BlueLibCommon;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class ChunkUtils {

    private ChunkUtils() {}

    public static Biome getBiomeOfChunk(Level pLevel, ChunkPos pChunkPos) {
        try {
            return pLevel.getBiome(pChunkPos.getWorldPosition()).value();
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("chunk.biome.error"), pException, true);
            throw pException;
        }
    }

    public static Collection<BlockEntity> getChunkTileEntities(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            return chunk.getBlockEntities().values();
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, BlueLibCommon.Translation.log("chunk.tile.error"), pException, true);
            throw pException;
        }
    }
}
