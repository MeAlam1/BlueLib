// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.minecraft;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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

    public static String getBiomeRegistryNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        ResourceLocation biomeKey = pLevel.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getKey(pLevel.getBiome(pChunkPos.getWorldPosition()).value());

        if (biomeKey == null) {
            NullPointerException exception = new NullPointerException("Biome at chunk position " + pChunkPos + " is null");
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving biome registry name of chunk at " + pChunkPos, exception, true);
            return exception.getMessage();
        }
        return biomeKey.toString();
    }

    public static String getBiomeSimpleNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        String registryName = getBiomeRegistryNameOfChunk(pLevel, pChunkPos);
        return registryName.contains(":") ? registryName.split(":")[1] : registryName;
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

    public static String getChunkTileEntitiesRegistryNames(Level pLevel, ChunkPos pChunkPos) {
        try {
            Collection<BlockEntity> blockEntities = getChunkTileEntities(pLevel, pChunkPos);

            return blockEntities.stream()
                    .map(blockEntity -> {
                        ResourceLocation key = pLevel.registryAccess()
                                .lookupOrThrow(Registries.BLOCK_ENTITY_TYPE)
                                .getKey(blockEntity.getType());

                        return key != null ? key.toString() : "unknown";
                    })
                    .collect(Collectors.joining(", "));
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving tile entity registry names for chunk at position " + pChunkPos, pException, true);
            throw pException;
        }
    }

    public static String getChunkTileEntitiesSimpleNames(Level pLevel, ChunkPos pChunkPos) {
        String registryNames = getChunkTileEntitiesRegistryNames(pLevel, pChunkPos);

        return Arrays.stream(registryNames.split(", "))
                .map(fullName -> fullName.contains(":") ? fullName.split(":")[1] : fullName)
                .collect(Collectors.joining(", "));
    }

    public static int getChunkBlockCount(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            int blockCount = 0;

            for (int x = 0; x < 16; x++) {
                for (int y = pLevel.getMinY(); y < pLevel.getHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos worldPos = new BlockPos(pChunkPos.getMinBlockX() + x, y, pChunkPos.getMinBlockZ() + z);
                        if (!chunk.getBlockState(worldPos).isAir()) {
                            blockCount++;
                        }
                    }
                }
            }
            return blockCount;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error counting blocks for chunk at position " + pChunkPos, pException, true);
            throw pException;
        }
    }

    /* FIXME: This method is not working as expected. It is not returning correctly.
    public static boolean isChunkLoaded(final LevelAccessor pWorld, final int pX, final int pZ) {
    try {
    boolean isLoaded = pWorld.getChunk(pX, pZ, ChunkStatus.FULL, false) != null;
    return isLoaded;
    } catch (Exception e) {
    return false;
    }
    }
    */
}
