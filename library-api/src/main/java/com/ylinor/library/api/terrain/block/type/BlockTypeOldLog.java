package com.ylinor.library.api.terrain.block.type;

import java.util.function.Predicate;

import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;
import com.ylinor.library.api.terrain.block.type.BlockTypePlanks.PlanksType;


public class BlockTypeOldLog extends BlockType {
    public static final PropertyEnum<PlanksType> VARIANT = PropertyEnum.create("variant", PlanksType.class, new Predicate<PlanksType>() {
        public boolean test(PlanksType p_apply_1_) {
            return p_apply_1_.getMetadata() < 4;
        }
    });

    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);

    protected BlockTypeOldLog(int id) {
        super(id, Material.WOOD);
    }

    @Override
    protected void initStates() {
        this.setDefaultState(this.blockStateFactory.getOneState()
                                                   .with(VARIANT, PlanksType.SPRUCE)
                                                   .with(LOG_AXIS, EnumAxis.Y));
    }

    public BlockState getStateFromMeta(int meta) {
        BlockState iblockstate = this.getDefaultState()
                                     .with(VARIANT, PlanksType.byMetadata((meta & 3) % 4));
        switch (meta & 12) {
            case 0:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.NONE);
        }

        return iblockstate;
    }

    @SuppressWarnings("incomplete-switch")
    public int getMetaFromState(BlockState state) {
        int i = 0;
        i = i | state.get(VARIANT).getMetadata();
        switch (state.get(LOG_AXIS)) {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }

        return i;
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, VARIANT, LOG_AXIS);
    }
}
