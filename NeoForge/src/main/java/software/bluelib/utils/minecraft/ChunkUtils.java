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

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
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
        return Objects.requireNonNull(pLevel.getBiome(pChunkPos.getWorldPosition()).value());
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
        return Objects.requireNonNull(pLevel.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(pLevel.getBiome(pChunkPos.getWorldPosition()).value())).toString();
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
        LevelChunk chunk = pLevel.getChunk(pChunkPos.x, pChunkPos.z);
        return chunk.getBlockEntities().values();
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
        Collection<BlockEntity> blockEntities = getChunkTileEntities(pLevel, pChunkPos);

        return blockEntities.stream()
                .map(blockEntity -> {
                    ResourceLocation key = pLevel.registryAccess()
                            .registryOrThrow(Registries.BLOCK_ENTITY_TYPE)
                            .getKey(blockEntity.getType());

                    return key != null ? key.toString() : "unknown";
                })
                .collect(Collectors.joining(", "));
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
        return blockCount;
    }

//FIXME: This method is not working as expected, Only returns true in SpawnChunks in stead of all loaded chunks.
    /**
     public static boolean isChunkLoaded(final LevelAccessor world, final int x, final int z) {
     if (world.getChunkSource() instanceof ServerChunkCache chunkCache) {
     final CompletableFuture<ChunkResult<ChunkAccess>> future = chunkCache.getChunkFuture(x, z, ChunkStatus.FULL, false);

     try {
     boolean isFutureDone = future.isDone();
     ChunkResult<ChunkAccess> chunkResult = future.getNow(ChunkHolder.UNLOADED_CHUNK);

     if (chunkResult instanceof ChunkResult.Fail) {
     System.out.println("Chunk at (" + x + ", " + z + ") failed to load with error: " + chunkResult);
     return false;
     }

     boolean isChunkLoaded = chunkResult != ChunkHolder.UNLOADED_CHUNK && chunkResult.isSuccess();
     System.out.println("Chunk at (" + x + ", " + z + ") future done: " + isFutureDone);
     System.out.println("Chunk at (" + x + ", " + z + ") loaded: " + isChunkLoaded);

     return isChunkLoaded;
     } catch (Exception e) {
     e.printStackTrace();
     System.out.println("Error checking chunk loading status at (" + x + ", " + z + "): " + e.getMessage());
     return false;
     }
     }
     boolean isLoaded = world.getChunk(x, z, ChunkStatus.FULL, false) != null;
     System.out.println("Chunk at (" + x + ", " + z + ") loaded: " + isLoaded);
     return isLoaded;
     }*/

}
