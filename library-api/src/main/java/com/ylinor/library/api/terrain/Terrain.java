package com.ylinor.library.api.terrain;

import java.util.ArrayList;
import java.util.List;

import com.artemis.World;
import com.ylinor.library.api.terrain.block.Block;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.TempVars;
import com.ylinor.library.util.math.AxisAlignedBB;
import com.ylinor.library.util.math.BlockPos;
import com.ylinor.library.util.math.MathHelper;
import com.ylinor.library.util.math.PositionableObject2D;


public class Terrain implements IChunkProvider, IBlockContainer {
    public static final short SIZE_Y = Chunk.SIZE_Y;

    protected IChunkProvider storage;

    public Terrain(IChunkProvider storage) {
        this.storage = storage;
    }

    public void inject(World world) {
        world.inject(storage);
    }

    public void setStorage(IChunkProvider storage) {
        this.storage = storage;
    }

    public Chunk getChunkOf(Block block) {
        return getChunkOf(block.getPos());
    }

    public Chunk getChunkOf(BlockPos pos) {
        return getChunk(pos.x >> 4, pos.z >> 4);
    }

    public Chunk getChunkOf(int x, int y, int z) {
        return getChunk(x >> 4, z >> 4);
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos) {
        return storage.getChunk(pos);
    }

    @Override
    public Chunk getChunk(int x, int z) {
        return storage.getChunk(x, z);
    }

    public IChunkProvider getStorage() {
        return storage;
    }

    public BlockType getBlockType(float x, float y, float z) {
        return getBlockType((int) x, (int) y, (int) z);
    }

    @Override
    public Block getBlock(BlockPos pos) {
        return getChunkOf(pos).getBlock(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return getChunkOf(x, y, z).getBlock(x & 15, y, z & 15);
    }

    @Override
    public Block getOrCreate(BlockPos pos) {
        return getChunkOf(pos).getOrCreate(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public Block getOrCreate(int x, int y, int z) {
        return getChunkOf(x, y, z).getOrCreate(x & 15, y, z & 15);
    }

    @Override
    public BlockType getBlockType(BlockPos pos) {
        return getChunkOf(pos).getBlockType(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        return getChunkOf(x, y, z).getBlockType(x & 15, y, z & 15);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return getChunkOf(pos).getBlockState(pos.x & 15, pos.y, pos.z & 15);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return getChunkOf(x, y, z).getBlockState(x & 15, y, z & 15);
    }

    @Override
    public void setBlock(Block block) {
        Chunk chunk = getChunkOf(block.getPos());

        chunk.setBlockType(block.getPos().x & 15, block.getPos().y, block.getPos().z & 15, block.getType());
        chunk.setBlockState(block.getPos().x & 15, block.getPos().y, block.getPos().z & 15, block.getData());
    }

    @Override
    public void setBlockType(BlockPos pos, BlockType type) {
        getChunkOf(pos).setBlockType(pos.x & 15, pos.y, pos.z & 15, type);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        getChunkOf(x, y, z).setBlockType(x & 15, y, z & 15, type);
    }

    @Override
    public void setBlockState(BlockPos pos, BlockState data) {
        getChunkOf(pos).setBlockState(pos.x & 15, pos.y, pos.z & 15, data);
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data) {
        getChunkOf(x, y, z).setBlockState(x & 15, y, z & 15, data);
    }

    public BlockType getBlockType(short id) {
        return BlockType.REGISTRY.get(id);
    }

    public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB aabb) {
        return getCollisionBoxes(aabb, -1);
    }

    public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB aabb, int entityId) {
        List<AxisAlignedBB> list = new ArrayList<>();
        int minX = MathHelper.floor_double(aabb.minX) - 1;
        int maxX = MathHelper.ceiling_double_int(aabb.maxX) + 1;
        int minY = MathHelper.floor_double(aabb.minY) - 1;
        int maxY = MathHelper.ceiling_double_int(aabb.maxY) + 1;
        int minZ = MathHelper.floor_double(aabb.minZ) - 1;
        int maxZ = MathHelper.ceiling_double_int(aabb.maxZ) + 1;
        TempVars tempVars = TempVars.get();

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int i2 = (x != minX && x != maxX - 1 ? 0 : 1) + (z != minZ && z != maxZ - 1 ? 0 : 1);
                if (i2 != 2) {
                    for (int y = minY; y < maxY; ++y) {
                        if (i2 <= 0 || y != minY && y != maxY - 1) {
                            tempVars.blockPos0.set(x, y, z);

                            getBlockState(tempVars.blockPos0).getAttributes()
                                                             .addCollisionBoxToList(this, tempVars.blockPos0, aabb, list, entityId, false);
                        }
                    }
                }
            }
        }

        tempVars.release();
        return list;
    }
}
