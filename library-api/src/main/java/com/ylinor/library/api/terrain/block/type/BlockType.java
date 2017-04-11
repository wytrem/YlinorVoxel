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
    public static final BlockType air = new BlockTypeAir(0).setModelName("air");
    public static final BlockType stone = new BlockTypeStone(1).setModelName("stone");
    public static final BlockType grass = new BlockTypeGrass(2).setModelName("grass");
    public static final BlockType dirt = new BlockTypeDirt(3).setModelName("dirt");
    public static final BlockType cobbleStone = new BlockTypeCobbleStone(4).setModelName("cobblestone");
    public static final BlockType planks = new BlockTypePlanks(5).setModelName("planks");
    public static final BlockType sapling = new BlockTypeSapling(6).setModelName("sapling");
    public static final BlockType bedrock = new BlockType(7, Material.ROCK).setModelName("bedrock");
    public static final BlockType flowingWater = new BlockType(8, Material.WATER).setModelName("flowing_water");
    public static final BlockType water = new BlockType(9, Material.WATER).setModelName("water");
    public static final BlockType flowingLava = new BlockType(10, Material.LAVA).setModelName("flowing_lava");
    public static final BlockType lava = new BlockType(11, Material.LAVA).setModelName("lava");
    public static final BlockType sand = new BlockType(12, Material.SAND).setModelName("sand");
    public static final BlockType gravel = new BlockType(13, Material.SAND).setModelName("gravel");
    public static final BlockType goldOre = new BlockType(14, Material.ROCK).setModelName("gold_ore");
    public static final BlockType ironOre = new BlockType(15, Material.ROCK).setModelName("iron_ore");
    public static final BlockType coalOre = new BlockType(16, Material.ROCK).setModelName("coal_ore");
    public static final BlockType oldLog = new BlockTypeOldLog(17).setModelName("old_log");
    public static final BlockType oldLeaves = new BlockTypeOldLeaves(18).setModelName("old_leaves");
    public static final BlockType sponge = new BlockType(19, Material.SPONGE).setModelName("sponge");
    public static final BlockType glass = new BlockType(20, Material.GLASS).setModelName("glass");
    public static final BlockType lapisOre = new BlockType(21, Material.ROCK).setModelName("lapis_ore");
    public static final BlockType lapisBlock = new BlockType(22, Material.ROCK).setModelName("lapis_block");
    public static final BlockType dispenser = new BlockType(23).setModelName("dispenser");
    public static final BlockType sandstone = new BlockType(24).setModelName("sandstone");
    public static final BlockType noteblock = new BlockType(25).setModelName("noteblock");
    public static final BlockType bed = new BlockType(26).setModelName("bed");
    public static final BlockType goldenRail = new BlockType(27).setModelName("golden_rail");
    public static final BlockType detectorRail = new BlockType(28).setModelName("detector_rail");
    public static final BlockType stickyPiston = new BlockType(29).setModelName("sticky_piston");
    public static final BlockType web = new BlockType(30).setModelName("web");
    public static final BlockType tallgrass = new BlockTallGrass(31).setModelName("tallgrass");
    public static final BlockType deadBush = new BlockType(32).setModelName("dead_bush");
    public static final BlockType piston = new BlockType(33).setModelName("piston_normal");
    // 34: piston head
    public static final BlockType wool = new BlockType(35).setModelName("wool");
    // 36: piston moving
    public static final BlockType yellowFlower = new BlockType(37).setModelName("yellow_flower");
    public static final BlockType redFlower = new BlockType(38).setModelName("red_flower");
    public static final BlockType brownMushroom = new BlockType(39).setModelName("brown_mushroom");
    public static final BlockType redMushroom = new BlockType(40).setModelName("red_mushroom");
    public static final BlockType goldBlock = new BlockType(41).setModelName("gold_block");
    public static final BlockType ironBlock = new BlockType(42).setModelName("iron_block");
    public static final BlockType doubleStoneSlab = new BlockType(43).setModelName("double_stone");
    public static final BlockType stoneSlab = new BlockType(44).setModelName("half_slab_stone");
    public static final BlockType brickBlock = new BlockType(45).setModelName("brick");
    public static final BlockType tnt = new BlockType(46).setModelName("tnt");
    public static final BlockType bookshelf = new BlockType(47).setModelName("bookshelf");
    public static final BlockType mossyCobblestone = new BlockType(48).setModelName("mossy_cobblestone");
    public static final BlockType obsidian = new BlockType(49).setModelName("obsidian");
    public static final BlockType torch = new BlockType(50).setModelName("normal_torch");
    public static final BlockType fire = new BlockType(51).setModelName("fire");
    public static final BlockType mobSpawner = new BlockType(52).setModelName("mob_spawner");
    public static final BlockType oakStairs = new BlockType(53).setModelName("oak_stairs");
    public static final BlockType chest = new BlockType(54).setModelName("chest");
    public static final BlockType redstoneWire = new BlockType(55).setModelName("redstone_dot");
    public static final BlockType diamondOre = new BlockType(56).setModelName("diamond_ore");
    public static final BlockType diamondBlock = new BlockType(57).setModelName("diamond_block");
    public static final BlockType craftingTable = new BlockType(58).setModelName("crafting_table");
    public static final BlockType wheat = new BlockType(59).setModelName("wheat_stage_0");
    public static final BlockType farmland = new BlockType(60).setModelName("farmland_dry");
    public static final BlockType furnace = new BlockType(61).setModelName("furnace");
    public static final BlockType litFurnace = new BlockType(62).setModelName("lit_furnace");
    public static final BlockType standingSign = new BlockType(63).setModelName("sign");
    public static final BlockType doorWood = new BlockType(64).setModelName("wooden_door_bottom");
    public static final BlockType ladder = new BlockType(65).setModelName("ladder");
    public static final BlockType rail = new BlockType(66).setModelName("normal_rail_flat");
    public static final BlockType stoneStairs = new BlockType(67).setModelName("stone_stairs");
    public static final BlockType wallSign = new BlockType(68).setModelName("sign");
    public static final BlockType lever = new BlockType(69).setModelName("lever");
    public static final BlockType stonePressurePlate = new BlockType(70).setModelName("stone_pressure_plate_up");
    public static final BlockType ironDoor = new BlockType(71).setModelName("iron_door_bottom");
    public static final BlockType woodePressurePlate = new BlockType(72).setModelName("wooden_pressure_plate_up");
    public static final BlockType redstoneOre = new BlockType(73).setModelName("redstone_ore");
    public static final BlockType litRedstoneOre = new BlockType(74).setModelName("lit_redstone_ore");
    public static final BlockType unlitRedstoneTorch = new BlockType(75).setModelName("unlit_redstone_torch");
    public static final BlockType redstoneTorch = new BlockType(76).setModelName("redstone_torch");
    public static final BlockType stoneButton = new BlockType(77).setModelName("stone_button");
    public static final BlockType snowLayer = new BlockType(78).setModelName("snow_layer");
    public static final BlockType ice = new BlockType(79).setModelName("ice");
    public static final BlockType snow = new BlockType(80).setModelName("snow");
    public static final BlockType cactus = new BlockType(81).setModelName("cactus");
    public static final BlockType clay = new BlockType(82).setModelName("clay");
    public static final BlockType reeds = new BlockType(83).setModelName("reeds");
    public static final BlockType jukebox = new BlockType(84).setModelName("jukebox");
    public static final BlockType fence = new BlockType(85).setModelName("oak_fence_side");
//    public static final BlockType pumpkin = new BlockType(86).setModelName("pumpkin");

    // 144: Skull
    public static final BlockType anvil = new BlockType(145).setModelName("anvil_undamaged");
    public static final BlockType trappedChest = new BlockType(146).setModelName("chest");
    public static final BlockType lightWeightedPressurePlate = new BlockType(147).setModelName("light_weighted_pressure_plate_up");
    public static final BlockType heavyWeightedPressurePlate = new BlockType(148).setModelName("heavy_weighted_pressure_plate_up");
    public static final BlockType unpoweredComparator = new BlockType(149).setModelName("comparator_unlit");
    public static final BlockType poweredComparator = new BlockType(150).setModelName("comparator_lit");
    public static final BlockType daylightDetector = new BlockType(151).setModelName("daylight_detector");
    public static final BlockType redstoneBlock = new BlockType(152).setModelName("redstone_block");
    public static final BlockType quartzOre = new BlockType(153).setModelName("quartz_ore");
    public static final BlockType hopper = new BlockType(154).setModelName("hopper");
    public static final BlockType quartzBlock = new BlockType(155).setModelName("quartz_block");
    public static final BlockType quartzStairs = new BlockType(156).setModelName("quartz_stairs");
    public static final BlockType activatorRail = new BlockType(157).setModelName("activator_rail");
    public static final BlockType dropper = new BlockType(158).setModelName("dropper");
    public static final BlockType hardenedClay = new BlockType(159).setModelName("hardened_clay");
    public static final BlockType glassPane = new BlockType(160).setModelName("glass_pane_post");
    public static final BlockType leaves2 = new BlockTypeOldLeaves(161).setModelName("old_leaves");
    public static final BlockType log2 = new BlockTypeOldLog(162).setModelName("old_log");
    public static final BlockType acaciaStairs = new BlockType(163).setModelName("acacia_stairs");
    public static final BlockType darkOakStairs = new BlockType(164).setModelName("dark_oak_stairs");

    private final short id;
    protected BlockStateFactory blockStateFactory;
    private BlockState defaultBlockState;
    private final BlockAttributes defaultAttributes;
    private String modelName;

    protected BlockType(int id, Material blockMaterialIn, MapColor blockMapColorIn) {
        this.id = (short) id;
        defaultAttributes = new BlockAttributes(blockMaterialIn, blockMapColorIn);
        REGISTRY.put(this.id, this);
    }
    
    public BlockType setModelName(String name) {
        this.modelName = name;
        return this;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    protected final void initStateFactory() {
        this.blockStateFactory = createBlockState();
        setDefaultState(blockStateFactory.getOneState());
    }
    
    protected void initStates() {
        
    }

    
    protected BlockType(int id) {
        this(id, Material.ROCK);
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
