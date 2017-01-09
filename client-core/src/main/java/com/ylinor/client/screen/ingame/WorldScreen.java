package com.ylinor.client.screen.ingame;

import com.ylinor.client.render.WorldRenderer;
import com.ylinor.client.screen.YlinorScreen;


/**
 * On this screen, the world will be loaded and rendered
 **/
public class WorldScreen extends YlinorScreen {
    /* Le moteur de rendu de monde (cf WordRenderer et Renderer */
    private WorldRenderer renderer;

    public WorldScreen(WorldRenderer renderer) {
        super();
        this.renderer = renderer;
    }

    public WorldRenderer getRenderer() {
        return renderer;
    }
}
