package com.ylinor.library.api.terrain.block.type;

import com.artemis.World;
import com.ylinor.library.api.terrain.block.BlockAttributes;
import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.util.math.BlockPos;

import gnu.trove.map.hash.TShortObjectHashMap;


public class BlockType {
    public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();
    public static final BlockType air = new BlockTypeAir(0);
    public static final BlockType stone = new BlockTypeStone(1);
    public static final BlockType grass = new BlockTypeGrass(2);
    public static final BlockType dirt = new BlockTypeDirt(3);
    public static final BlockType cobbleStone = new BlockTypeCobbleStone(4);
    public static final BlockType planks = new BlockTypePlanks(5);

    private final short id;
    protected BlockStateFactory blockStateFactory;
    private BlockState defaultBlockState;
    private final BlockAttributes defaultAttributes;

    protected BlockType(int id, Material blockMaterialIn, MapColor blockMapColorIn) {
        this.id = (short) id;
        defaultAttributes = new BlockAttributes(blockMaterialIn, blockMapColorIn);
        REGISTRY.put(this.id, this);
    }
    
    protected final void initStateFactory() {
        this.blockStateFactory = createBlockState();
        setDefaultState(blockStateFactory.getOneState());
    }
    
    protected void initStates() {
        
    }

    protected BlockType(int id, Material materialIn) {
        this(id, materialIn, materialIn.getMaterialMapColor());
    }

    protected BlockStateFactory createBlockState() {
        return new BlockStateFactory(this, new StateProperty<?> [0]);
    }

    public void setDefaultState(BlockState defaultBlockState) {
        this.defaultBlockState = defaultBlockState;
    }

    public BlockState getDefaultState() {
        return defaultBlockState;
    }

    public short getId() {
        return id;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
     }
    
    public int getMetaFromState(BlockState state) {
        if (state.getProperties().isEmpty()) {
            return 0;
        }
        else {
            throw new IllegalArgumentException("Don\'t know how to convert " + state + " back into data...");
        }
    }

    public void onFallenUpon(World worldIn, BlockPos pos, int entityIn, float fallDistance) {
        // entityIn.fall(fallDistance, 1.0F);
    }

    public BlockAttributes getDefaultAttributes() {
        return defaultAttributes;
    }
    
    static {
        REGISTRY.valueCollection().forEach(type -> {
            type.initStateFactory();
            type.initStates();
        });
    }
}
