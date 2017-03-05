package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.BlockRenderLayer;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyBool;


public class BlockTypeGrass extends BlockType {
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockTypeGrass(int id) {
        super(id, Material.GRASS);
        getDefaultAttributes().setBlockHardness(0.6f);
        getDefaultAttributes().setBlockResistance(3.0f);
        getDefaultAttributes().setNeedsRandomTick(true);
        getDefaultAttributes().setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
        getDefaultAttributes().setColorMultiplier(-8602261);
    }

    public int getMetaFromState(BlockState state) {
        return state.get(SNOWY) ? 1 : 0;
    }
    
    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(SNOWY, meta == 1);
    }
    
    @Override
    protected void initStates() {
        this.setDefaultState(this.blockStateFactory.getOneState()
                                                   .with(SNOWY, Boolean.valueOf(false)));
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, SNOWY);
    }
}
