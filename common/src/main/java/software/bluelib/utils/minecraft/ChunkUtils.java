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

/**
 * A {@code class} providing methods to interact with Minecraft chunks,
 * specifically for retrieving biome and tile entity information.
 * <p>
 * Key Methods:
 * <ul>
 * <li>{@link #getBiomeOfChunk(Level, ChunkPos)} - Retrieves the {@link Biome} of the specified chunk.</li>
 * <li>{@link #getChunkTileEntities(Level, ChunkPos)} - Retrieves the tile entities within the specified chunk.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.7.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ChunkUtils {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This constructor is intentionally empty to prevent creating instances of this utility class.
     * </p>
     *
     * @author MeAlam
     * @since 1.0.0
     */
    private ChunkUtils() {}

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
     * @author MeAlam
     * @since 1.0.0
     */
    public static Biome getBiomeOfChunk(Level pLevel, ChunkPos pChunkPos) {
        try {
            return pLevel.getBiome(pChunkPos.getWorldPosition()).value();
        } catch (Exception pException) {
            BaseLogger.log(BaseLogLevel.ERROR, "Error retrieving biome for chunk at position " + pChunkPos, pException, true);
            throw pException;
        }
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
     * @author MeAlam
     * @since 1.0.0
     */
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
