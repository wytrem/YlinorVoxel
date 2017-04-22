package com.ylinor.client;

import static com.ylinor.library.api.ecs.ArtemisUtils.dispatchEvent;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import com.ylinor.client.events.GdxPauseEvent;
import com.ylinor.client.events.GdxResizeEvent;
import com.ylinor.client.events.GdxResumeEvent;
import com.ylinor.client.input.GdxInputDispatcherSystem;
import com.ylinor.client.input.PlayerInputSystem;
import com.ylinor.client.network.ClientNetworkSystem;
import com.ylinor.client.network.PositionSyncSystem;
import com.ylinor.client.physics.systems.PhySystem;
import com.ylinor.client.render.AssetsLoadingSystem;
import com.ylinor.client.render.CameraSystem;
import com.ylinor.client.render.ClearScreenSystem;
import com.ylinor.client.render.HudRenderSystem;
import com.ylinor.client.render.ScreenSystem;
import com.ylinor.client.render.TerrainRenderSystem;
import com.ylinor.client.terrain.ClientTerrain;
import com.ylinor.client.util.YlinorFiles;
import com.ylinor.client.util.settings.GameSettings;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;


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
     * La verision du client.
     */
    public static final String VERSION = "Espilon 0.1";

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
    }

    @Override
    public void create() {
        logger.info("Loading Ylinor Client version {}", getVersion());

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
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
        
        
        configuration.with(SystemsPriorities.Update.UPDATE_PRIORITY, ClientNetworkSystem.class);

        configuration.with(SystemsPriorities.Update.UPDATE_PRIORITY, AssetsLoadingSystem.class, GdxInputDispatcherSystem.class, PlayerInputSystem.class, PhySystem.class, PositionSyncSystem.class);

        configuration.with(SystemsPriorities.RENDER_PRIORITY, CameraSystem.class, ClearScreenSystem.class, TerrainRenderSystem.class, HudRenderSystem.class, ScreenSystem.class);
    
        // We want to inject any Terrain field with this
        
        
        configuration.with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Terrain.class).toInstance(terrain);
                bind(YlinorApplication.class).toInstance(YlinorClient.this);
                bind(YlinorClient.class).toInstance(YlinorClient.this);
            }
        });
    }

    @Override
    public void render() {
        world.delta = Gdx.graphics.getDeltaTime();
        world.process();
    }

    @Override
    public void resize(int width, int height) {
        dispatchEvent(world, new GdxResizeEvent(width, height));
    }

    @Override
    public void dispose() {
        logger.info("Disposing world.");

        try {
            world.dispose();
        }
        catch (Exception ex) {
            logger.error("Error while disposing world :", ex);
        }
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

    @Override
    public String getVersion() {
        return VERSION;
    }
}
