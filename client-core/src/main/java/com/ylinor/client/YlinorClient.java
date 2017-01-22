package com.ylinor.client;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.pregame.LoadingScreen;
import com.ylinor.client.screen.pregame.MainMenuScreen;
import com.ylinor.client.util.YlinorFiles;
import com.ylinor.client.util.settings.GameSettings;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.world.World;
import com.ylinor.library.api.world.storage.StorageManager;


/**
 * Le client Ylinor
 *
 * Le jeu principal, fait les actions de base, gère les screens, etc...
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorClient extends YlinorApplication implements ApplicationListener
{
    /**
     * La verision du client
     */
    public static final String VERSION = "0.0.1";

    /**
     * Un logger Wylog
     */
    private static final Logger logger = LoggerFactory.getLogger(YlinorClient.class);

    /**
     * L'instance des assets
     */
    private Assets assets = Assets.get();

    /**
     * Si le preloading (chargement des assets utilisés avant/pendant le
     * chargement) a été fait
     */
    private boolean preloaded = false;

    /**
     * Si le loading a été fait (chargement des assets)
     */
    private boolean loaded = false;

    /**
     * Le temps actuel (System.currentTimeMillis())
     */
    private long assetsTime;

    /**
     * User settings of the game
     */
    private GameSettings settings;

    /**
     * Current screen
     */
    private Screen screen;

    /**
     * Current world
     */
    private World world;

    //    /**
    //     * Instance du système reseau client
    //     */
    //    private ClientNetwork<ServerEntity> clientNetwork;
    //
    //    /**
    //     * Protocl de redirection de packet
    //     */
    //    private IProtocol<ServerEntity> protocol;


    public YlinorClient()
    {
        instance = this;
    }

    @Override
    public void create()
    {
        logger.info("Loading Ylinor Client v" + VERSION);

        assetsTime = System.currentTimeMillis();
        assets.preload();

        try
        {
            settings = GameSettings.get(new File(YlinorFiles.getGameFolder(), "settings.json"));
            settings.save(new File(YlinorFiles.getGameFolder(), "settings.json"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        world = new World(null);
        world.setStorage(new StorageManager(world, new File(YlinorFiles.getGameFolder(), "world")));

        //        protocol = new HandlerProtocol<>();

        //        clientNetwork = new ClientNetwork<>(new Kryo(), "127.0.0.1", 25565, protocol, ServerEntity::new);
        //        clientNetwork.start();
    }

    @Override
    public void render()
    {
        // Clearing screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Assets loading
        if (assets.update() && !loaded)
        {
            if (!preloaded)
            {
                logger.info("Pre-assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = System.currentTimeMillis();
                assets.load();

                preloaded = true;

                setScreen(new LoadingScreen());
            }
            else
            {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = 0;
                loaded = true;

                setScreen(new MainMenuScreen());
            }
        }

        // Screen updating
        if (screen != null)
        {
            screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height)
    {
        if (screen != null)
        {
            screen.resize(width, height);
        }
        logger.debug("Window resized : " + width + "x" + height);
    }

    @Override
    public void dispose()
    {
        logger.info("Closing !");
        assets.dispose();
        //        clientNetwork.end();

        if (screen != null)
            screen.hide();

        logger.info("Bye");
    }

    @Override
    public void pause()
    {
        if (screen != null)
        {
            screen.pause();
        }
    }

    @Override
    public void resume()
    {
        if (screen != null)
        {
            screen.resume();
        }
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen()
    {
        return screen;
    }

    public void setScreen(@NotNull Screen screen)
    {
        if (this.screen != null)
        {
            this.screen.hide();
        }
        this.screen = screen;
        this.screen.show();
        this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        logger.debug("Setting screen : " + screen.getClass().getSimpleName());
    }

    public GameSettings getSettings()
    {
        return settings;
    }
}
