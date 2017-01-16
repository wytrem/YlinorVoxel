package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.util.Serializer;
import com.ylinor.library.util.math.PositionableObject2D;
import java.io.File;
import java.io.IOException;

public class CacheWorldStorage extends WorldStorage
{
    private File folder;

    public CacheWorldStorage(File folder, boolean writable)
    {
        super(writable);
        this.folder = folder;
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        try
        {
            return Serializer.read(getFileFor(pos));
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new IllegalStateException("Corrupted World ! (Can't load chunk from cache)", e);
        }
    }

    @Override
    public Chunk getChunk(int x, int z)
    {
        try
        {
            return Serializer.read(getFileFor(x, z));
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new IllegalStateException("Corrupted World ! (Can't load chunk from cache)", e);
        }
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {
        try
        {
            Serializer.write(getFileFor(pos), chunk);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("World writing error ! (Can't save chunk to cache)", e);
        }
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        try
        {
            Serializer.write(getFileFor(x, z), chunk);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("World writing error ! (Can't save chunk to cache)", e);
        }
    }

    private File getFileFor(int x, int z)
    {
        return new File(folder, "c_" + x + "-" + z);
    }

    private File getFileFor(PositionableObject2D pos)
    {
        return new File(folder, "c_" + pos.x() + "-" + pos.y());
    }

    public File getFolder()
    {
        return folder;
    }
}
