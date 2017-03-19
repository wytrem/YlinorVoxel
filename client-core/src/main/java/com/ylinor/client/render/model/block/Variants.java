package com.ylinor.client.render.model.block;

import java.util.Map;

import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;


public class Variants {

    private Map<String, BlockModel> variants;

    public Variants(Map<String, BlockModel> variants) {
        this.variants = variants;
    }

    public BlockModel get(Terrain world, BlockType type, BlockState data) {
        if (variants.size() == 1) {
            return variants.values().iterator().next();
        }

        return variants.get(data.propertiesToString());
    }
}
