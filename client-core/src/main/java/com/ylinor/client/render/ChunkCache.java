package com.ylinor.client.render;

import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.IBlockContainer;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;


public class ChunkCache implements IBlockContainer {
    private Chunk center, xPos, xNeg, zPos, zNeg;

    public ChunkCache(Chunk chunk, Terrain terrain) {
        center = chunk;
        xPos = terrain.getChunk(chunk.x + 1, chunk.z);
        xNeg = terrain.getChunk(chunk.x - 1, chunk.z);
        zPos = terrain.getChunk(chunk.x, chunk.z + 1);
        zNeg = terrain.getChunk(chunk.x, chunk.z - 1);
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        if (x < 0) {
            return xNeg.getBlockType(x + Chunk.SIZE_X, y, z);
        }

        if (z < 0) {
            return zNeg.getBlockType(x, y, z + Chunk.SIZE_Z);
        }

        if (x >= Chunk.SIZE_X) {
            return xPos.getBlockType(x - Chunk.SIZE_X, y, z);
        }

        if (z >= Chunk.SIZE_Z) {
            return zPos.getBlockType(x, y, z - Chunk.SIZE_Z);
        }

        return center.getBlockType(x, y, z);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        if (x < 0) {
            return xNeg.getBlockState(x + Chunk.SIZE_X, y, z);
        }

        if (z < 0) {
            return zNeg.getBlockState(x, y, z + Chunk.SIZE_Z);
        }

        if (x >= Chunk.SIZE_X) {
            return xPos.getBlockState(x - Chunk.SIZE_X, y, z);
        }

        if (z >= Chunk.SIZE_Z) {
            return zPos.getBlockState(x, y, z - Chunk.SIZE_Z);
        }

        return center.getBlockState(x, y, z);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data) {
    }
}
