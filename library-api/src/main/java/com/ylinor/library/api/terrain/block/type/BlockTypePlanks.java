package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;


public class BlockTypePlanks extends BlockType {

    public static final PropertyEnum<PlanksType> VARIANT = PropertyEnum.<PlanksType> create("variant", PlanksType.class);

    protected BlockTypePlanks(int id) {
        super(id, Material.WOOD);

        getDefaultAttributes().setBlockHardness(2.0f);
        getDefaultAttributes().setBlockResistance(15.0f);
    }

    @Override
    protected void initStates() {
        this.setDefaultState(this.getDefaultState()
                                 .with(VARIANT, PlanksType.OAK));
        for (BlockState state : blockStateFactory.getPossibleStates()) {
            state.getAttributes().setMapColor(state.get(VARIANT).getMapColor());
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                   .with(VARIANT, PlanksType.byMetadata(meta));
    }

    public int getMetaFromState(BlockState state) {
        return state.get(VARIANT).getMetadata();
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, VARIANT);
    }

    public static enum PlanksType {
        OAK(0, "oak", MapColor.WOOD),
        SPRUCE(1, "spruce", MapColor.OBSIDIAN),
        BIRCH(2, "birch", MapColor.SAND),
        JUNGLE(3, "jungle", MapColor.DIRT),
        ACACIA(4, "acacia", MapColor.ADOBE),
        DARK_OAK(5, "dark_oak", "big_oak", MapColor.BROWN);

        private static final PlanksType[] META_LOOKUP = new PlanksType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor mapColor;

        private PlanksType(int metaIn, String nameIn, MapColor mapColorIn) {
            this(metaIn, nameIn, nameIn, mapColorIn);
        }

        private PlanksType(int metaIn, String nameIn, String unlocalizedNameIn, MapColor mapColorIn) {
            this.meta = metaIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.mapColor = mapColorIn;
        }

        public int getMetadata() {
            return this.meta;
        }

        public MapColor getMapColor() {
            return this.mapColor;
        }

        public String toString() {
            return this.name;
        }

        public static PlanksType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            for (PlanksType blockplanks$enumtype : values()) {
                META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
            }
        }
    }
}
