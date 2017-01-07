package com.ylinor.client;

import static com.badlogic.gdx.Gdx.gl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.pregame.LoadingScreen;
import com.ylinor.client.screen.pregame.MainMenuScreen;


/**
 * Le client Ylinor
 *
 * Le jeu principal, fait les actions de base, gère les screens, etc...
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorClient extends Game {
    /**
     * Client version.
     */
    public static final String VERSION = "0.0.1";

    /**
     * Wylog logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(YlinorClient.class);

    /**
     * Assets instances.
     */
    private Assets assets = Assets.get();

    /**
     * If the preloading (assets's loading used before and during the loading) is done.
     */
    private boolean preloaded = false;

    /**
     * If the loading is done (assets loading)
     */
    private boolean loaded = false;

    /**
     * Current time (System.currentTimeMillis() )
     */
    private long assetsTime;

    //    /**
    //     * Network system instance
    //     */
    //    private ClientNetwork<ServerEntity> clientNetwork;
    //
    //    /**
    //     * Packet redirection protocol
    //     */
    //    private IProtocol<ServerEntity> protocol;

    @Override
    public void create() {
        logger.info("Loading Ylinor Client v" + VERSION);

        assetsTime = System.currentTimeMillis();
        assets.preload();

        //        protocol = new HandlerProtocol<>();

        //        clientNetwork = new ClientNetwork<>(new Kryo(), "127.0.0.1", 25565, protocol, ServerEntity::new);
        //        clientNetwork.start();
    }

    @Override
    public void render() {
        // Clearing screen
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Assets loading
        if (assets.update() && !loaded) {
            if (!preloaded) {
                logger.info("Pre-assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = System.currentTimeMillis();
                assets.load();

                preloaded = true;

                setScreen(new LoadingScreen());
            }
            else {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = 0;
                loaded = true;

                setScreen(new MainMenuScreen());
            }
        }

        // Screen updating
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        logger.debug("Window resized : " + width + "x" + height);
    }

    @Override
    public void setScreen(@NotNull Screen screen) {
        super.setScreen(screen);

        logger.debug("Setting screen : " + screen.getClass().getSimpleName());
    }

    @Override
    public void dispose() {
        logger.info("Closing !");
        assets.dispose();
        //        clientNetwork.end();

        logger.info("Bye");
    }
}
