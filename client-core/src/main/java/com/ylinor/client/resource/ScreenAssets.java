package com.ylinor.client.resource;

import static com.ylinor.client.resource.Assets.FONTS_FOLDER;
import static com.ylinor.client.resource.Assets.IMAGES_FOLDER;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


/**
 * Contains all screens's assets
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class ScreenAssets implements Loadable, Preloadable {
    private final Assets assets;

    ScreenAssets(Assets assets) {
        this.assets = assets;
    }

    @Preloaded
    public final String splash = IMAGES_FOLDER + "splash.jpg";

    public final String logo = IMAGES_FOLDER + "logo.png";
    public final String augustus = FONTS_FOLDER + "AUGUSTUS.TTF";
    public final String liberation = FONTS_FOLDER + "LiberationSerif-Regular.ttf";

    public void preload() {
        assets.loadTexture(splash);
    }

    public void load() {
        //assets.loadTexture(logo);
        assets.loadFont(augustus);
        assets.loadFont(liberation);
    }

    @Preloaded
    public Texture splash() {
        return assets.get(splash);
    }

    public Texture logo() {
        return assets.get(logo);
    }

    public FreeTypeFontGenerator augustus() {
        return assets.get(augustus);
    }

    public FreeTypeFontGenerator liberation() {
        return assets.get(liberation);
    }
}
