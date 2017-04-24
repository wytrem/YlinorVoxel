package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.BlockRenderLayer;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.util.math.AxisAlignedBB;


public class BlockTallGrass extends BlockType {

    protected BlockTallGrass(int id) {
        super(id, Material.GRASS);
        getDefaultAttributes().setFullBlock(false);
        getDefaultAttributes().setUseNeighborBrightness(true);
        getDefaultAttributes().setNeedsRandomTick(true);
        getDefaultAttributes().setFullCube(false);
        getDefaultAttributes().setOpaqueCube(false);
        getDefaultAttributes().setBoundingBox(new AxisAlignedBB(0.09999999403953552f, 0.0f, 0.09999999403953552f, 0.8999999761581421f, 0.800000011920929f, 0.8999999761581421f));
        getDefaultAttributes().setRenderLayer(BlockRenderLayer.CUTOUT);
        getDefaultAttributes().setColorMultiplier(-8602261);
    }

}
