package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.util.math.PositionableObject2D;
import java.io.File;

public class CacheWorldStorage extends WorldStorage
{
    private File folder;
    private boolean writable;

    public CacheWorldStorage(File folder, boolean writable)
    {
        this.folder = folder;
        this.writable = writable;
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
        checkForWriting();
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        checkForWriting();
    }

    @Override
    public void saveChunk(PositionableObject2D pos, Chunk chunk)
    {
        checkForWriting();
    }

    private void checkForWriting()
    {
        if (!writable)
        {
            throw new IllegalStateException("Read-only world being edited");
        }
    }

    @Override
    public void saveChunk(int x, int z, Chunk chunk)
    {
        checkForWriting();
    }

    @Override
    public void saveWorld()
    {
        checkForWriting();
    }

    public File getFolder()
    {
        return folder;
    }

    public boolean isWritable()
    {
        return writable;
    }
}
