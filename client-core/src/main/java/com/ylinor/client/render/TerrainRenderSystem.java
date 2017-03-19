package com.ylinor.client.render;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.events.AssetsLoadedEvent;
import com.ylinor.client.input.GdxInputDispatcherSystem;
import com.ylinor.client.input.PlayerInputSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;

import net.mostlyoriginal.api.event.common.Subscribe;


/**
 * World system that renders the terrain from the {@link CameraSystem} point of
 * view.
 *
 * @author wytrem
 */
public class TerrainRenderSystem extends BaseSystem {

    @Wire
    private Terrain terrain;

    /**
     * L'instance des assets
     */
    @Wire
    private Assets assets;

    @Wire
    private AssetsLoadingSystem assetsLoadingSystem;

    @Wire
    private PlayerInputSystem playerInputSystem;

    @Wire
    private GdxInputDispatcherSystem inputDispatcherSystem;

    @Wire
    private CameraSystem cameraSystem;

    @Wire
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
        world.inject(renderGlobal);
        renderGlobal.init(world);
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
