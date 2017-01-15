package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.conception.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;

public class World implements IChunkContainer
{
    public static final short SIZE_Y = Chunk.SIZE_Y;

    private StorageManager storage;

    public World(StorageManager storage)
    {
        this.storage = storage;
    }

    public Region getRegionOf(Block block)
    {
        return null;
    }

    public Region getRegionOf(BlockPos pos)
    {
        return null;
    }

    public Region getRegionOf(int x, int y, int z)
    {
        return null;
    }

    public Chunk getChunkOf(Block block)
    {
        return null;
    }

    public Chunk getChunkOf(BlockPos pos)
    {
        return null;
    }

    public Chunk getChunkOf(int x, int y, int z)
    {
        return null;
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

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {

    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {

    }

    public StorageManager getStorage()
    {
        return storage;
    }
}
