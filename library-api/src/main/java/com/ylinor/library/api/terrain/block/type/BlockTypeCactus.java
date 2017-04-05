package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;

public class BlockTypeCactus extends BlockType {
    protected BlockTypeCactus(int id) {
        super(id, Material.CACTUS);

        getDefaultAttributes().setOpaqueCube(false);
    }
}
