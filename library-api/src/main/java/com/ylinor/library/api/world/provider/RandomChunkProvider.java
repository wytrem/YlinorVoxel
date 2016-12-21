package com.ylinor.library.api.world.provider;

import java.util.Random;

import com.ylinor.library.api.block.BlockType;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;
import com.ylinor.library.util.math.Positionable2D;


public class RandomChunkProvider implements IChunkProvider
{
    private Random random = new Random();

    @Override
    public Chunk provide(World world, Positionable2D pos)
    {
        Chunk chunk = new Chunk(world, pos); // TODO: FIX SIZE PARAMETER

        for (int x = 0; x < chunk.getSizeX(); x++)
        {
            for (int y = 0; y < World.MAX_HEIGHT; y++)
            {
                for (int z = 0; z < chunk.getSizeZ(); z++)
                {
                    // TODO: Fix NPE
                    chunk.setBlock(x, y, z, BlockType.getByID(random.nextInt(1)));
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
