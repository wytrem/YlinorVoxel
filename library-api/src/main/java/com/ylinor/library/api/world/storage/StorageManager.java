package com.ylinor.library.api.world.storage;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkProvider;
import com.ylinor.library.api.world.World;
import com.ylinor.library.util.math.PositionableObject2D;

import gnu.trove.map.hash.TLongObjectHashMap;

public class StorageManager implements IChunkProvider
{
    private WorldStorage storage;
    private TLongObjectHashMap<Chunk> cache = new TLongObjectHashMap<>();

    public StorageManager(World world, File folder)
    {
        this.storage = new WorldStorage(world, folder);
    }

    @Override
    public @NotNull Chunk getChunk(PositionableObject2D pos)
    {
        return getChunk(pos.x(), pos.y());
    }

    @Override
    public @NotNull Chunk getChunk(int x, int z)
    {
        long id = StorageUtil.chunkXZToLong(x, z);
        Chunk chunk = cache.get(id);
         
        if (chunk == null) {
            chunk = storage.getChunk(x, z);
            cache.put(id, chunk);
        }
        
        return chunk;
    }

    public boolean isLoaded(PositionableObject2D pos)
    {
        return cache.containsKey(StorageUtil.chunkXZToLong(pos));
    }

    public boolean isLoaded(int x, int z)
    {
        return cache.containsKey(StorageUtil.chunkXZToLong(x, z));
    }

    public File getFolder()
    {
        return storage.getFolder();
    }
}
