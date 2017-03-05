package com.ylinor.library.api.terrain.block.type;

import com.artemis.World;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.BlockAttributes;
import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.util.math.BlockPos;

import gnu.trove.map.hash.TShortObjectHashMap;

public class BlockType {
	public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();
	public static final BlockType air = new BlockTypeAir(0);
	public static final BlockType stone = new BlockType(1);
	public static final BlockType dirt = new BlockType(2);
	public static final BlockType wood = new BlockType(3);
	public static final BlockType stoneBricks = new BlockType(4);
	public static final BlockType bricks = new BlockType(5);
	public static final BlockType sand = new BlockType(6);
	public static final BlockType gravel = new BlockType(7);
	public static final BlockType sponge = new BlockType(8);
	public static final BlockType ice = new BlockType(9);

	private final short id;
	protected final BlockStateFactory blockStateFactory;
	private BlockState defaultBlockState;
	private BlockAttributes defaultAttributes;

	protected final Material blockMaterial;
	protected final MapColor blockMapColor;

	protected BlockType(int id, Material blockMaterialIn, MapColor blockMapColorIn) {
		this.id = (short) id;
		this.blockStateFactory = createStateFactory();
		REGISTRY.put(this.id, this);
		setDefaultBlockState(blockStateFactory.getOneState());
		defaultAttributes = new BlockAttributes(blockMaterialIn, blockMapColorIn);
	}

	protected BlockType(int id, Material materialIn) {
		this(id, materialIn, materialIn.getMaterialMapColor());
	}

	public MapColor getBlockMapColor() {
		return blockMapColor;
	}

	public Material getBlockMaterial() {
		return blockMaterial;
	}

	protected BlockStateFactory createStateFactory() {
		return new BlockStateFactory(this);
	}

	public void setDefaultBlockState(BlockState defaultBlockState) {
		this.defaultBlockState = defaultBlockState;
	}

	public BlockState getDefaultState() {
		return defaultBlockState;
	}

	public short getId() {
		return id;
	}

	public void onFallenUpon(World worldIn, BlockPos pos, int entityIn, float fallDistance) {
		// entityIn.fall(fallDistance, 1.0F);
		System.out.println("entity " + entityIn + " fell on " + this.getId());
	}

	public BlockAttributes getDefaultAttributes() {
		return null;
	}
}
