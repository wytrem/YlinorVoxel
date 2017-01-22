package com.ylinor.library.api.world;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;

public class World implements IChunkContainer, IBlockContainer
{
    public static final short SIZE_Y = Chunk.SIZE_Y;

    private StorageManager storage;

    public World(StorageManager storage)
    {
        this.storage = storage;
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
    public Block getOrCreate(BlockPos pos)
    {
        return getChunkOf(pos).getOrCreate(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public Block getOrCreate(int x, int y, int z)
    {
        return getChunkOf(x, y, z).getOrCreate(x & 15, y, z & 15);
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
    public void setBlock(Block block)
    {
        Chunk chunk = getChunkOf(block.getPos());

        chunk.setBlockType(block.getPos().x & 15, block.getPos().y, block.getPos().z & 15, block.getType());
        chunk.setBlockData(block.getPos().x & 15, block.getPos().y, block.getPos().z & 15, block.getData());
    }

    @Override
    public void setBlockType(BlockPos pos, BlockType type)
    {
        getChunkOf(pos).setBlockType(pos.x & 15, pos.y, pos.z & 15, type);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type)
    {
        getChunkOf(x, y, z).setBlockType(x & 15, y, z & 15, type);
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

    public BlockType getBlockType(short id)
    {
        return BlockType.REGISTRY.get(id);
    }
}
