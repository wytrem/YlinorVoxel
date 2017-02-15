package com.ylinor.client.render;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IChunkProvider;
import com.ylinor.library.api.world.World;
import com.ylinor.library.api.world.blocks.BlockType;
import com.ylinor.library.util.math.PositionableObject2D;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class Test implements IChunkProvider {

    private World world;

    public Test() {
    }

    public void setWorld(World w) {
        this.world = w;
    }

    private TLongObjectMap<Chunk> chunkMap = new TLongObjectHashMap<Chunk>();

    public Chunk getChunk(int x, int z) {
        long id = chunkXZ2Int(x, z);

        Chunk chunk = chunkMap.get(id);

        if (chunk == null) {
            chunk = generateAt(x, z);
            chunkMap.put(id, chunk);
        }

        return chunk;
    }

    private Chunk generateAt(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(world, chunkX, chunkZ);

        //        short id = (short) (Math.abs(chunkX % 3 + (chunkZ % 3) * 3) + 1);

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {
                    chunk.setBlockType(x, y, z, world.getBlockType(BlockType.stone.getId()));
                    //                    chunk.setBlockType(x, y, z, world.getBlockType(id));
                }
            }
        }

        return chunk;
    }

    /**
     * converts a chunk coordinate pair to a long (suitable for hashing)
     */
    public static long chunkXZ2Int(int chunkX, int chunkZ) {
        return chunkX & 4294967295L | (chunkZ & 4294967295L) << 32;
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos) {
        return getChunk(pos.x(), pos.y());
    }
}
