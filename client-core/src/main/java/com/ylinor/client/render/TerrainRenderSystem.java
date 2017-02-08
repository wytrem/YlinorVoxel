package com.ylinor.client.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.client.events.GdxPauseEvent;
import com.ylinor.client.events.GdxResizeEvent;
import com.ylinor.client.events.GdxResumeEvent;
import com.ylinor.client.physics.AABB;
import com.ylinor.client.physics.PlayerInputSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Terrain;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;


/**
 * World system that renders the terrain from the {@link CameraSystem} point of
 * view.
 *
 * @author wytrem
 */
public class TerrainRenderSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(TerrainRenderSystem.class);

    @Wire
    private Terrain terrain;

    /**
     * L'instance des assets
     */
    @Wire
    private Assets assets;

    @Wire
    private EventSystem eventSystem;

    @Wire
    private AssetsLoadingSystem assetsLoadingSystem;

    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private PlayerInputSystem playerInputSystem;

    @Wire
    private CameraSystem cameraSystem;

    /**
     * Current screen
     */
    private Screen screen;

    private RenderGlobal renderGlobal;

    @Override
    protected void initialize() {
        renderGlobal = new RenderGlobal(terrain);
        world.inject(renderGlobal);

        eventSystem.registerEvents(this);
    }

    @Override
    protected void begin() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    protected void processSystem() {
        // Screen updating
        if (screen != null) {
            screen.render(Gdx.graphics.getDeltaTime());
        }
        else {
            if (assetsLoadingSystem.loaded()) {
                renderGlobal.render();
            }
        }
    }

    @Subscribe
    public void resize(GdxResizeEvent event) {
        if (screen != null) {
            screen.resize(event.width, event.height);
        }
        logger.info("Window resized : " + event.width + "x" + event.height);
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
        }

        this.screen = screen;

        if (this.screen != null) {
            world.inject(screen);
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else {
            Gdx.input.setCursorCatched(true);
            Gdx.input.setInputProcessor(playerInputSystem);
        }

        logger.debug("Setting screen : " + (screen == null ? "null" : screen.getClass()));
    }

    public void dispose() {
        if (screen != null) {
            screen.hide();
            screen.dispose();
        }

        renderGlobal.dispose();
    }

    @Subscribe
    public void pause(GdxPauseEvent event) {
        if (screen != null) {
            screen.pause();
        }
    }

    @Subscribe
    public void resume(GdxResumeEvent event) {
        if (screen != null) {
            screen.resume();
        }
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return screen;
    }
}