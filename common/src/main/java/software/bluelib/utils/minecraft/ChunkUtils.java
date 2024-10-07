// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.utils.minecraft;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A {@code class} providing methods to interact with Minecraft chunks,
 * specifically for retrieving biome and tile entity information.
 * <p>
 * Key Methods:
 * <ul>
 *   <li>{@link #getBiomeOfChunk(Level, ChunkPos)} - Retrieves the {@link Biome} of the specified chunk.</li>
 *   <li>{@link #getBiomeRegistryNameOfChunk(Level, ChunkPos)} - Retrieves the biome registry name of the specified chunk.</li>
 *   <li>{@link #getBiomeSimpleNameOfChunk(Level, ChunkPos)} - Retrieves the simple name of the biome in the specified chunk.</li>
 *   <li>{@link #getChunkTileEntities(Level, ChunkPos)} - Retrieves the tile entities within the specified chunk.</li>
 *   <li>{@link #getChunkTileEntitiesRegistryNames(Level, ChunkPos)} - Retrieves the registry names of tile entities in the specified chunk.</li>
 *   <li>{@link #getChunkTileEntitiesSimpleNames(Level, ChunkPos)} - Retrieves the simple names of tile entities in the specified chunk.</li>
 *   <li>{@link #getChunkBlockCount(Level, ChunkPos)} - Counts the number of non-air blocks in the specified chunk.</li>
 * </ul>
 * </p>
 *
 * @author MeAlam
 * @since 1.0.0
 */
public class ChunkUtils {

    /**
     * A {@link Biome} that retrieves the {@link Biome} of the specified chunk.
     * <p>
     * Logs a success message if the biome is retrieved successfully,
     * and an error message if an exception occurs.
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The {@link Biome} associated with the specified chunk.
     * @throws RuntimeException if there is an error retrieving the biome.
     */
    public static Biome getBiomeOfChunk(Level pLevel, ChunkPos pChunkPos) {
        try {
            Biome biome = pLevel.getBiome(pChunkPos.getWorldPosition()).value();
            BaseLogger.log(BaseLogLevel.INFO, "Retrieved biome for chunk at position " + pChunkPos + ": " + biome);
            return biome;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving biome for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }


    /**
     * A {@link String} that retrieves the biome registry name of the specified chunk.
     * <p>
     * Example: "minecraft:plains", "minecraft:desert"
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The registry name of the chunk's biome as a {@link String}.
     * @throws RuntimeException if there is an error retrieving the biome registry name.
     */
    public static String getBiomeRegistryNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        ResourceLocation biomeKey = pLevel.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(pLevel.getBiome(pChunkPos.getWorldPosition()).value());

        if (biomeKey == null) {
            NullPointerException exception = new NullPointerException("Biome at chunk position " + pChunkPos + " is null");
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving biome registry name of chunk at " + pChunkPos, exception);
            return exception.getMessage();
        }

        BaseLogger.log(BaseLogLevel.SUCCESS, "Retrieved biome registry name for chunk at position " + pChunkPos + ": " + biomeKey);
        return biomeKey.toString();
    }


    /**
     * A {@link String} that retrieves the simple name of the biome in the specified chunk.
     * <p>
     * Example: "plains", "desert"
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The simple name of the chunk's biome.
     */
    public static String getBiomeSimpleNameOfChunk(Level pLevel, ChunkPos pChunkPos) {
        String registryName = getBiomeRegistryNameOfChunk(pLevel, pChunkPos);
        return registryName.contains(":") ? registryName.split(":")[1] : registryName;
    }

    /**
     * A {@link Collection<BlockEntity>} that retrieves the tile entities within the specified chunk.
     * <p>
     * Logs a success message with the number of tile entities retrieved,
     * and an error message if an exception occurs.
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return A collection of tile entities present in the specified chunk.
     * @throws RuntimeException if there is an error retrieving tile entities.
     */
    public static Collection<BlockEntity> getChunkTileEntities(Level pLevel, ChunkPos pChunkPos) {
        try {
            LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
            Collection<BlockEntity> tileEntities = chunk.getBlockEntities().values();
            BaseLogger.log(BaseLogLevel.INFO, "Retrieved " + tileEntities.size() + " tile entities for chunk at position " + pChunkPos);
            return tileEntities;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving tile entities for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }


    /**
     * A {@link String} that retrieves the registry names of tile entities in the specified chunk.
     * <p>
     * Example: "minecraft:chest, minecraft:furnace"
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return A comma-separated string of tile entity registry names in the chunk.
     * @throws RuntimeException if there is an error retrieving tile entity registry names.
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

            BaseLogger.log(BaseLogLevel.INFO, "Tile entities for chunk at position " + pChunkPos + ": " + registryNames);
            return registryNames;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving tile entity registry names for chunk at position " + pChunkPos, pException);
            throw pException;
        }
    }

    /**
     * A {@link String} that retrieves the simple names of tile entities in the specified chunk.
     * <p>
     * Example: "chest, furnace"
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
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
     * A {@link Integer} that counts the number of non-air blocks in the specified chunk.
     * <p>
     * Logs a success message with the block count,
     * and an error message if an exception occurs.
     * </p>
     *
     * @param pLevel    {@link Level} - The game world level.
     * @param pChunkPos {@link ChunkPos} - The position of the chunk.
     * @return The number of non-air blocks in the specified chunk.
     * @throws RuntimeException if there is an error counting blocks.
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

            BaseLogger.log(BaseLogLevel.INFO, "Non-air block count for chunk at position " + pChunkPos + ": " + blockCount);
            return blockCount;
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error counting blocks for chunk at position " + pChunkPos, pException);
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
