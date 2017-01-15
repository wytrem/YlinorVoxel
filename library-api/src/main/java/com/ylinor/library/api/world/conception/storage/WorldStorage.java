package com.ylinor.library.api.world.conception.storage;

import com.ylinor.library.api.world.conception.Chunk;
import com.ylinor.library.api.world.conception.IChunkContainer;
import com.ylinor.library.util.math.PositionableObject2D;

public abstract class WorldStorage implements IChunkContainer
{
    public abstract void saveChunk(PositionableObject2D pos, Chunk chunk);
    public abstract void saveChunk(int x, int z, Chunk chunk);

    public abstract void saveWorld();
}
