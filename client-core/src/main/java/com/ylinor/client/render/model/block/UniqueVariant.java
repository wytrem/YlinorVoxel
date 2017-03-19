package com.ylinor.client.render.model.block;

import java.util.HashMap;

import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;


public class UniqueVariant extends Variants {
    private BlockModel model;

    public UniqueVariant(BlockModel model) {
        super(new HashMap<>());
        this.model = model;
    }

    @Override
    public BlockModel get(Terrain world, BlockType type, BlockState data) {
        return model;
    }
}
