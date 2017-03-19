package com.ylinor.library.api.terrain.block.type;

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
    public static final BlockType air = new BlockTypeAir(0).setUnlocalizedName("air");
    public static final BlockType stone = new BlockTypeStone(1).setUnlocalizedName("stone");
    public static final BlockType grass = new BlockTypeGrass(2).setUnlocalizedName("grass");
    public static final BlockType dirt = new BlockTypeDirt(3).setUnlocalizedName("dirt");
    public static final BlockType cobbleStone = new BlockTypeCobbleStone(4).setUnlocalizedName("cobblestone");
    public static final BlockType planks = new BlockTypePlanks(5).setUnlocalizedName("planks");
    public static final BlockType sapling = new BlockTypeSapling(6).setUnlocalizedName("sapling");
    public static final BlockType bedrock = new BlockType(7, Material.ROCK).setUnlocalizedName("bedrock");
    public static final BlockType flowingWater = new BlockType(8, Material.WATER).setUnlocalizedName("flowing_water");
    public static final BlockType water = new BlockType(9, Material.WATER).setUnlocalizedName("water");
    public static final BlockType flowingLava = new BlockType(10, Material.LAVA).setUnlocalizedName("flowing_lava");
    public static final BlockType lava = new BlockType(11, Material.LAVA).setUnlocalizedName("lava");
    public static final BlockType sand = new BlockType(12, Material.SAND).setUnlocalizedName("sand");
    public static final BlockType gravel = new BlockType(13, Material.SAND).setUnlocalizedName("gravel");
    public static final BlockType goldOre = new BlockType(14, Material.ROCK).setUnlocalizedName("gold_ore");
    public static final BlockType ironOre = new BlockType(15, Material.ROCK).setUnlocalizedName("iron_ore");
    public static final BlockType coalOre = new BlockType(16, Material.ROCK).setUnlocalizedName("coal_ore");

    private final short id;
    protected BlockStateFactory blockStateFactory;
    private BlockState defaultBlockState;
    private final BlockAttributes defaultAttributes;
    private String unlocalizedName;

    protected BlockType(int id, Material blockMaterialIn, MapColor blockMapColorIn) {
        this.id = (short) id;
        defaultAttributes = new BlockAttributes(blockMaterialIn, blockMapColorIn);
        REGISTRY.put(this.id, this);
    }

    public BlockType setUnlocalizedName(String name) {
        this.unlocalizedName = name;
        return this;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
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
        return new BlockStateFactory(this, new StateProperty<?>[0]);
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

    public void onFallenUpon(BlockPos pos, int entityIn, float fallDistance) {
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
