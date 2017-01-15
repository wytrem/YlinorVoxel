package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.conception.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;

public class World implements IChunkContainer, IBlockContainer
{
    public static final short SIZE_Y = Chunk.SIZE_Y;

    private StorageManager storage;

    public World(StorageManager storage)
    {
        this.storage = storage;
    }

    public Region getRegionOf(Block block)
    {
        return null;
    }

    public Region getRegionOf(BlockPos pos)
    {
        return null;
    }

    public Region getRegionOf(int x, int y, int z)
    {
        return null;
    }

    public Chunk getChunkOf(Block block)
    {
        return getChunkOf(block.getPos());
    }

    public Chunk getChunkOf(BlockPos pos)
    {
        return getChunk(pos.x >> 4, pos.z >> 4);
    }

    public Chunk getChunkOf(int x, int y, int z)
    {
        return getChunk(x >> 4, z >> 4);
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos)
    {
        return storage.getChunk(pos);
    }

    @Override
    public Chunk getChunk(int x, int z)
    {
        return storage.getChunk(x, z);
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk)
    {
        storage.setChunk(pos, chunk);
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk)
    {
        storage.setChunk(x, z, chunk);
    }

    public StorageManager getStorage()
    {
        return storage;
    }

    @Override
    public Block getBlock(BlockPos pos)
    {
        return getChunkOf(pos).getBlock(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return getChunkOf(x, y, z).getBlock(x & 15, y, z & 15);
    }

    @Override
    public BlockType getBlockType(BlockPos pos)
    {
        return getChunkOf(pos).getBlockType(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public BlockType getBlockType(int x, int y, int z)
    {
        return getChunkOf(x, y, z).getBlockType(x & 15, y, z & 15);
    }

    @Override
    public BlockExtraData getBlockData(BlockPos pos)
    {
        return getChunkOf(pos).getBlockData(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public BlockExtraData getBlockData(int x, int y, int z)
    {
        return getChunkOf(x, y, z).getBlockData(x & 15, y, z & 15);
    }

    @Override
    public void setBlock(BlockPos pos, Block block)
    {
        getChunkOf(pos).setBlock(pos.x & 15, pos.y, pos.z & 15, block);
    }

    @Override
    public void setBlock(int x, int y, int z, Block block)
    {
        getChunkOf(x, y, z).setBlock(x & 15, y, z & 15, block);
    }

    @Override
    public void setBlockData(BlockPos pos, BlockExtraData data)
    {
        getChunkOf(pos).setBlockData(pos.x & 15, pos.y, pos.z & 15, data);
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockExtraData data)
    {
        getChunkOf(x, y, z).setBlockData(x & 15, y, z & 15, data);
    }
}
