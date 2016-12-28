package com.ylinor.library.api.world.provider;

import java.util.Random;

import org.joml.Vector2i;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class RandomChunkProvider implements IChunkProvider {
    private Random random = new Random();

    private TLongObjectMap<Chunk> chunkMap = new TLongObjectHashMap<>();

    @Override
    public Chunk provide(World world, int x, int z) {
        long id = chunkXZ2Long(x, z);
        Chunk chunk = chunkMap.get(id);
        if (chunk == null) {
            chunk = generateAt(world, x, z);
            chunkMap.put(id, chunk);
        }
        return chunk;
    }

    private Chunk generateAt(World world, int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(world, new Vector2i(chunkX, chunkZ));

        for (int x = 0; x < chunk.getSizeX(); x++) {
            for (int y = 0; y < World.MAX_HEIGHT; y++) {
                for (int z = 0; z < chunk.getSizeZ(); z++) {
                    chunk.setBlockId(x, y, z, (short) random.nextInt(1));
                }
            }
        }

        return chunk;
    }

    /** * converts a chunk coordinate pair to a long (suitable for hashing) */
    public static long chunkXZ2Long(int chunkX, int chunkZ) {
        return chunkX & 4294967295L | (chunkZ & 4294967295L) << 32;
    }
}
