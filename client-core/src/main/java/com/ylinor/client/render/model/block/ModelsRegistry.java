package com.ylinor.client.render.model.block;

import com.ylinor.library.api.terrain.BlockExtraData;
import com.ylinor.library.api.terrain.BlockType;
import com.ylinor.library.api.terrain.Terrain;

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

    public BlockModel get(Terrain world, BlockType type, BlockExtraData data) {
        return variantsById.get(type.getId()).get(world, type, data);
    }
}
