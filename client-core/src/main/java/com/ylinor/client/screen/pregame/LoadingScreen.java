package com.ylinor.client.screen.pregame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.YlinorScreen;


/**
 * Loading screen, launch after the assets's preloading, display the assets's
 * loading
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class LoadingScreen extends YlinorScreen {
    /* Logo affiché au milieu de l'écran */
    private Image logo;

    /* La progressBar */
    private ProgressBar loading;

    /* Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private Assets assets;

    /* Appele lors de la création du screen */
    @Override
    public void show() {
        /* Définit l'InputProcessor (cf YlinorScreen) */
        super.show();

        /* Créer le logo a partire de la classe Asset */
        logo = new Image(assets.screen.splash());
        this.addActor(logo);

        /* Créer la ProgressBar */
        loading = new VisProgressBar(0, assets.assetsToLoad() + assets.assetsLoaded(), 1, false);
        this.addActor(loading);

        logger.debug(assets.assetsLoaded() + "/" + assets.assetsToLoad() + " assets loaded");
    }

    @Override
    public void resize(int width, int height) {
        /* Appeler la méthode super, voire YlinorClient */
        super.resize(width, height);

        /* Definir la nouvelle taille du logo */
        logo.setSize(450, 450);

        /* Changer sa position en fonction de la taille de la fenetre. */
        logo.setPosition(width / 2 - logo.getWidth() / 2, height / 2 - logo.getHeight() / 2);

        /*
         * Changer la taille de la progress bar en fonction de la taille de la
         * fenetre
         */
        loading.setBounds(0, 0, width, 20);
    }

    @Override
    public void render(float delta) {
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.begin(ShapeType.Filled);

        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, getWidth(), getHeight());

        renderer.end();

        /* Appeller la méthode super (cf YlinorClient) */
        super.render(delta);

        /*
         * Changer la valeur de la progress bar en fonction du nombre d'assets
         * chargés
         */
        int value = assets.assetsLoaded();

        if (value != loading.getValue())
        {
            try
            {
                Thread.sleep(750L);
            }
            catch (InterruptedException e)
            {
            }

            loading.setValue(assets.assetsLoaded());
        }
    }
}
