package com.ylinor.library.api.world;

/**
 *
 */


public class ChunkCache {
    private Chunk center, xPos, xNeg, zPos, zNeg;

    public ChunkCache(Chunk chunk, World world) {
        center = chunk;
        xPos = world.getChunkAt(chunk.x + 1, chunk.z);
        xNeg = world.getChunkAt(chunk.x - 1, chunk.z);
        zPos = world.getChunkAt(chunk.x, chunk.z + 1);
        zNeg = world.getChunkAt(chunk.x, chunk.z - 1);
    }

    public int get(int x, int y, int z) {
        if (x < 0) {
            return xNeg.get(x + Chunk.SIZE_X, y, z);
        }
        if (z < 0) {
            return zNeg.get(x, y, z + Chunk.SIZE_Z);
        }
        if (x >= Chunk.SIZE_X) {
            return xPos.get(x - Chunk.SIZE_X, y, z);
        }
        if (z >= Chunk.SIZE_Z) {
            return zPos.get(x, y, z - Chunk.SIZE_Z);
        }
        return center.get(x, y, z);
    }
}

