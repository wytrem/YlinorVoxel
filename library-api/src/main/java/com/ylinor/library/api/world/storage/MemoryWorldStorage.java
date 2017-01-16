package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.util.math.PositionableObject2D;

public class MemoryWorldStorage extends WorldStorage
{
    public MemoryWorldStorage(boolean writable)
    {
        super(writable);
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        return null;
    }

    @Override
    public Chunk getChunk(int x, int z)
    {
        return null;
    }
}
