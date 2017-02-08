package com.ylinor.client.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.VisUI;


/**
 * Game's assets (Singleton)
 *
 * This class contains all constants of assets's folder The
 * {@link AssetManager}, and the other public assets class as
 * {@link ScreenAssets}.
 *
 * It contains load and preload function to precharge assets
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Assets implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(Assets.class);

    /**
     * Font folder
     */
    public static final String FONTS_FOLDER = "fonts/";

    /**
     * Images folder
     */
    public static final String IMAGES_FOLDER = "img/";

    /**
     * LibGDX's assets manager
     */
    private final AssetManager assets;

    /**
     * Screens's assets
     */
    public final ScreenAssets screenAssets;

    public Assets() {
        assets = new AssetManager(new InternalFileHandleResolver());
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));

        screenAssets = new ScreenAssets(this);
    }

    /**
     * Load pre-assets (used before or during the loading screen)
     */
    public void preload() {
        logger.info("Loading pre-assets (including VisUI)...");

        VisUI.load();
        screenAssets.preload();
    }

    /**
     * Load assets.
     *
     * @return if assets are completely loaded
     */
    public boolean update() {
        return assets.update();
    }

    /**
     * Load assets
     */
    public void load() {
        logger.info("Loading assets...");

        screenAssets.load();
    }

    @Override
    public void dispose() {
        logger.info("Disposing assets...");

        assets.dispose();
        VisUI.dispose();

        logger.info("Done !");
    }

    /**
     * @return the number of loaded assets
     */
    public int assetsLoaded() {
        return assets.getLoadedAssets();
    }

    /**
     * @return The number of assets to load
     */
    public int assetsToLoad() {
        return assets.getQueuedAssets();
    }

    /**
     * Load a texture
     *
     * @param file the texture's file
     */
    public void loadTexture(String file) {
        load(file, Texture.class);
    }

    /**
     * Load the police
     *
     * @param file the police's file
     */
    public void loadFont(String file) {
        load(file, FreeTypeFontGenerator.class);
    }

    /**
     * Load an assets
     *
     * @param file the asset's file
     * @param type the asset to load
     */
    public void load(String file, Class<?> type) {
        assets.load(file, type);
    }

    /**
     * @param file The file of the asset
     * @param <T> the asset type
     *
     * @return the asset
     */
    public <T> T get(String file) {
        return assets.get(file);
    }
}
