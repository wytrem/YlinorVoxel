package com.ylinor.library.api.terrain;

import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.math.BlockPos;


public class Terrain implements IChunkProvider, IBlockContainer {
    public static final short SIZE_Y = Chunk.SIZE_Y;

    protected IChunkProvider storage;

    public Terrain(IChunkProvider storage) {
        this.storage = storage;
    }
    
    public void inject(World world) {
        world.injector.injectMembers(storage);
    }

    public void setStorage(IChunkProvider storage) {
        this.storage = storage;
    }

    public Chunk getChunkOf(BlockPos pos) {
        return getChunk(pos.x >> 4, pos.z >> 4);
    }

    public Chunk getChunkOf(int x, int y, int z) {
        return getChunk(x >> 4, z >> 4);
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
}
