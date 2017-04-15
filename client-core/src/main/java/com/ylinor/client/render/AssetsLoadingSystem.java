package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylinor.client.events.AssetsLoadedEvent;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.pregame.LoadingScreen;
import com.ylinor.client.screen.pregame.MainMenuScreen;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.library.util.ecs.system.EventSystem;


@Singleton
public class AssetsLoadingSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(AssetsLoadingSystem.class);

    @Inject
    private Assets assets;

    @Inject
    private EventSystem eventSystem;

    @Inject
    private TerrainRenderSystem renderSystem;
    
    @Inject
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
    public  void initialize() {
        assetsTime = System.currentTimeMillis();
        assets.beginPreloding();
    }

    @Override
    protected void processSystem() {
        // Assets loading
        if (assets.update() && !loaded) {
            if (!preloaded) {
                logger.info("Pre-assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");

                assetsTime = System.currentTimeMillis();
                assets.beginLoading();

                preloaded = true;

                screenSystem.setScreen(new LoadingScreen());
            }
            else {
                logger.info("Assets loaded in " + (System.currentTimeMillis() - assetsTime) + "ms");
                
                assetsTime = 0;
                loaded = true;

                eventSystem.dispatch(new AssetsLoadedEvent());
                screenSystem.setScreen(new MainMenuScreen());
            }
        }
    }

    @Override
    public  void dispose() {
        if (assets != null)
            assets.dispose();
    }

    public boolean loaded() {
        return loaded;
    }

    public boolean preloaded() {
        return preloaded;
    }
}
