package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import java.util.HashMap;

public class Region implements IBlockContainer
{
    private HashMap<Short, Chunk> chunks = new HashMap<>();

    public Chunk getChunkOf(Block block)
    {
        return null;
    }

    public Chunk getChunkOf(BlockPos pos)
    {
        return null;
    }

    public Chunk getChunkOf(int x, int y, int z)
    {
        return null;
    }

    @Override
    public Block getBlock(BlockPos pos)
    {
        return null;
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return null;
    }

    @Override
    public void setBlock(BlockPos pos, Block block)
    {

    }

    @Override
    public void setBlock(int x, int y, int z, Block block)
    {

    }
}
