package com.ylinor.library.api.world.blocks;

import com.ylinor.library.api.world.World;
import com.ylinor.library.util.math.BlockPos;

public abstract class BlockExtraData
{
    public abstract Block provide(BlockType type, BlockPos pos, World world);
    
    public abstract boolean isApplicableFor(BlockType type);
}
