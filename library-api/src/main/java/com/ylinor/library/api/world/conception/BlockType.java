package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import gnu.trove.map.hash.TShortObjectHashMap;

public abstract class BlockType
{
    public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();

    private short id;

    public BlockType(short id)
    {
        this.id = id;
        REGISTRY.put(id, this);
    }

    public abstract BlockExtraData createData(BlockPos pos);

    public short getId()
    {
        return id;
    }
}
