package com.ylinor.client.world;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.storage.CacheWorldStorage;
import com.ylinor.library.util.io.Compresser;
import com.ylinor.library.util.io.Serializer;
import com.ylinor.library.util.math.PositionableObject2D;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class HardCacheWorldStorage extends CacheWorldStorage
{
    public HardCacheWorldStorage(File folder, boolean writable)
    {
        super(folder, writable);
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        try
        {
            return read(getFileFor(pos));
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
            return read(getFileFor(x, z));
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new IllegalStateException("Corrupted World ! (Can't load chunk from cache)", e);
        }
    }

    private Chunk read(File file) throws IOException, ClassNotFoundException
    {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        bytes = Compresser.decompress(bytes);

        return Serializer.read(bytes);
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
}
