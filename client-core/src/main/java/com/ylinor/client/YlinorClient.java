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

    //    /**
    //     * Instance du système reseau client
    //     */
    //    private ClientNetwork<ServerEntity> clientNetwork;
    //
    //    /**
    //     * Protocl de redirection de packet
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
