package com.ylinor.client.world;

import com.ylinor.client.util.YlinorFiles;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.storage.CacheWorldStorage;
import com.ylinor.library.api.world.storage.MemoryWorldStorage;
import com.ylinor.library.api.world.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;
import com.ylinor.library.util.math.PositionableObject3D;
import java.io.File;

public class ClientStorageManager extends StorageManager
{
    private File folder = new File(YlinorFiles.getGameFolder(), "world");

    private MemoryWorldStorage memory = new MemoryWorldStorage(false);
    private CacheWorldStorage cache = new CacheWorldStorage(new File(folder, "cache"), false);
    private HardCacheWorldStorage hardCache = new HardCacheWorldStorage(new File(folder, "hard-cache"), false);

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

    @Override
    public void playerPosUpdate(PositionableObject3D pos)
    {
        super.playerPosUpdate(pos);
    }
}
