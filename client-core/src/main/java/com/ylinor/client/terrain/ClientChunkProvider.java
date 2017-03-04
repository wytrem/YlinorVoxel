package com.ylinor.client.terrain;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import com.ylinor.library.api.events.terrain.ChunkLoadedEvent;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.IChunkProvider;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.math.PositionableObject2D;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import net.mostlyoriginal.api.event.common.EventSystem;


public class ClientChunkProvider implements IChunkProvider {
    @Wire
    private Terrain terrain;

    @Wire
    private EventSystem eventSystem;

    public ClientChunkProvider() {
    }

    private TLongObjectMap<Chunk> chunkMap = new TLongObjectHashMap<Chunk>();

    public Chunk getChunk(int x, int z) {
        long id = chunkXZ2Int(x, z);

        Chunk chunk = chunkMap.get(id);

        if (chunk == null) {
            chunk = generateAt(x, z);
            chunkMap.put(id, chunk);
            eventSystem.dispatch(new ChunkLoadedEvent(chunk));
        }

        return chunk;
    }

    private Chunk generateAt(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(terrain, chunkX, chunkZ);

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {
                    if (y < 255) {
                        chunk.setBlockType(x, y, z, terrain.getBlockType((short) (BlockType.stone.getId())));
                    }
                    else {
                        chunk.setBlockType(x, y, z, terrain.getBlockType((short) (MathUtils.randomBoolean(0.01f) ? BlockType.dirt.getId() : 0)));
                    }
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
