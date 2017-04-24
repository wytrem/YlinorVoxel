package com.ylinor.client.terrain;

import java.io.File;

import com.ylinor.library.api.terrain.ChunkProviderReader;
import com.ylinor.library.api.terrain.Terrain;


public class ClientTerrain extends Terrain {

    private ChunkProviderReader clientChunkProvider;

    public ClientTerrain() {
        super(new ChunkProviderReader(new File(".")));
        this.clientChunkProvider = (ChunkProviderReader) storage;
    }

    public ChunkProviderReader getClientChunkProvider() {
        return clientChunkProvider;
    }
}
