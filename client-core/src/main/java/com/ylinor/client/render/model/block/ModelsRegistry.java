package com.ylinor.client.render.model.block;

import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;


public class ModelsRegistry {
    private TShortObjectMap<Variants> variantsById;

    public ModelsRegistry() {
        variantsById = new TShortObjectHashMap<>(64);
    }

    public void register(BlockType type, Variants variants) {
        variantsById.put(type.getId(), variants);
    }

    public BlockModel get(Terrain world, BlockType type, BlockState data) {
        return variantsById.get(type.getId()).get(world, type, data);
    }
}
