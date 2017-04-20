package com.ylinor.client.render.model.block;

import java.util.Map;

import com.ylinor.client.render.model.ModelDeserializer;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;


public class Variants {
    private Map<String, BlockModel> variants;
    private ModelDeserializer modelDeserializer;

    public Variants(Map<String, BlockModel> firstVariants, ModelDeserializer modelDeserializer) {
        this.variants = firstVariants;
        this.modelDeserializer = modelDeserializer;
    }

    public BlockModel get(Terrain world, BlockType type, BlockState data) {
        if (variants.size() == 1) {
            return variants.values().iterator().next();
        }

        String key = data.propertiesToString();

        if (!variants.containsKey(key)) {
            BlockModel variant = modelDeserializer.variant(data);

            if (variant == null) {
                throw new IllegalArgumentException("Block model variant for '" + key + "' not found for type " + type.getId() + ".");
            }

            variants.put(key, variant);
            return variant;
        }

        return variants.get(data.propertiesToString());
    }
}
