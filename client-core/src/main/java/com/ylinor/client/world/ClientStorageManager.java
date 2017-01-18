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

    private int currentChunkX = -1;
    private int currentChunkZ = -1;

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        return memory.getChunk(pos);
    }

    @Override
    public Chunk getChunk(int x, int z)
    {
        return memory.getChunk(x, z);
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {
        throw new UnsupportedOperationException("Client world tried to be edited");
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        throw new UnsupportedOperationException("Client world tried to be edited");
    }

    @Override
    public void onPlayerMove(PositionableObject3D pos)
    {
        super.onPlayerMove(pos);

        int x = pos.x() >> 4;
        int z = pos.z() >> 4;

        if (x == currentChunkX && z == currentChunkZ)
        {
            return;
        }

        if (!hardCache.hasChunk(x, z))
        {
            throw new UnsupportedOperationException("Not implemented : Should block the player, this is not supposed to happen");
        }

        PositionableObject2D[] chunks = getMemoryChunks();

        for (PositionableObject2D p : chunks)
        {
            if (memory.hasChunk(p))
            {
                continue;
            }

            if (cache.hasChunk(p))
            {
                memory.setChunk(p, cache.getChunk(p));
            }
            else
            {
                // TODO: Charger la région qu'il faut dans le cache
            }
        }

        // TODO: Si le joueur est a plus de <render distance> d'une région adjacente chargée dans le cache, supprimer cette dernière du cache
        // TODO: Supprimer de la mémoire les chunks qui ne doivent plus y être :/
    }

    private PositionableObject2D[] getMemoryChunks()
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
