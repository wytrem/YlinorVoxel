package com.ylinor.client;

import static com.ylinor.library.api.ecs.ArtemisUtils.dispatchEvent;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.WorldConfigurationBuilder.Priority;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.ylinor.client.events.GdxPauseEvent;
import com.ylinor.client.events.GdxResizeEvent;
import com.ylinor.client.events.GdxResumeEvent;
import com.ylinor.client.input.GdxInputDispatcherSystem;
import com.ylinor.client.input.PlayerInputSystem;
import com.ylinor.client.physics.alamano.PhySystem;
import com.ylinor.client.physics.alamano.Physics;
import com.ylinor.client.physics.bullet.BulletDynamicsProcessingSystem;
import com.ylinor.client.physics.bullet.BulletEntitiesSystem;
import com.ylinor.client.render.AssetsLoadingSystem;
import com.ylinor.client.render.CameraSystem;
import com.ylinor.client.render.ClearScreenSystem;
import com.ylinor.client.render.HudRenderSystem;
import com.ylinor.client.render.PlayerInitSystem;
import com.ylinor.client.render.ScreenSystem;
import com.ylinor.client.render.TerrainRenderSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.terrain.ClientTerrain;
import com.ylinor.client.util.YlinorFiles;
import com.ylinor.client.util.settings.GameSettings;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.ecs.systems.Timer;
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

    public boolean isInGame = false;

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

        terrain = new ClientTerrain();

        world = buildWorld();
    }

    @Override
    protected void preConfigure(WorldConfigurationBuilder configurationBuilder) {
        super.preConfigure(configurationBuilder);
        configurationBuilder.dependsOn(Priority.HIGHEST, PlayerInitSystem.class);

        configurationBuilder.dependsOn(SystemsPriorities.Update.UPDATE_PRIORITY, AssetsLoadingSystem.class, GdxInputDispatcherSystem.class, PlayerInputSystem.class, PhySystem.class);

        configurationBuilder.dependsOn(SystemsPriorities.RENDER_PRIORITY, CameraSystem.class, ClearScreenSystem.class, TerrainRenderSystem.class, HudRenderSystem.class, ScreenSystem.class);
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
        // We want to inject any Terrain field with this
        configuration.register(Terrain.class.getName(), terrain);
        configuration.register(terrain);
        configuration.register(new Assets());
        configuration.register(this);
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

    public GameSettings getSettings() {
        return settings;
    }

    private static YlinorClient client;

    public static final YlinorClient client() {
        return client;
    }
}
