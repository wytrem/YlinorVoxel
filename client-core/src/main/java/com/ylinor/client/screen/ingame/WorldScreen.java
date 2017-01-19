package com.ylinor.client.screen.ingame;

import com.ylinor.client.render.TerrainRenderer;
import com.ylinor.client.screen.YlinorScreen;


/**
 * On this screen, the world will be loaded and rendered
 **/
public class WorldScreen extends YlinorScreen {
    /* Le moteur de rendu de monde (cf WordRenderer et Renderer */
    private TerrainRenderer renderer;

    public WorldScreen(TerrainRenderer renderer) {
        super();
        this.renderer = renderer;
    }

    public TerrainRenderer getRenderer() {
        return renderer;
    }
}
