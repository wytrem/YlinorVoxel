package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.Subscribe;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.events.AssetsLoadedEvent;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.system.BaseSystem;


/**
 * World system that renders the terrain from the {@link CameraSystem} point of
 * view.
 *
 * @author wytrem
 */
@Singleton
public class TerrainRenderSystem extends BaseSystem {

    @Inject
    private Terrain terrain;

    @Inject
    private AssetsLoadingSystem assetsLoadingSystem;

    @Inject
    private YlinorClient client;

    private RenderGlobal renderGlobal;
    
    public ChunkRenderer getChunkRenderer(Chunk chunk) {
        return renderGlobal.getChunkRenderer(chunk);
    }

    @Override
    protected void processSystem() {
        if (assetsLoadingSystem.loaded()) {
            renderGlobal.render();
        }
    }

    public void dispose() {
        if (renderGlobal != null)
            renderGlobal.dispose();
    }
    
    @Subscribe
    public void assetsLoaded(AssetsLoadedEvent event) {
    	renderGlobal = new RenderGlobal();
        terrain.inject(world);
        world.injector.injectMembers(renderGlobal);
        renderGlobal.init(world);
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
