package com.ylinor.library.api.world.storage;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkProvider;
import com.ylinor.library.util.io.Compresser;
import com.ylinor.library.util.io.Serializer;
import com.ylinor.library.util.math.PositionableObject2D;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

public class WorldStorage implements IChunkProvider
{
    private File folder;

    public WorldStorage(File folder)
    {
        this.folder = folder;
    }

    public File getFileOf(int x, int z)
    {
        return new File("c-" + x + "-" + z + ".bin.zst");
    }

    public File getFileOf(PositionableObject2D pos)
    {
        return new File("c-" + pos.x() + "-" + pos.y() + ".bin.zst");
    }

    public @NotNull Chunk load(File file)
    {
        try
        {
            return Serializer.read(Compresser.decompress(FileUtils.readFileToByteArray(file)));
        }
        catch (IOException e)
        {
            System.err.println("/!\\ I/O error while reading ");
            e.printStackTrace();

            return new Chunk();
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
