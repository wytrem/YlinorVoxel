package com.ylinor.library.api.terrain.block;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.artemis.World;
import com.ylinor.library.api.terrain.IBlockContainer;
import com.ylinor.library.api.terrain.block.material.EnumPushReaction;
import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.util.math.AxisAlignedBB;
import com.ylinor.library.util.math.BlockPos;

public class BlockAttributes {
	public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.f, 0.f, 0.f, 1.f, 1.f, 1.f);
	@Nullable
	public static final AxisAlignedBB NULL_AABB = null;

	private boolean fullBlock;
	private int lightOpacity;
	private boolean translucent;
	private int lightValue;
	private boolean useNeighborBrightness;
	private float blockHardness;
	private float blockResistance;
	private boolean enableStats;
	private boolean needsRandomTick;
	private boolean isBlockContainer;
	private float blockParticleGravity;
	private Material material;
	private MapColor blockMapColor;
	private float slipperiness;
	private RenderType renderType;
	private boolean isFullCube;
	private boolean isOpaqueCube;
	private boolean isCollidable;
	private AxisAlignedBB blockAabb;
	private AxisAlignedBB collisionBoundingBox;
	private boolean canCollideCheck;
	private BlockRenderLayer renderLayer;
	private int colorMultiplier;

	public BlockAttributes(Material materialIn, MapColor mapColorIn) {
		this.isOpaqueCube = true;
		this.fullBlock = isOpaqueCube;
		this.enableStats = true;
		this.blockParticleGravity = 1.0F;
		this.slipperiness = 0.6F;
		this.material = materialIn;
		this.blockMapColor = mapColorIn;
		this.lightOpacity = this.fullBlock ? 255 : 0;
		this.translucent = !materialIn.blocksLight();
		this.isFullCube = true;
		this.isCollidable = true;
		this.blockAabb = FULL_BLOCK_AABB;
		this.collisionBoundingBox = this.blockAabb;
		this.renderType = RenderType.BLOCKMODEL;
		this.lightValue = 0;
		this.renderLayer = BlockRenderLayer.SOLID;
		this.colorMultiplier = 0xffffff;
	}
	
	public int getColorMultiplier() {
        return colorMultiplier;
    }

    public void setColorMultiplier(int colorMultiplier) {
        this.colorMultiplier = colorMultiplier;
    }

    public BlockAttributes(BlockAttributes defaultAttributes) {
	    set(defaultAttributes);
	}

    public float getSlipperiness() {
		return slipperiness;
	}

	public Material getMaterial() {
		return material;
	}

	public void addCollisionBoxToList(IBlockContainer worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, int entityIn, boolean p_185477_7_) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, getCollisionBoundingBox(worldIn, pos));
	}

	protected static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox) {
		if (blockBox != NULL_AABB) {
			AxisAlignedBB axisalignedbb = blockBox.offset(pos);
			if (entityBox.intersectsWith(axisalignedbb)) {
				collidingBoxes.add(axisalignedbb);
			}
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockContainer blockContainer, BlockPos pos) {
		return blockAabb;
	}

	public boolean isFullBlock() {
		return this.fullBlock;
	}

	public int getLightOpacity() {
		return lightOpacity;
	}

	public int getLightValue() {
		return lightValue;
	}

	public boolean isTranslucent() {
		return translucent;
	}

	public boolean useNeighborBrightness() {
		return useNeighborBrightness;
	}

	public MapColor getMapColor() {
		return blockMapColor;
	}

	public boolean isFullCube() {
		return this.isFullCube;
	}

	public RenderType getRenderType() {
		return renderType;
	}

	boolean isBlockNormalCube() {
		return getMaterial().blocksMovement() && isFullCube();
	}

	boolean isNormalCube() {
		return getMaterial().isOpaque() && isFullCube();
	}

	float getBlockHardness(World worldIn, BlockPos pos) {
		return blockHardness;
	}

	EnumPushReaction getMobilityFlag() {
		return getMaterial().getMobilityFlag();
	}

	AxisAlignedBB getSelectedBoundingBox(IBlockContainer blockAccess, BlockPos pos) {
		return getBoundingBox(blockAccess, pos).offset(pos);
	}

	public boolean isOpaqueCube() {
		return this.isOpaqueCube;
	}

	@Nullable
	AxisAlignedBB getCollisionBoundingBox(IBlockContainer blockAccess, BlockPos pos) {
		return collisionBoundingBox;
	}

	boolean isFullyOpaque() {
		return getMaterial().isOpaque() && isFullCube();
	}
	
	public void setFullBlock(boolean fullBlock) {
		this.fullBlock = fullBlock;
	}

	public void setLightOpacity(int lightOpacity) {
		this.lightOpacity = lightOpacity;
	}

	public void setTranslucent(boolean translucent) {
		this.translucent = translucent;
	}

	public void setLightValue(int lightValue) {
		this.lightValue = lightValue;
	}

	public void setUseNeighborBrightness(boolean useNeighborBrightness) {
		this.useNeighborBrightness = useNeighborBrightness;
	}

	public void setBlockHardness(float blockHardness) {
		this.blockHardness = blockHardness;
	}

	public void setBlockResistance(float blockResistance) {
		this.blockResistance = blockResistance;
	}

	public void setEnableStats(boolean enableStats) {
		this.enableStats = enableStats;
	}

	public void setNeedsRandomTick(boolean needsRandomTick) {
		this.needsRandomTick = needsRandomTick;
	}

	public void setBlockContainer(boolean isBlockContainer) {
		this.isBlockContainer = isBlockContainer;
	}

	public void setBlockParticleGravity(float blockParticleGravity) {
		this.blockParticleGravity = blockParticleGravity;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setMapColor(MapColor blockMapColor) {
		this.blockMapColor = blockMapColor;
	}

	public void setSlipperiness(float slipperiness) {
		this.slipperiness = slipperiness;
	}

	public void setRenderType(RenderType renderType) {
		this.renderType = renderType;
	}

	public void setFullCube(boolean isFullCube) {
		this.isFullCube = isFullCube;
	}

	public boolean isCollidable() {
		return isCollidable;
	}

	public void setCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}

	public void setOpaqueCube(boolean isOpaqueCube) {
		this.isOpaqueCube = isOpaqueCube;
	}
	
	public boolean isCanCollideCheck() {
		return canCollideCheck;
	}

	public void setCanCollideCheck(boolean canCollideCheck) {
		this.canCollideCheck = canCollideCheck;
	}
	
	public AxisAlignedBB getCollisionBoundingBox() {
		return collisionBoundingBox;
	}

	public void setCollisionBoundingBox(AxisAlignedBB collisionBoundingBox) {
		this.collisionBoundingBox = collisionBoundingBox;
	}
	
	public BlockRenderLayer getRenderLayer() {
        return renderLayer;
    }

    public void setRenderLayer(BlockRenderLayer renderLayer) {
        this.renderLayer = renderLayer;
    }

    public void set(BlockAttributes defaultAttributes) {
		this.fullBlock = defaultAttributes.fullBlock;
		this.translucent = defaultAttributes.translucent;
		this.lightOpacity = defaultAttributes.lightOpacity;
		this.lightValue = defaultAttributes.lightValue;
		this.useNeighborBrightness = defaultAttributes.useNeighborBrightness;
		this.blockHardness = defaultAttributes.blockHardness;
		this.blockResistance = defaultAttributes.blockResistance;
		this.enableStats = defaultAttributes.enableStats;
		this.needsRandomTick = defaultAttributes.needsRandomTick;
		this.isBlockContainer = defaultAttributes.isBlockContainer;
		this.material = defaultAttributes.material;
		this.blockParticleGravity = defaultAttributes.blockParticleGravity;
		this.blockMapColor = defaultAttributes.blockMapColor;
		this.slipperiness = defaultAttributes.slipperiness;
		this.renderType = defaultAttributes.renderType;
		this.isFullCube = defaultAttributes.isFullCube;
		this.isOpaqueCube = defaultAttributes.isOpaqueCube;
		this.isCollidable = defaultAttributes.isCollidable;
		this.canCollideCheck = defaultAttributes.canCollideCheck;
		this.blockAabb = defaultAttributes.blockAabb;
		this.collisionBoundingBox = defaultAttributes.collisionBoundingBox;
		this.colorMultiplier = defaultAttributes.colorMultiplier;
	}

    public void setBoundingBox(AxisAlignedBB axisAlignedBB) {
        this.blockAabb = axisAlignedBB;
    }
}
