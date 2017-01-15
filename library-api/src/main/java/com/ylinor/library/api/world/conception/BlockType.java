package com.ylinor.library.api.world.conception;

import gnu.trove.map.hash.TShortObjectHashMap;

public class BlockType
{
    public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();

    private short id;

    public BlockType(short id)
    {
        this.id = id;
        REGISTRY.put(id, this);
    }

    public short getId()
    {
        return id;
    }
}
