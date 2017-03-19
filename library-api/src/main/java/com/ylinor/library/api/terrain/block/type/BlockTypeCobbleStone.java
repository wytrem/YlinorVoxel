package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;


public class BlockTypeCobbleStone extends BlockType {
    protected BlockTypeCobbleStone(int id) {
        super(id, Material.ROCK);
        getDefaultAttributes().setBlockHardness(2.0f);
        getDefaultAttributes().setBlockResistance(30.0f);
    }
}
