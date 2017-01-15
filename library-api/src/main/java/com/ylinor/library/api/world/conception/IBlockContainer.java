package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;

public interface IBlockContainer
{
    Block getBlock(BlockPos pos);
    Block getBlock(int x, int y, int z);

    Block getOrCreate(BlockPos pos);
    Block getOrCreate(int x, int y, int z);

    BlockType getBlockType(BlockPos pos);
    BlockType getBlockType(int x, int y, int z);

    BlockExtraData getBlockData(BlockPos pos);
    BlockExtraData getBlockData(int x, int y, int z);

    default boolean hasData(int x, int y, int z)
    {
        return getBlockData(x, y, z) != null;
    }

    default boolean hasData(BlockPos pos)
    {
        return getBlockData(pos) != null;
    }

    void setBlock(Block block);

    void setBlockType(BlockPos pos, BlockType type);
    void setBlockType(int x, int y, int z, BlockType type);

    void setBlockData(BlockPos pos, BlockExtraData data);
    void setBlockData(int x, int y, int z, BlockExtraData data);
}
