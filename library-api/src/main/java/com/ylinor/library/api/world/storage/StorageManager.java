package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkProvider;
import com.ylinor.library.util.math.PositionableObject2D;
import gnu.trove.map.hash.TShortObjectHashMap;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public class StorageManager implements IChunkProvider
{
    private WorldStorage storage;
    private TShortObjectHashMap<Chunk> cache = new TShortObjectHashMap<>();

    public StorageManager(File folder)
    {
        this.storage = new WorldStorage(folder);
    }

    @Override
    public @NotNull Chunk getChunk(PositionableObject2D pos)
    {
        return isLoaded(pos) ? cache.get(StorageUtil.posToShort(pos)) : storage.getChunk(pos);
    }

    @Override
    public @NotNull Chunk getChunk(int x, int z)
    {
        return isLoaded(x, z) ? cache.get(StorageUtil.posToShort(x, z)) : storage.getChunk(x, z);
    }

    public boolean isLoaded(PositionableObject2D pos)
    {
        return cache.containsKey(StorageUtil.posToShort(pos));
    }

    public boolean isLoaded(int x, int z)
    {
        return cache.containsKey(StorageUtil.posToShort(x, z));
    }

    public File getFolder()
    {
        return storage.getFolder();
    }
}
