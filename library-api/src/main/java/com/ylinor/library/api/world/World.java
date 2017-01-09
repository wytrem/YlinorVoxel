package com.ylinor.library.api.world;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.provider.IChunkProvider;
import com.ylinor.library.util.math.PositionableObject2D;


/**
 * Un monde Ylinor.
 *
 * Un monde est une grille de {@link Chunk}. Chaque Chunk fait la hauteur du
 * monde, et est un pavé droit à base carrée (dont normalement les dimensions
 * sont puissances de deux). Chaque chunk est rempli de blocs.
 *
 * @author Litarvan
 * @author wytrem
 * @since 1.0.0
 */
public class World {
    /**
     * La taille maximum d'un monde
     */
    public static final int MAX_HEIGHT = Chunk.CHUNK_SIZE_Y;

    /**
     * Le {@link IChunkProvider} qui sera appelé dans le cas d'utilisation de
     * {@link #getChunk(PositionableObject2D)} sur un chunk non-chargé, ou dans
     * le cas du déchargement d'un chunk.
     */
    @NotNull
    private IChunkProvider provider;

    /**
     * Un monde Ylinor
     *
     * @param provider Le chunk provider fournissant les Chunks du monde
     */
    public World(@NotNull IChunkProvider provider) throws IllegalArgumentException {
        this.provider = provider;
    }

    public Chunk getChunkFromBlockCoords(BlockPos pos) {
        return getChunkFromBlockCoords(pos.x, pos.y, pos.z);
    }

    public Chunk getChunkFromBlockCoords(int x, int y, int z) {
        return provider.provide(this, x >> 4, z >> 4);
    }

    public Chunk getChunkFromChunkCoords(Vector2i pos) {
        return getChunkFromChunkCoords(pos.x, pos.y);
    }

    public Chunk getChunkFromChunkCoords(int x, int z) {
        return provider.provide(this, x, z);
    }

    public Block getBlock(BlockPos pos) {
        return getChunkFromBlockCoords(pos.x, pos.y, pos.z).getBlock(pos.x & 15, pos.y, pos.z & 15);
    }

    public short getBlockId(BlockPos pos) {
        return getChunkFromBlockCoords(pos.x, pos.y, pos.z).getBlockId(pos.x & 15, pos.y, pos.z & 15);
    }

    public Block getBlock(int x, int y, int z) {
        return getChunkFromBlockCoords(x, y, z).getBlock(x & 15, y, z & 15);
    }

    public short getBlockId(int x, int y, int z) {
        return getChunkFromBlockCoords(x, y, z).getBlockId(x & 15, y, z & 15);
    }

    /**
     * @return Le chunk provider actuel (null si non donné au constructeur)
     */
    @NotNull
    public IChunkProvider getChunkProvider() {
        return provider;
    }
}
