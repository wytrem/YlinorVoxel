package com.ylinor.server;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.ylinor.library.api.terrain.ChunkProviderReader;
import com.ylinor.library.api.terrain.Terrain;

@Singleton
public class ServerTerrain extends Terrain {
    @Inject
    public ServerTerrain(@Named("mapFolder") File mapFolder) {
        super(new ChunkProviderReader(mapFolder));
    }
}
