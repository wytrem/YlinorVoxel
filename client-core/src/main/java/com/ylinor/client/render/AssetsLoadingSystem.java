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

    @Wire
    private Assets assets;

    @Wire
    private EventSystem eventSystem;

    @Wire
    private WorldRenderSystem renderSystem;
    
    @Wire
    private ScreenSystem screenSystem;

    /**
     * If "prelaoding assets" are completely loaded. "Preloading assets" are the
     * needed assets to process to the standard assets loading (e.g. logo and
     * progress bar).
     */
    private boolean preloaded = false;

    /**
     * If assets are completely loaded.
     */
    private boolean loaded = false;

    /**
     * ???
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

                screenSystem.setScreen(new LoadingScreen());
            }
            else {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = 0;
                loaded = true;

                screenSystem.setScreen(new MainMenuScreen());
            }
        }
    }

    @Override
    protected void dispose() {
        assets.dispose();
    }

    public boolean loaded() {
        return loaded;
    }

    public boolean preloaded() {
        return preloaded;
    }
}
