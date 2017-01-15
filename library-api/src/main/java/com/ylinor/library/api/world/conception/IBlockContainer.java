package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;

public interface IBlockContainer
{
    Block getBlock(BlockPos pos);
    Block getBlock(int x, int y, int z);

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

    void setBlock(BlockPos pos, Block block);
    void setBlock(int x, int y, int z, Block block);

    void setBlockType(BlockPos pos, BlockType block);
    void setBlockType(int x, int y, int z, BlockType block);

    void setBlockData(BlockPos pos, BlockExtraData data);
    void setBlockData(int x, int y, int z, BlockExtraData data);
}
