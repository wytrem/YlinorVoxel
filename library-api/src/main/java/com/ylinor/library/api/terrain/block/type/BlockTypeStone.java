package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;


public class BlockTypeStone extends BlockType {
    protected BlockTypeStone(int id) {
        super(id, Material.ROCK);
        getDefaultAttributes().setBlockHardness(1.5f);
        getDefaultAttributes().setBlockResistance(30.0f);
    }
}
