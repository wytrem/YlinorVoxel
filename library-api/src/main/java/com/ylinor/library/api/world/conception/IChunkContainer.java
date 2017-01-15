package com.ylinor.library.api.world.conception;

import com.ylinor.library.util.math.PositionableObject2D;

public interface IChunkContainer
{
    Chunk getChunk(PositionableObject2D pos);
    Chunk getChunk(int x, int z);

    void setChunk(PositionableObject2D pos, Chunk chunk);
    void setChunk(int x, int z, Chunk chunk);
}
