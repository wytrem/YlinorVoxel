package com.ylinor.client.terrain;

import com.ylinor.library.api.terrain.Terrain;


public class ClientTerrain extends Terrain {

    private ClientChunkProvider clientChunkProvider;

    public ClientTerrain() {
        super(new ClientChunkProvider());
        this.clientChunkProvider = (ClientChunkProvider) storage;
    }

    public ClientChunkProvider getClientChunkProvider() {
        return clientChunkProvider;
    }
}
