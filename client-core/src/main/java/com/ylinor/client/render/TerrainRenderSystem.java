package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.Subscribe;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.events.AssetsLoadedEvent;
import com.ylinor.client.input.GdxInputDispatcherSystem;
import com.ylinor.client.input.PlayerInputSystem;
import com.ylinor.client.resource.Assets;
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

    /**
     * L'instance des assets
     */
    @Inject
    private Assets assets;

    @Inject
    private AssetsLoadingSystem assetsLoadingSystem;

    @Inject
    private PlayerInputSystem playerInputSystem;

    @Inject
    private GdxInputDispatcherSystem inputDispatcherSystem;

    @Inject
    private CameraSystem cameraSystem;

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
