package com.ylinor.client.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
	 * Images folder
	 */
	public static final String UI_IMAGES_FOLDER = IMAGES_FOLDER + "ui/";

	/**
	 * LibGDX's assets manager
	 */
	private final AssetManager assets;

	/**
	 * Screens's assets
	 */
	public final ScreenAssets screen;

	public final BlockAssets blockAssets;

	public Assets() {
		assets = new AssetManager(new InternalFileHandleResolver());
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assets.getFileHandleResolver()));

		screen = new ScreenAssets(this);
		blockAssets = new BlockAssets(this);
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
		blockAssets.load();
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
	 * Loads a texture.
	 *
	 * @param file
	 *            the texture's file
	 */
	public void loadTexture(String file) {
		load(file, Texture.class);
	}

	/**
	 * Loads a pixmap.
	 *
	 * @param file
	 *            the pixmap's file
	 */
	public void loadPixmap(String file) {
		load(file, Pixmap.class);
	}

	/**
	 * Load the police
	 *
	 * @param file
	 *            the police's file
	 */
	public void loadFont(String file) {
		load(file, FreeTypeFontGenerator.class);
	}

	/**
	 * Load an assets
	 *
	 * @param file
	 *            the asset's file
	 * @param type
	 *            the asset to load
	 */
	public void load(String file, Class<?> type) {
		assets.load(file, type);
	}

	/**
	 * @param file
	 *            The file of the asset
	 * @param <T>
	 *            the asset type
	 *
	 * @return the asset
	 */
	public <T> T get(String file) {
		return assets.get(file);
	}

	public static NinePatch fromPixmap(Pixmap pixmap) {
		Pixmap realTexture = new Pixmap(pixmap.getWidth() - 2, pixmap.getHeight() - 2, pixmap.getFormat());
		realTexture.drawPixmap(pixmap, 0, 0, 1, 1, pixmap.getWidth() - 2, pixmap.getHeight() - 2);

		int top = 0;
		while (top < pixmap.getHeight() - 1 && pixmap.getPixel(0, top) == 0) {
			top++;
		}

		int bottom = 0;
		while (bottom < pixmap.getHeight() - 1 && pixmap.getPixel(0, pixmap.getHeight() - 1 - bottom) == 0) {
			bottom++;
		}

		int left = 0;
		while (left < pixmap.getWidth() && pixmap.getPixel(0, left) == 0) {
			left++;
		}

		int right = 0;
		while (right < pixmap.getWidth() - 1 && pixmap.getPixel(pixmap.getWidth() - 1 - right, 0) == 0) {
			right++;
		}

		return new NinePatch(new Texture(realTexture), left - 1, right - 1, top - 1, bottom - 1);
	}
}
