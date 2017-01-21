package com.ylinor.library.api.world;

import com.ylinor.library.util.math.PositionableObject2D;

public interface IChunkContainer
{
    Chunk getChunk(PositionableObject2D pos);
    Chunk getChunk(int x, int z);

    void setChunk(PositionableObject2D pos, Chunk chunk);
    void setChunk(int x, int z, Chunk chunk);

    default boolean hasChunk(int x, int z)
    {
        return getChunk(x, z) != null;
    }

    default boolean hasChunk(PositionableObject2D pos)
    {
        return getChunk(pos) != null;
    }
}
