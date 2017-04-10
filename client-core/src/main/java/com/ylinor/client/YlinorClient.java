package com.ylinor.client;

import static com.ylinor.library.api.ecs.ArtemisUtils.dispatchEvent;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.ArtemisMultiException;
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
import com.ylinor.client.physics.systems.PhySystem;
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
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.packets.NetworkableObjects;
import com.ylinor.packets.PacketLogin;

import net.mostlyoriginal.api.network.marshal.common.MarshalObserver;
import net.mostlyoriginal.api.network.marshal.common.MarshalState;
import net.mostlyoriginal.api.network.marshal.kryonet.KryonetClientMarshalStrategy;
import net.mostlyoriginal.api.network.system.MarshalSystem;


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

    private KryonetClientMarshalStrategy kryonetClientMarshalStrategy;
    private MarshalSystem marshalSystem;

    private World world;
    private String host = "localhost";
    private int port = 8529;

    public boolean isInGame = false;

    public YlinorClient() {
        instance = this;
        client = this;
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

    public void connectToServer() {
        logger.info("Connecting to server {}:{}.", host, port);
        marshalSystem.start();

        if (marshalSystem.getState() != MarshalState.STARTED) {
            logger.error("Network start failed, aborting.");
            System.exit(-1);
        }
        
        kryonetClientMarshalStrategy.sendToAll(new PacketLogin());
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

        kryonetClientMarshalStrategy = new KryonetClientMarshalStrategy("localhost", 32321);
        marshalSystem = new MarshalSystem(NetworkableObjects.MARSHAL_DICTIONARY, kryonetClientMarshalStrategy);
        configuration.setSystem(marshalSystem);
        marshalSystem.getMarshal().setListener(new MarshalObserver() {
            @Override
            public void received(int connectionId, Object object) {
                logger.info("Received (from {}) : {}", connectionId, object);
            }

            @Override
            public void disconnected(int connectionId) {
                logger.info("Disconnected {}", connectionId);
            }

            @Override
            public void connected(int connectionId) {
                logger.info("Connected {}", connectionId);
            }
        });
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

        try {
            world.dispose();
        }
        catch (ArtemisMultiException e) {
            e.getExceptions().stream().forEach(ex -> {
                logger.error("Error while disposing world :", ex);
            });
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

    private static YlinorClient client;

    public static final YlinorClient client() {
        return client;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
