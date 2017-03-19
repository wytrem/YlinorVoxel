package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.BlockRenderLayer;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;
import com.ylinor.library.api.terrain.block.state.props.PropertyInteger;
import com.ylinor.library.api.terrain.block.type.BlockTypePlanks.PlanksType;
import com.ylinor.library.util.math.AxisAlignedBB;


public class BlockTypeSapling extends BlockType {

    public static final PropertyEnum<PlanksType> TYPE = PropertyEnum.<PlanksType> create("type", PlanksType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    protected BlockTypeSapling(int id) {
        super(id, Material.PLANTS);

        getDefaultAttributes().setFullBlock(false);
        getDefaultAttributes().setUseNeighborBrightness(true);
        getDefaultAttributes().setNeedsRandomTick(true);
        getDefaultAttributes().setFullCube(false);
        getDefaultAttributes().setOpaqueCube(false);
        getDefaultAttributes().setBoundingBox(new AxisAlignedBB(0.09999999403953552f, 0.0f, 0.09999999403953552f, 0.8999999761581421f, 0.800000011920929f, 0.8999999761581421f));
//        getDefaultAttributes().setCollisionBoundingBox(null);
        getDefaultAttributes().setRenderLayer(BlockRenderLayer.CUTOUT);
    }
    
    @Override
    protected void initStates() {
        this.setDefaultState(this.getDefaultState().with(TYPE, PlanksType.OAK).with(STAGE, 0));
    }

    public int getMetaFromState(BlockState state) {
        int i = 0;
        i = i | state.get(TYPE).getMetadata();
        i = i | state.get(STAGE).intValue() << 3;
        return i;
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, TYPE, STAGE);
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                   .with(TYPE, PlanksType.byMetadata(meta & 7))
                   .with(STAGE, Integer.valueOf((meta & 8) >> 3));
    }

}
