// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import software.bluelib.utils.logging.BaseLogger;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A {@code ChunkUtils} class providing utility methods for interacting with Minecraft chunks,
 * specifically focusing on retrieving biome and tile entity information.
 *
 * Key Methods:
 * <ul>
 *   <li>{@link #getBiomeOfChunk(Level, ChunkPos)} - Returns the {@link Biome} of the chunk.</li>
 *   <li>{@link #getBiomeRegistryNameOfChunk(Level, ChunkPos)} - Retrieves the biome registry name of the chunk.</li>
 *   <li>{@link #getChunkTileEntities(Level, ChunkPos)} - Gets the tile entities within the chunk.</li>
 * </ul>
 * @author MeAlam
 * @since 1.0.0
 */
public class ChunkUtils {

    /**
     * A {@link Biome} that retrieves the biome of a chunk.
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The {@link Biome} associated with the specified chunk.
     */
    public static Biome getBiomeOfChunk(Level pLevel, ChunkPos pChunkPos) {
        try {
            Biome biome = pLevel.getBiome(pChunkPos.getWorldPosition()).value();
            BaseLogger.bluelibLogSuccess("Retrieved biome for chunk at position " + pChunkPos + ": " + biome);
            return biome;
        } catch (Exception pException) {
            BaseLogger.logError("Error retrieving biome for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }


    /**
     * A {@link String} representing the registry name of the biome of a chunk.
     * Example: "minecraft:plains", "minecraft:desert"
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The registry name of the chunk's biome as a {@link String}.
     */
    public static String getBiomeRegistryNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        ResourceLocation biomeKey = pLevel.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(pLevel.getBiome(pChunkPos.getWorldPosition()).value());

        if (biomeKey == null) {
            NullPointerException exception = new NullPointerException("Biome at chunk position " + pChunkPos + " is null");
            BaseLogger.logError("Error retrieving biome registry name of chunk at " + pChunkPos, exception);
            return "Biome at chunk position " + pChunkPos + " is null";
        }

        BaseLogger.bluelibLogSuccess("Retrieved biome registry name for chunk at position " + pChunkPos + ": " + biomeKey);
        return biomeKey.toString();
    }


    /**
     * A {@link String} that retrieves the simple name of the biome in the chunk.
     * Example: "plains", "desert"
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The simple name of the chunk's biome.
     */
    public static String getBiomeSimpleNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        String registryName = getBiomeRegistryNameOfChunk(pLevel, pChunkPos);
        return registryName.contains(":") ? registryName.split(":")[1] : registryName;
    }

    /**
     * A {@link Collection} of {@link BlockEntity} that retrieves the tile entities in a chunk.
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return A collection of tile entities present in the specified chunk.
     */
    public static Collection<BlockEntity> getChunkTileEntities(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            Collection<BlockEntity> tileEntities = chunk.getBlockEntities().values();
            BaseLogger.bluelibLogSuccess("Retrieved " + tileEntities.size() + " tile entities for chunk at position " + pChunkPos);
            return tileEntities;
        } catch (Exception pException) {
            BaseLogger.logError("Error retrieving tile entities for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }


    /**
     * A {@link String} that retrieves the registry names of tile entities in a chunk.
     * Example: "minecraft:chest, minecraft:furnace"
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return A comma-separated string of tile entity registry names in the chunk.
     */
    public static String getChunkTileEntitiesRegistryNames(Level pLevel, ChunkPos pChunkPos) {
        try {
            Collection<BlockEntity> blockEntities = getChunkTileEntities(pLevel, pChunkPos);
            String registryNames = blockEntities.stream()
                    .map(blockEntity -> {
                        ResourceLocation key = pLevel.registryAccess()
                                .registryOrThrow(Registries.BLOCK_ENTITY_TYPE)
                                .getKey(blockEntity.getType());

                        return key != null ? key.toString() : "unknown";
                    })
                    .collect(Collectors.joining(", "));

            BaseLogger.bluelibLogSuccess("Tile entities for chunk at position " + pChunkPos + ": " + registryNames);
            return registryNames;
        } catch (Exception pException) {
            BaseLogger.logError("Error retrieving tile entity registry names for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }

    /**
     * A {@link String} that retrieves the simple names of tile entities in a chunk.
     * Example: "chest, furnace"
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return A comma-separated string of tile entity simple names in the chunk.
     */
    public static String getChunkTileEntitiesSimpleNames(Level pLevel, ChunkPos pChunkPos) {
        String registryNames = getChunkTileEntitiesRegistryNames(pLevel, pChunkPos);

        return Arrays.stream(registryNames.split(", "))
                .map(fullName -> fullName.contains(":") ? fullName.split(":")[1] : fullName)
                .collect(Collectors.joining(", "));
    }

    /**
     * An {@code int} that counts the number of non-air blocks in the chunk.
     *
     * @param pLevel {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The number of non-air blocks in the specified chunk.
     */
    public static int getChunkBlockCount(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            int blockCount = 0;

            for (int x = 0; x < 16; x++) {
                for (int y = pLevel.getMinBuildHeight(); y < pLevel.getHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos worldPos = new BlockPos(pChunkPos.getMinBlockX() + x, y, pChunkPos.getMinBlockZ() + z);
                        if (!chunk.getBlockState(worldPos).isAir()) {
                            blockCount++;
                        }
                    }
                }
            }

            BaseLogger.bluelibLogSuccess("Non-air block count for chunk at position " + pChunkPos + ": " + blockCount);
            return blockCount;
        } catch (Exception pException) {
            BaseLogger.logError("Error counting blocks for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }

    /** FIXME: This method is not working as expected. It is not returning correctly.
        public static boolean isChunkLoaded(final LevelAccessor pWorld, final int pX, final int pZ) {
            try {
                boolean isLoaded = pWorld.getChunk(pX, pZ, ChunkStatus.FULL, false) != null;
                BaseLogger.bluelibLogSuccess("Chunk at (" + pX + ", " + pZ + ") is loaded: " + isLoaded);
                return isLoaded;
            } catch (Exception e) {
                BaseLogger.logError("Error checking if chunk at (" + pX + ", " + pZ + ") is loaded", e);
                return false;
            }
        }
     */


}
