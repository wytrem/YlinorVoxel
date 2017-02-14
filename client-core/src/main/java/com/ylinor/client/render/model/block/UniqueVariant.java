package com.ylinor.client.render.model.block;

import com.ylinor.library.api.world.blocks.BlockExtraData;
import com.ylinor.library.api.world.blocks.BlockType;
import com.ylinor.library.api.world.World;

public class UniqueVariant extends Variants {
    private BlockModel model;

    public UniqueVariant(BlockModel model) {
        super();
        this.model = model;
    }
    
    @Override
    public BlockModel get(World world, BlockType type, BlockExtraData data) {
        return model;
    }
}
