package com.ylinor.library.api.world.storage;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkProvider;
import com.ylinor.library.api.world.World;
import com.ylinor.library.util.io.Compresser;
import com.ylinor.library.util.io.Serializer;
import com.ylinor.library.util.math.PositionableObject2D;

public class WorldStorage implements IChunkProvider
{
    private static final Logger logger = LoggerFactory.getLogger(WorldStorage.class);
    
    private File folder;
    private World world;
    private Chunk dummyChunk;

    public WorldStorage(World world, File folder)
    {
        this.folder = folder;
        this.world = world;
        this.dummyChunk = new Chunk(world, 0, 0);
    }

    public File getFileOf(int x, int z)
    {
        return new File(folder, "c-" + x + "-" + z + ".bin.zst");
    }

    public File getFileOf(PositionableObject2D pos)
    {
        return new File(folder, "c-" + pos.x() + "-" + pos.y() + ".bin.zst");
    }

    public @NotNull Chunk load(File file)
    {
        try
        {
            return Serializer.read(Compresser.decompress(FileUtils.readFileToByteArray(file)));
        }
        catch (IOException e)
        {
            logger.error("I/O error while reading ", e);
            return dummyChunk;
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("FATAL: Chunk class not found while un-serializing a chunk", e);
        }
    }

    public File getFolder()
    {
        return folder;
    }

    @Override
    public @NotNull Chunk getChunk(PositionableObject2D pos)
    {
        return load(getFileOf(pos));
    }

    @Override
    public @NotNull Chunk getChunk(int x, int z)
    {
        return load(getFileOf(x, z));
    }
}
