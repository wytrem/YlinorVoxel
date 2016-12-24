package com.ylinor.library.api.world;

import org.jetbrains.annotations.NotNull;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.util.math.PositionableObject3D;


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
public class Block implements PositionableObject3D
{
    /**
     * Le type du block (ex: Block de dirt)
     */
    private short blockType;

    /**
     * La position du block dans le Chunk ({@link #chunk})
     */
    @NotNull
    private BlockPos pos;
    
    private boolean canBeUncached;

    public Block(short type, @NotNull BlockPos pos)
    {
        this.blockType = type;
        this.pos = pos;
    }
    
    public boolean canBeUncached()
    {
        return canBeUncached;
    }
    
    public void setCanBeUncached(boolean canBeUncached)
    {
        this.canBeUncached = canBeUncached;
    }

    /**
     * @return Le type de block (ex: Block de dirt)
     */
    public short getTypeId()
    {
        return blockType;
    }

    public int x()
    {
        return pos.x();
    }

    public int y()
    {
        return pos.y();
    }

    public int z()
    {
        return pos.z();
    }
    
    public BlockPos getPos()
    {
        return pos;
    }
}
