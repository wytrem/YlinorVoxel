package com.ylinor.library.api.terrain;

import com.ylinor.library.util.math.BlockPos;


public abstract class BlockExtraData {
    public abstract Block provide(BlockType type, BlockPos pos, Terrain world);

    public abstract boolean isApplicableFor(BlockType type);
}
