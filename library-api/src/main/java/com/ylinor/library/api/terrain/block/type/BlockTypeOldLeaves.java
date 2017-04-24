package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;
import com.ylinor.library.api.terrain.block.type.BlockTypePlanks.PlanksType;


public class BlockTypeOldLeaves extends BlockType {
    public static final PropertyEnum<PlanksType> VARIANT = PropertyEnum.create("variant", PlanksType.class, type -> type.getMetadata() < 4);

    protected BlockTypeOldLeaves(int id) {
        super(id, Material.LEAVES);
        getDefaultAttributes().setLightOpacity(1);
        getDefaultAttributes().setBlockHardness(0.2f);
        getDefaultAttributes().setBlockResistance(1.0f);
        getDefaultAttributes().setNeedsRandomTick(true);
        getDefaultAttributes().setColorMultiplier(-8602261);
    }

    @Override
    protected void initStates() {
        this.setDefaultState(this.blockStateFactory.getOneState()
                                                   .with(VARIANT, PlanksType.OAK));
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                   .with(VARIANT, PlanksType.byMetadata((meta & 3) % 4));
    }

    public int getMetaFromState(BlockState state) {
        int i = 0;
        i = i | state.get(VARIANT).getMetadata();

        return i;
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, VARIANT);
    }
}
