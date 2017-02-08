package com.ylinor.library.api.terrain.storage;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.util.math.PositionableObject2D;


public final class StorageUtil {
    public static long chunkXZToLong(PositionableObject2D pos) {
        return pos.x() & 4294967295L | (pos.y() & 4294967295L) << 32;
    }

    public static long chunkXZToLong(int chunkX, int chunkZ) {
        return chunkX & 4294967295L | (chunkZ & 4294967295L) << 32;
    }

    public static long posToLong(BlockPos pos) {
        return pos.toLong();
    }

    public static long posToLong(int x, int y, int z) {
        return BlockPos.toLong(x, y, z);
    }
}
