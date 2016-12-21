package com.ylinor.library.api.world;

import org.jetbrains.annotations.NotNull;

import com.ylinor.library.api.block.BlockType;
import com.ylinor.library.util.math.Position3D;
import com.ylinor.library.util.math.Positionable3D;


/**
 * Un Block d'un {@link Chunk}
 *
 * Un Block est un cube qui fait environ 1m x 1m. Il est l'unité la plus petite
 * d'Ylinor, et il constitue les Chunks (donc les mondes).
 *
 * Il y a différentes types de blocks, chacun avec leur actions, leur texture,
 * leur forme.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public abstract class BlockData implements Positionable3D
{
    /**
     * Le type du block (ex: Block de dirt)
     */
    @NotNull
    private BlockType type;

    /**
     * Le {@link Chunk} où est ce block
     */
    @NotNull
    private Chunk chunk;

    /**
     * La position du block dans le Chunk ({@link #chunk})
     */
    @NotNull
    private Positionable3D pos;

    /**
     * Un Block
     *
     * @param chunk Le {@link Chunk} contenant ce block
     * @param pos La position du block par rapport au chunk
     */
    public BlockData(@NotNull BlockType type, @NotNull Chunk chunk, @NotNull Positionable3D pos)
    {
        this.type = type;
        this.chunk = chunk;
        this.pos = pos;
    }

    /**
     * @return Le type de block (ex: Block de dirt)
     */
    @NotNull
    public BlockType getType()
    {
        return type;
    }

    /**
     * @return Le Chunk qui contient ce block
     */
    @NotNull
    public Chunk getChunk()
    {
        return chunk;
    }

    /**
     * @return La position du bloc relative au Chunk
     */
    @NotNull
    public Positionable3D getPosInChunk()
    {
        return pos;
    }

    /**
     * @return La position du block relative au monde
     */
    @NotNull
    public Positionable3D getPos()
    {
        return new Position3D(getX(), getY(), getZ());
    }

    @Override
    public int getX()
    {
        return getChunk().getX() * getChunk().getSizeX() + getPosInChunk().getX();
    }

    @Override
    public int getY()
    {
        return getPosInChunk().getY();
    }

    @Override
    public int getZ()
    {
        return getChunk().getY() * getChunk().getSizeY() + getPosInChunk().getZ();
    }
}
