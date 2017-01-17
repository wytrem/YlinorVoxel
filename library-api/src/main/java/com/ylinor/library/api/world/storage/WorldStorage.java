package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkContainer;
import com.ylinor.library.util.math.PositionableObject2D;

public abstract class WorldStorage implements IChunkContainer
{
    private boolean writable;

    public WorldStorage(boolean writable)
    {
        this.writable = writable;
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {
        checkForWriting();
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        checkForWriting();
    }

    public void save()
    {
    }

    protected void checkForWriting()
    {
        if (!writable)
        {
            throw new IllegalStateException("Read-only world being edited");
        }
    }

    public boolean isWritable()
    {
        return writable;
    }
}
