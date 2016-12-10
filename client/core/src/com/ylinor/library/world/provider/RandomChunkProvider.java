package com.ylinor.library.world.provider;

import com.ylinor.library.block.BlockType;
import com.ylinor.library.math.Positionable2D;
import com.ylinor.library.world.Chunk;
import com.ylinor.library.world.World;
import java.util.Random;

public class RandomChunkProvider implements IChunkProvider
{
    private Random random = new Random();

    @Override
    public Chunk provide(World world, Positionable2D pos)
    {
        int size = world.getChunkSize();
        Chunk chunk = new Chunk(world, pos, size); // TODO: FIX SIZE PARAMETER

        int max = BlockType.getTypes().length;

        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < world.getSizeY(); y++)
            {
                for (int z = 0; z < size; z++)
                {
                    // TODO: Fix NPE
                    chunk.setBlock(x, y, z, BlockType.getByID(random.nextInt(max)));
                }
            }
        }

        return chunk;
    }

    @Override
    public void unload(World world, Positionable2D pos)
    {
    }
}
