package com.ylinor.library.api.world;

import com.ylinor.library.api.block.BlockType;
import com.ylinor.library.api.world.provider.IChunkProvider;
import com.ylinor.library.util.math.Position2D;
import com.ylinor.library.util.math.Position3D;
import com.ylinor.library.util.math.Positionable2D;
import com.ylinor.library.util.math.Positionable3D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Un monde Ylinor.
 *
 * Un monde est une grille de {@link Chunk}.
 * Chaque Chunk fait la hauteur du monde, et est un pavé
 * droit à base carrée (dont normalement les dimensions sont
 * puissances de deux). Chaque chunk est rempli de blocs.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class World
{
    /**
     * La taille maximum d'un monde
     */
    public static final int MAX_HEIGHT = 256;

    /**
     * Le {@link IChunkProvider} qui sera appelé dans le cas
     * d'utilisation de {@link #getChunk(Positionable2D)} sur
     * un chunk non-chargé, ou dans le cas du déchargement d'un
     * chunk.
     */
    @NotNull
    private IChunkProvider provider;

    /**
     * Un monde Ylinor
     *
     * @param provider Le chunk provider fournissant les Chunks du monde
     */
    public World(@NotNull IChunkProvider provider) throws IllegalArgumentException
    {
        this.provider = provider;
    }

    /**
     * Retourne le chunk contenant la position en block donnée
     *
     * @param pos La position du monde en block à trouver
     *
     * @return Le chunk contenant la position donnée, null si
     * il est vide et qu'il n'y a pas de {@link IChunkProvider}
     * ou que celui-ci a renvoyé null
     */
    @Nullable
    public Chunk getChunkOfBlock(@NotNull Positionable3D pos)
    {
        return getChunk(getChunkPosOfBlock(pos));
    }

    /**
     * Retourne la position du chunk contenant la position en
     * block donnée
     *
     * @param pos La position du monde en block à trouver
     *
     * @return La position chunk contenant la position donnée
     */
    @NotNull
    public Position2D getChunkPosOfBlock(@NotNull Positionable3D pos)
    {
        return new Position2D(pos.getX() >> 4, pos.getY() >> 4);
    }

    /**
     * Retourne la position relative au chunk de la position
     * en block donnée
     *
     * @param pos La position d'un block, en block
     *
     * @return La même position que donnée, mais relative à son chunk
     */
    @NotNull
    public Positionable3D shift(@NotNull Positionable3D pos)
    {
        return new Position3D(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
    }

    /**
     * Retourne le type de block à la position NON relative
     * à son chunk, donnée
     *
     * @param pos La position du block en question NON relative
     *            à son chunk.
     *
     * @return Le block trouvé ou null si il n'y a pas de block
     */
    @Nullable
    public BlockType getBlock(@NotNull Positionable3D pos)
    {
        Chunk chunk = getChunkOfBlock(pos);
        return chunk == null ? null : chunk.getBlock(shift(pos));
    }

    /**
     * Retourne l'id du type de block à la position NON relative
     * à son chunk, donnée
     *
     * @param pos La position du block en question NON relative
     *            à son chunk.
     *
     * @return L'id du type de block
     */
    public int getBlockId(@NotNull Positionable3D pos)
    {
        Chunk chunk = getChunkOfBlock(pos);
        return chunk == null ? 0 : chunk.getBlockId(shift(pos));
    }

    /**
     * Retourne les données du block à la position NON relative
     * à son chunk, donnée
     *
     * @param pos La position du block en question NON relative
     *            à son chunk.
     *
     * @return Le block trouvé ou null si il n'y a pas de block
     * ou que c'est un block non particulier
     */
    @Nullable
    public BlockData getBlockData(@NotNull Positionable3D pos)
    {
        Chunk chunk = getChunkOfBlock(pos);
        return chunk == null ? null : chunk.getBlockData(shift(pos));
    }

    /**
     * Retourne le chunk à la position donnée
     *
     * @param pos La position du chunk à retourner
     *
     * @return Le chunk, null si il est vide et qu'il n'y a
     * pas de {@link IChunkProvider} ou que celui-ci a
     * renvoyé null
     */
    @Nullable
    public Chunk getChunk(@NotNull Positionable2D pos)
    {
        return provider.provide(this, pos);
    }

    /**
     * @return Le chunk provider actuel (null si non donné au constructeur)
     */
    @NotNull
    public IChunkProvider getChunkProvider()
    {
        return provider;
    }
}
