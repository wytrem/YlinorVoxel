package com.ylinor.server.world;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;

public class ServerStorageManager extends StorageManager
{
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

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {

    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {

    }
}