package com.ylinor.client.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.pregame.LoadingScreen;
import com.ylinor.client.screen.pregame.MainMenuScreen;

import net.mostlyoriginal.api.event.common.EventSystem;


public class AssetsLoadingSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(AssetsLoadingSystem.class);
    
    /**
     * L'instance des assets
     */
    @Wire
    private Assets assets;

    @Wire
    private EventSystem eventSystem;
    
    @Wire
    private RenderSystem renderSystem;

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
    
    @Override
    protected void initialize() {
        assetsTime = System.currentTimeMillis();
        assets.preload();
    }

    @Override
    protected void processSystem() {
        // Assets loading
        if (assets.update() && !loaded) {
            if (!preloaded) {
                logger.info("Pre-assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = System.currentTimeMillis();
                assets.load();

                preloaded = true;

                renderSystem.setScreen(new LoadingScreen());
            }
            else {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = 0;
                loaded = true;

                renderSystem.setScreen(new MainMenuScreen());
            }
        }
    }
    
    public boolean loaded() {
        return loaded;
    }
    
    public boolean preloaded() {
        return preloaded;
    }
}
