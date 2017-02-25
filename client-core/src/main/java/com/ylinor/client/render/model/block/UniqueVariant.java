package com.ylinor.client.render.model.block;

import com.ylinor.library.api.terrain.BlockExtraData;
import com.ylinor.library.api.terrain.BlockType;
import com.ylinor.library.api.terrain.Terrain;

public class UniqueVariant extends Variants {
    private BlockModel model;

    public UniqueVariant(BlockModel model) {
        super();
        this.model = model;
    }

    @Override
    public BlockModel get(Terrain world, BlockType type, BlockExtraData data) {
        return model;
    }
}
