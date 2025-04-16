// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.minecraft;

import java.util.Collection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

@SuppressWarnings("unused")
public class ChunkUtils {

    private ChunkUtils() {}

    public static Biome getBiomeOfChunk(Level pLevel, ChunkPos pChunkPos) {
        try {
            return pLevel.getBiome(pChunkPos.getWorldPosition()).value();
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving biome for chunk at position " + pChunkPos, pException, true);
            throw pException;
        }
    }

    public static Collection<BlockEntity> getChunkTileEntities(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            return chunk.getBlockEntities().values();
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving tile entities for chunk at position " + pChunkPos, pException, true);
            throw pException;
        }
    }
}
