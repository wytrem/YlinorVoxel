package com.ylinor.library.api.terrain;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import com.ylinor.library.api.events.terrain.ChunkLoadedEvent;
import com.ylinor.library.api.terrain.mc.McChunkLoader;
import com.ylinor.library.util.ecs.EventSystem;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

public class ChunkProviderReader implements IChunkProvider {
    @Inject
    private Terrain terrain;

    @Inject
    private EventSystem eventSystem;

    private File regionsFolder;
    
    public ChunkProviderReader(File regionsFolder) {
        this.regionsFolder = regionsFolder;
    }   

    private TLongObjectMap<Chunk> chunkMap = new TLongObjectHashMap<Chunk>();

    public @NotNull Chunk getChunk(int x, int z) {
        long id = chunkXZ2Int(x, z);

        Chunk chunk = chunkMap.get(id);

        if (chunk == null) {
            try {
                chunk = McChunkLoader.loadChunk(terrain, x, z, regionsFolder);
            } catch (IOException e) {
                e.printStackTrace();

                chunk = new Chunk(terrain, x, z);
            }

            chunkMap.put(id, chunk);
            eventSystem.dispatch(new ChunkLoadedEvent(chunk));
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
