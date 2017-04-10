package com.ylinor.server;

import java.io.File;

import com.ylinor.library.api.terrain.ChunkProviderReader;
import com.ylinor.library.api.terrain.Terrain;


public class ServerTerrain extends Terrain {
    private ChunkProviderReader serverChunkProvider;

    public ServerTerrain(File regionsFolder) {
        super(new ChunkProviderReader(regionsFolder));
        this.serverChunkProvider = (ChunkProviderReader) storage;
    }

    public ChunkProviderReader getClientChunkProvider() {
        return serverChunkProvider;
    }
}
