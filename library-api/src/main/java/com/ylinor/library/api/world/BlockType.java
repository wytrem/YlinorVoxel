package com.ylinor.library.api.world;

import com.ylinor.library.api.block.BlockPos;
import gnu.trove.map.hash.TShortObjectHashMap;

public class BlockType
{
    public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();
    public static final BlockType air = new BlockType((short) 0);

    private short id;

    public BlockType(short id)
    {
        this.id = id;
        REGISTRY.put(id, this);
    }
    
    public boolean isOpaque()
    {
        return id != 0;
    }

    public BlockExtraData createData(BlockPos pos, World world)
    {
        return null;
    }

    public short getId()
    {
        return id;
    }
}
