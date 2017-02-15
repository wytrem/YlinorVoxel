package com.ylinor.library.api.world;

import com.ylinor.library.api.world.blocks.Block;
import com.ylinor.library.api.world.blocks.BlockExtraData;
import com.ylinor.library.api.world.blocks.BlockType;
import com.ylinor.library.util.math.BlockPos;

public interface IBlockContainer
{
    default Block getBlock(BlockPos pos)
    {
        return getBlock(pos.x(), pos.y(), pos.z());
    }
    Block getBlock(int x, int y, int z);

    default Block getOrCreate(BlockPos pos)
    {
        return getOrCreate(pos.x(), pos.y(), pos.z());
    }
    Block getOrCreate(int x, int y, int z);

    default BlockType getBlockType(BlockPos pos)
    {
        return getBlockType(pos.x(), pos.y(), pos.z());
    }
    BlockType getBlockType(int x, int y, int z);

    default BlockExtraData getBlockData(BlockPos pos)
    {
        return getBlockData(pos.x(), pos.y(), pos.z());
    }
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

    default void setBlockType(BlockPos pos, BlockType type)
    {
        setBlockType(pos.x(), pos.y(), pos.z(), type);
    }
    void setBlockType(int x, int y, int z, BlockType type);

    default void setBlockData(BlockPos pos, BlockExtraData data)
    {
        setBlockData(pos.x(), pos.y(), pos.z(), data);
    }
    void setBlockData(int x, int y, int z, BlockExtraData data);
}
