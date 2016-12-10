package com.ylinor.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.pregame.LoadingScreen;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Le client Ylinor
 *
 * Le jeu principal, fait les actions de base, gère
 * les screens, etc...
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorClient extends Game
{
    /**
     * La verision du client
     */
    public static final String VERSION = "0.0.1";

    /**
     * Un logger Wylog
     */
    private final Logger logger = LoggerFactory.getLogger(YlinorClient.class);

    /**
     * L'instance des assets
     */
    private Assets assets = Assets.get();

    /**
     * Si le preloading (chargement des assets utilisés
     * avant/pendant le chargement) a été fait
     */
    private boolean preload = false;

    /**
     * Si le loading a été fait (chargement des assets)
     */
    private boolean load = false;

    /**
     * Le temps actuel (System.currentTimeMillis())
     */
    private long assetsTime;

    @Override
    public void create()
    {
        logger.info("Loading Ylinor Client v" + VERSION);

        assetsTime = System.currentTimeMillis();
        assets.preload();
    }

    @Override
    public void render()
    {
        // Clearing screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Assets loading
        if (assets.update() && !load)
        {
            if (!preload)
            {
                logger.info("Pre-assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = System.currentTimeMillis();
                assets.load();

                preload = true;

                setScreen(new LoadingScreen());
            }
            else
            {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = 0;
                load = true;

                // setScreen(new MainMenuScreen());
            }
        }

        // Screen updating
        super.render();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);

        logger.debug("Window resized : " + width + "x" + height);
    }

    @Override
    public void setScreen(@NotNull Screen screen)
    {
        super.setScreen(screen);

        logger.debug("Setting screen : " + screen.getClass().getSimpleName());
    }

    @Override
    public void dispose()
    {
        logger.info("Closing !");
        assets.dispose();

        logger.info("Bye");
    }
}
