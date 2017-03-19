package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.RenderType;
import com.ylinor.library.api.terrain.block.material.Material;


public class BlockTypeAir extends BlockType {
    protected BlockTypeAir(int id) {
        super(id, Material.AIR);
        getDefaultAttributes().setFullBlock(false);
        getDefaultAttributes().setTranslucent(true);
        getDefaultAttributes().setRenderType(RenderType.INVISIBLE);
        getDefaultAttributes().setFullCube(false);
        getDefaultAttributes().setOpaqueCube(false);
        getDefaultAttributes().setCanCollideCheck(false);
        getDefaultAttributes().setCollisionBoundingBox(null);
    }
}
