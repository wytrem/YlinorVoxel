package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;

public abstract class BlockExtraData
{
    public abstract Block provide(BlockType type, BlockPos pos);
}
