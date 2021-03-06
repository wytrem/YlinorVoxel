package com.ylinor.client.terrain;

import javax.inject.Inject;

import com.badlogic.gdx.math.MathUtils;
import com.ylinor.library.api.events.terrain.ChunkLoadedEvent;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.IChunkProvider;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.ecs.system.EventSystem;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class ClientChunkProvider implements IChunkProvider {
    @Inject
    private Terrain terrain;

    @Inject
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
                    if (y < 115) {
                        chunk.setBlockType(x, y, z, BlockType.dirt);
                    }
                    else if (y < 120) {
                        if (MathUtils.randomBoolean(0.01f)) {
                            chunk.setBlockType(x, y, z, terrain.getBlockType((short) MathUtils.random(100, 105)));
                        }
                    }
                }
            }
        }
        if (chunkX == 0 && chunkZ == 0) {
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    chunk.setBlockType(8 + x, 115 + 3, 8 + z, BlockType.oldLeaves);
                }
            }

            chunk.setBlockType(8 - 1, 115 + 4, 8, BlockType.oldLeaves);
            chunk.setBlockType(8 + 1, 115 + 4, 8, BlockType.oldLeaves);
            chunk.setBlockType(8, 115 + 4, 8 - 1, BlockType.oldLeaves);
            chunk.setBlockType(8, 115 + 4, 8 + 1, BlockType.oldLeaves);
            chunk.setBlockType(8, 115 + 5, 8, BlockType.oldLeaves);

            for (int y = 0; y < 4; y++) {
                chunk.setBlockType(8, 115 + y, 8, BlockType.oldLog);
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
}
