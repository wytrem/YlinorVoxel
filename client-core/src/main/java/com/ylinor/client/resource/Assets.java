package com.ylinor.client.resource;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.VisUI;
import com.ylinor.client.render.model.ModelDeserializer;
import com.ylinor.client.render.model.ModelRegistry;


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
     * Models folder
     */
    public static final String MODELS_FOLDER = "models/";

    /**
     * Class instance
     */
    private static Assets instance = new Assets();

    /**
     * LibGDX's assets manager
     */
    private final AssetManager assets;

    /**
     * Screens's assets
     */
    public final ScreenAssets screen;

    /**
     * The folder where the models are
     */
    private FileHandle modelFolder;

    private Assets() {
        assets = new AssetManager(new InternalFileHandleResolver());
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));

        screen = new ScreenAssets(this);
    }

    /**
     * Load pre-assets (used before or during the loading screen)
     */
    public void preload() {
        logger.info("Loading pre-assets (including VisUI)...");

        VisUI.load();
        screen.preload();
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

        screen.load();

        modelFolder = Gdx.files.internal(MODELS_FOLDER);

        // Models
        try {
            TextureAtlas atlas = new TextureAtlas();
            atlas.loadFrom(new File("/home/victor/Ylinor/atlas"), false, 2048);
            //            BlockModel[] models = ModelDeserializer.read(new File(modelFolder, "test.json"), new ModelRegistry(), atlas).getModel();
            ModelDeserializer test = ModelDeserializer.read(modelFolder.child("test.json"), new ModelRegistry(), atlas, fileName -> modelFolder.child(fileName + ".json"));
            test.deserialize();
            System.out.println(test.variants);

            System.out.println("ON A REUSSSI !!!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

    public FileHandle getModelFolder() {
        return modelFolder;
    }

    /**
     * @return the class instance
     */
    public static Assets get() {
        return instance;
    }
}
