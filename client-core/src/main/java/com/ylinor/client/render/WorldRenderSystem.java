package com.ylinor.client.render;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.input.GdxInputDispatcherSystem;
import com.ylinor.client.input.PlayerInputSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Terrain;


/**
 * World system that renders the terrain from the {@link CameraSystem} point of
 * view.
 *
 * @author wytrem
 */
public class WorldRenderSystem extends BaseSystem {

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

    @Override
    protected void initialize() {
        renderGlobal = new RenderGlobal(terrain);
        terrain.inject(world);
        world.inject(renderGlobal);
    }

    @Override
    protected void processSystem() {
        if (assetsLoadingSystem.loaded()) {
            renderGlobal.render();
        }
    }

    public void dispose() {
        renderGlobal.dispose();
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
