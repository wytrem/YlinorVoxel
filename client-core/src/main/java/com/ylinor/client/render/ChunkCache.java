package com.ylinor.client.render;

import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.IBlockContainer;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.Block;
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
    public Block getBlock(int x, int y, int z) {
        if (x < 0) {
            return xNeg.getBlock(x + Chunk.SIZE_X, y, z);
        }

        if (z < 0) {
            return zNeg.getBlock(x, y, z + Chunk.SIZE_Z);
        }

        if (x >= Chunk.SIZE_X) {
            return xPos.getBlock(x - Chunk.SIZE_X, y, z);
        }

        if (z >= Chunk.SIZE_Z) {
            return zPos.getBlock(x, y, z - Chunk.SIZE_Z);
        }

        return center.getBlock(x, y, z);
    }

    @Override
    public Block getOrCreate(int x, int y, int z) {
        if (x < 0) {
            return xNeg.getOrCreate(x + Chunk.SIZE_X, y, z);
        }

        if (z < 0) {
            return zNeg.getOrCreate(x, y, z + Chunk.SIZE_Z);
        }

        if (x >= Chunk.SIZE_X) {
            return xPos.getOrCreate(x - Chunk.SIZE_X, y, z);
        }

        if (z >= Chunk.SIZE_Z) {
            return zPos.getOrCreate(x, y, z - Chunk.SIZE_Z);
        }

        return center.getOrCreate(x, y, z);
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
    public BlockState getBlockData(int x, int y, int z) {
        if (x < 0) {
            return xNeg.getBlockData(x + Chunk.SIZE_X, y, z);
        }

        if (z < 0) {
            return zNeg.getBlockData(x, y, z + Chunk.SIZE_Z);
        }

        if (x >= Chunk.SIZE_X) {
            return xPos.getBlockData(x - Chunk.SIZE_X, y, z);
        }

        if (z >= Chunk.SIZE_Z) {
            return zPos.getBlockData(x, y, z - Chunk.SIZE_Z);
        }

        return center.getBlockData(x, y, z);
    }

    @Override
    public void setBlock(Block block) {
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockState data) {
    }
}
