package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkContainer;
import com.ylinor.library.util.math.PositionableObject2D;

public abstract class WorldStorage implements IChunkContainer
{
    public abstract void saveChunk(PositionableObject2D pos, Chunk chunk);
    public abstract void saveChunk(int x, int z, Chunk chunk);

    public abstract void saveWorld();
}
