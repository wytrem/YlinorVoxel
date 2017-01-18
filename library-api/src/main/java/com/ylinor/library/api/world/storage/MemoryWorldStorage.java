package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.util.math.PositionableObject2D;
import gnu.trove.map.hash.TShortObjectHashMap;

public class MemoryWorldStorage extends WorldStorage
{
    private TShortObjectHashMap<Chunk> chunks = new TShortObjectHashMap<>();

    public MemoryWorldStorage(boolean writable)
    {
        super(writable);
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        return chunks.get(StorageUtil.posToShort(pos));
    }

    @Override
    public Chunk getChunk(int x, int z)
    {
        return chunks.get(StorageUtil.posToShort(x, z));
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {
        if (hasChunk(pos))
        {
            checkForWriting();
        }

        chunks.put(StorageUtil.posToShort(pos), chunk);
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        if (hasChunk(x, z))
        {
            checkForWriting();
        }

        chunks.put(StorageUtil.posToShort(x, z), chunk);
    }



    @Override
    public void unloadChunk(PositionableObject2D pos, Chunk chunk)
    {
        super.unloadChunk(pos, chunk);
    }

    @Override
    public void unloadChunk(int x, int z, Chunk chunk)
    {
        super.unloadChunk(x, z, chunk);
    }
}
