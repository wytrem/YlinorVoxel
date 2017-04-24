package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;


public class BlockTypeFence extends BlockType {
    protected BlockTypeFence(int id) {
        super(id, Material.WOOD);

        getDefaultAttributes().setOpaqueCube(false);
    }
}
