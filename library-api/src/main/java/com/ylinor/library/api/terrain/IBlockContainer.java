package com.ylinor.library.api.terrain;

import com.ylinor.library.api.terrain.block.Block;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.math.BlockPos;


public interface IBlockContainer {
    default Block getBlock(BlockPos pos) {
        return getBlock(pos.x(), pos.y(), pos.z());
    }

    Block getBlock(int x, int y, int z);

    default Block getOrCreate(BlockPos pos) {
        return getOrCreate(pos.x(), pos.y(), pos.z());
    }

    Block getOrCreate(int x, int y, int z);

    default BlockType getBlockType(BlockPos pos) {
        return getBlockType(pos.x(), pos.y(), pos.z());
    }

    BlockType getBlockType(int x, int y, int z);

    default BlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.x(), pos.y(), pos.z());
    }

    BlockState getBlockState(int x, int y, int z);

    default boolean hasData(int x, int y, int z) {
        return getBlockState(x, y, z) != null;
    }

    default boolean hasData(BlockPos pos) {
        return getBlockState(pos) != null;
    }

    void setBlock(Block block);

    default void setBlockType(BlockPos pos, BlockType type) {
        setBlockType(pos.x(), pos.y(), pos.z(), type);
    }

    void setBlockType(int x, int y, int z, BlockType type);

    default void setBlockState(BlockPos pos, BlockState data) {
        setBlockState(pos.x(), pos.y(), pos.z(), data);
    }

    void setBlockState(int x, int y, int z, BlockState data);
}
