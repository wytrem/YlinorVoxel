package com.ylinor.client;

import static com.ylinor.library.api.ecs.ArtemisUtils.dispatchEvent;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ylinor.client.events.GdxPauseEvent;
import com.ylinor.client.events.GdxResizeEvent;
import com.ylinor.client.events.GdxResumeEvent;
import com.ylinor.client.physics.GravitySystem;
import com.ylinor.client.physics.PhysicsSystem;
import com.ylinor.client.physics.PlayerInputSystem;
import com.ylinor.client.render.AssetsLoadingSystem;
import com.ylinor.client.render.CameraSystem;
import com.ylinor.client.render.HudRenderSystem;
import com.ylinor.client.render.PlayerInitSystem;
import com.ylinor.client.render.TerrainRenderSystem;
import com.ylinor.client.render.TestChunkProvider;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.util.YlinorFiles;
import com.ylinor.client.util.settings.GameSettings;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;


/**
 * Le client Ylinor
 *
 * Le jeu principal, fait les actions de base, gère les screens, etc...
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorClient extends YlinorApplication
                implements ApplicationListener {
    /**
     * La verision du client
     */
    public static final String VERSION = "0.0.1";

    /**
     * Un logger Wylog
     */
    private static final Logger logger = LoggerFactory.getLogger(YlinorClient.class);

    /**
     * User settings of the game
     */
    private GameSettings settings;

    /**
     * Current world
     */
    private Terrain terrain;

    private World world;

    public YlinorClient() {
        instance = this;
        client = this;
    }

    @Override
    public void create() {
        logger.info("Loading Ylinor Client v" + VERSION);

        try {
            settings = GameSettings.get(new File(YlinorFiles.getGameFolder(), "settings.json"));
            settings.save(new File(YlinorFiles.getGameFolder(), "settings.json"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        TestChunkProvider test = new TestChunkProvider();

        terrain = new Terrain(test);
        test.setWorld(terrain);

        world = buildWorld();
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
        configuration.register(terrain);
        configuration.register(new Assets());
        configuration.register(this);
        // Initializing player
        configuration.setSystem(PlayerInitSystem.class);

        // Loading assets
        configuration.setSystem(AssetsLoadingSystem.class);

        // Input processing
        configuration.setSystem(PlayerInputSystem.class);

        // Physics management
        configuration.setSystem(GravitySystem.class);
        configuration.setSystem(PhysicsSystem.class);

        // Camera follows player entity
        configuration.setSystem(CameraSystem.class);

        // Render the terrain
        configuration.setSystem(TerrainRenderSystem.class);

        configuration.setSystem(HudRenderSystem.class);
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
    }

    @Override
    public void resize(int width, int height) {
        dispatchEvent(world, new GdxResizeEvent(width, height));
    }

    public void setScreen(Screen screen) {
        world.getSystem(TerrainRenderSystem.class).setScreen(screen);
    }

    @Override
    public void dispose() {
        logger.info("Disposing world.");
        world.dispose();
        logger.info("Stopping!");
    }

    @Override
    public void pause() {
        dispatchEvent(world, new GdxPauseEvent());
    }

    @Override
    public void resume() {
        dispatchEvent(world, new GdxResumeEvent());
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return world.getSystem(TerrainRenderSystem.class).getScreen();
    }

    public GameSettings getSettings() {
        return settings;
    }

    private static YlinorClient client;

    public static final YlinorClient client() {
        return client;
    }
}
