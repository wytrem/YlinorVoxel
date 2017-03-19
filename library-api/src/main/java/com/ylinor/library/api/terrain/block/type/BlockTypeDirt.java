package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyBool;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;


public class BlockTypeDirt extends BlockType {
    public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.<DirtType> create("variant", DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockTypeDirt(int id) {
        super(id, Material.GROUND);
        getDefaultAttributes().setBlockHardness(0.5f);
        getDefaultAttributes().setBlockResistance(2.5f);
    }

    @Override
    protected void initStates() {
        this.setDefaultState(this.blockStateFactory.getOneState()
                                                   .with(VARIANT, DirtType.DIRT)
                                                   .with(SNOWY, Boolean.valueOf(false)));

        for (BlockState state : blockStateFactory.getPossibleStates()) {
            state.getAttributes().setMapColor(state.get(VARIANT).getColor());
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(VARIANT, DirtType.byMetadata(meta));
    }

    public int getMetaFromState(BlockState state) {
        return state.get(VARIANT).getMetadata();
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, VARIANT, SNOWY);
    }

    public static enum DirtType {
        DIRT(0, "dirt", "default", MapColor.DIRT),
        COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.DIRT),
        PODZOL(2, "podzol", MapColor.OBSIDIAN);

        private static final BlockTypeDirt.DirtType[] METADATA_LOOKUP = new BlockTypeDirt.DirtType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        private DirtType(int metadataIn, String nameIn, MapColor color) {
            this(metadataIn, nameIn, nameIn, color);
        }

        private DirtType(int metadataIn, String nameIn, String unlocalizedNameIn, MapColor color) {
            this.metadata = metadataIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.color = color;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public MapColor getColor() {
            return this.color;
        }

        public String toString() {
            return this.name;
        }

        public static BlockTypeDirt.DirtType byMetadata(int metadata) {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
                metadata = 0;
            }

            return METADATA_LOOKUP[metadata];
        }

        public String getName() {
            return this.name;
        }

        static {
            for (BlockTypeDirt.DirtType blockdirt$dirttype : values()) {
                METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
            }
        }
    }
}
