package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;

public class BlockTypeHalfSlab extends BlockType {
    protected BlockTypeHalfSlab(int id) {
        super(id, Material.ROCK);

        getDefaultAttributes().setOpaqueCube(false);
    }
}
