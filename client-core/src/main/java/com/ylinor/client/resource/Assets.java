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
 * Les Assets du jeux (Singleton)
 *
 * Cette classe contient les constantes des dossiers d'assets,
 * l'{@link AssetManager}, et les différentes classes d'assets en public comme
 * {@link ScreenAssets}.
 *
 * Elle contient aussi les fonctions load et preload pour charger ou precharger
 * les assets.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Assets implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(Assets.class);

    /**
     * Le dossiers des polices
     */
    public static final String FONTS_FOLDER = "fonts/";

    /**
     * Le dossier des images
     */
    public static final String IMAGES_FOLDER = "img/";

    /**
     * L'instance de la classe
     */
    private static Assets instance = new Assets();

    /**
     * L'assets manager LibGDX
     */
    private final AssetManager assets;

    /**
     * Les assets des Screens
     */
    public final ScreenAssets screen;

    private Assets() {
        assets = new AssetManager(new InternalFileHandleResolver());
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));

        screen = new ScreenAssets(this);
    }

    /**
     * Charge les pré-assets, càd les assets utilisés avant ou pendant l'écran
     * de chargement
     */
    public void preload() {
        logger.info("Loading pre-assets (including VisUI)...");

        VisUI.load();
        screen.preload();
    }

    /**
     * Charge les assets
     *
     * @return Si les assets sont entièrement chargés
     */
    public boolean update() {
        return assets.update();
    }

    /**
     * Charge les assets
     */
    public void load() {
        logger.info("Loading assets...");

        screen.load();
    }

    @Override
    public void dispose() {
        logger.info("Disposing assets...");

        assets.dispose();
        VisUI.dispose();

        logger.info("Done !");
    }

    /**
     * @return Le nombre d'assets chargés
     */
    public int assetsLoaded() {
        return assets.getLoadedAssets();
    }

    /**
     * @return Le nombre d'assets encore à charger
     */
    public int assetsToLoad() {
        return assets.getQueuedAssets();
    }

    /**
     * Charge une texture
     *
     * @param file Le fichier de la texture
     */
    public void loadTexture(String file) {
        load(file, Texture.class);
    }

    /**
     * Charge la police
     *
     * @param file Le fichier de la police
     */
    public void loadFont(String file) {
        load(file, FreeTypeFontGenerator.class);
    }

    /**
     * Charge un asset
     *
     * @param file Le fichier de l'asset
     * @param type L'asset à charger
     */
    public void load(String file, Class<?> type) {
        assets.load(file, type);
    }

    /**
     * Retourne un des assets chargés
     *
     * @param file Le fichier correspondant à l'asset
     * @param <T> Le type d'asset
     *
     * @return L'asset en question
     */
    public <T> T get(String file) {
        return assets.get(file);
    }

    /**
     * @return L'instance de cette classe
     */
    public static Assets get() {
        return instance;
    }
}
