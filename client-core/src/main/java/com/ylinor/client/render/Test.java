package com.ylinor.client.render;

import com.badlogic.gdx.math.MathUtils;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;
import com.ylinor.library.api.world.storage.StorageManager;
import com.ylinor.library.util.math.PositionableObject2D;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

public class Test  extends StorageManager {

    private World world;

    public Test() {
    }
    
    public void setWorld(World w)
    {
        this.world = w;
    }
    
    private TLongObjectMap<Chunk> chunkMap = new TLongObjectHashMap<Chunk>();

    public Chunk getChunk(int x, int z)
    {
        long id = chunkXZ2Int(x, z);

        Chunk chunk = chunkMap.get(id);

        if (chunk == null)
        {
            chunk = generateAt(x, z);
            chunkMap.put(id, chunk);
        }

        return chunk;
    }

    private Chunk generateAt(int chunkX, int chunkZ)
    {
        Chunk chunk = new Chunk(world, chunkX, chunkZ);

        for (int x = 0; x < Chunk.SIZE_X; x++)
        {
            for (int y = 0; y < Chunk.SIZE_Y; y++)
            {
                for (int z = 0; z < Chunk.SIZE_Z; z++)
                {
                    chunk.setBlockType(x, y, z, world.getBlockType((short) (MathUtils.random(20) == 0 ? 1 : 0)));
                }
            }
        }

        return chunk;
    }

    /**
     * converts a chunk coordinate pair to a long (suitable for hashing)
     */
    public static long chunkXZ2Int(int chunkX, int chunkZ)
    {
        return chunkX & 4294967295L | (chunkZ & 4294967295L) << 32;
    }

    @Override
    public Chunk getChunk(PositionableObject2D pos) {
        return getChunk(pos.x(), pos.y());
    }

    @Override
    public void setChunk(PositionableObject2D pos, Chunk chunk) {
        
    }

    @Override
    public void setChunk(int x, int z, Chunk chunk) {
        
    }
}
